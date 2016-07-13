package ohirakyou.turtletech.common.block.generators.soul;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import ohirakyou.turtletech.common.tileentity.TileEntityPoweredMachine;
import ohirakyou.turtletech.config.ConfigLongs;
import ohirakyou.turtletech.util.InventoryUtils;
import ohirakyou.turtletech.util.MathUtils;

import javax.annotation.Nullable;
import java.util.List;

public class SoulTurbineGeneratorTileEntity extends TileEntityPoweredMachine {

    /** Energy generated freely when on top of soul sand in the nether */
    private static long energyTrickle;

    private static final int INPUT_SLOT_INDEX = 0;
    private static final int OUTPUT_SLOT_INDEX = 1;

    public ItemStackHandler inventory = new ItemStackHandler(2);

    private ItemStack stackBeingConverted;
    private int currentConversionTicks;


    public SoulTurbineGeneratorTileEntity() {
        super(SoulTurbineGeneratorTileEntity.class.getSimpleName());
        becomePureEnergyGenerator();

        energyTrickle = ConfigLongs.ENERGY_TRICKLE_SOUL_TURBINE_GENERATOR.value;
    }

    @Override
    public final void tickUpdate(boolean isServerWorld) {
        super.tickUpdate(isServerWorld);

        int targetConversionTicks = getItemConversionTicks(stackBeingConverted);

        // Update conversion progress for both the server and client
        if (stackBeingConverted != null && currentConversionTicks < targetConversionTicks) {
            currentConversionTicks = Math.min(currentConversionTicks + 1, targetConversionTicks);
        }

        if (isServer()) {
            // Generate energy during conversion
            if (currentConversionTicks > 0 && stackBeingConverted != null) {
                generateEnergy(getItemEnergyRate(stackBeingConverted));
            }

            // Generate energy when on soul sand in the nether
            if (isNether() && getDownBlock() == Blocks.SOUL_SAND) {
                generateEnergy(energyTrickle);
            }

            // Distribute energy generated this tick
            distributeEnergyFromBuffer();

            // Finish conversion
            if (currentConversionTicks >= targetConversionTicks) {
                boolean needsSync = false;

                // Create output stack
                ItemStack outputStack = getConversionOutput(stackBeingConverted);

                if (outputStack != null) {
                    inventory.insertItem(OUTPUT_SLOT_INDEX, outputStack, false);
                    needsSync = true;
                }

                // Reset for next conversion
                currentConversionTicks = 0;
                stackBeingConverted = null;

                // Start new conversion when a simulated output merges without remainder
                ItemStack nextInputStack = inventory.getStackInSlot(INPUT_SLOT_INDEX);

                if (itemHasSoulEnergy(nextInputStack)) {
                    ItemStack nextOutputStack = getConversionOutput(nextInputStack);

                    if (inventory.insertItem(OUTPUT_SLOT_INDEX, nextOutputStack, true) == null) {
                        stackBeingConverted = inventory.extractItem(INPUT_SLOT_INDEX, 1, false);
                        needsSync = true;
                    }
                }

                // If a conversion was actually finished or started, sync the client
                if (needsSync) {
                    this.sync();
                }

            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.inventory;
        }

        return super.getCapability(capability, facing);
    }

    public static boolean itemHasSoulEnergy(ItemStack stack) {
        return getItemSoulEnergy(stack) > 0;
    }

    public static int getItemConversionTicks(@Nullable ItemStack stack) {
        if (stack == null) {return 0;}
        Item item = stack.getItem();

        if (item == Item.getItemFromBlock(Blocks.SOUL_SAND)) {
            return 125;
        }

        return 0;
    }

    public static int getItemEnergyRate(@Nullable ItemStack stack) {
        if (stack == null) {return 0;}
        Item item = stack.getItem();

        if (item == Item.getItemFromBlock(Blocks.SOUL_SAND)) {
            return 2;
        }

        return 0;
    }

    public static int getItemSoulEnergy(@Nullable ItemStack stack) {
        if (stack == null) {return 0;}
        return getItemEnergyRate(stack) * getItemConversionTicks(stack);
    }

    public static ItemStack getConversionOutput(@Nullable ItemStack stack) {
        if (stack == null) {return null;}
        Item item = stack.getItem();

        if (item == Item.getItemFromBlock(Blocks.SOUL_SAND)) {
            return new ItemStack(Item.getItemFromBlock(Blocks.SAND));
        }

        return null;
    }

    public float getProgressLevel() {
        float totalTicks = getItemConversionTicks(stackBeingConverted);

        if (totalTicks > 0) {
            return MathUtils.clamp01((float)currentConversionTicks / totalTicks);
        }

        return 0;
    }

    public List<ItemStack> getDrops() {
        return InventoryUtils.dropItemHandlerContents(inventory, getWorld().rand);
    }


    @Override
    public boolean isPowered() {
        return currentConversionTicks > 0;
    }


    // Data management

    @Override
    public void readFromNBT(NBTTagCompound root) {
        super.readFromNBT(root);

        if (root.hasKey("inventory")) {
            inventory.deserializeNBT(root.getCompoundTag("inventory"));
        }

        if (root.hasKey("stackBeingConverted")) {
            stackBeingConverted = ItemStack.loadItemStackFromNBT(root.getCompoundTag("stackBeingConverted"));
        } else {
            stackBeingConverted = null;
        }

        if (root.hasKey("currentConversionTicks")) {
            currentConversionTicks = (int)root.getShort("currentConversionTicks");
        }
    }

    protected NBTTagCompound writeSyncNBT(NBTTagCompound root) {
        root.setTag("inventory", inventory.serializeNBT());

        if (stackBeingConverted != null) {
            root.setTag("stackBeingConverted", stackBeingConverted.serializeNBT());
        }

        return root;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound root) {
        super.writeToNBT(root);

        writeSyncNBT(root);

        root.setShort("currentConversionTicks", (short)currentConversionTicks);
        return super.writeToNBT(root);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        readFromNBT(packet.getNbtCompound());
    }

    public NBTTagCompound getUpdateTag() {
        return writeToNBT(super.getUpdateTag());
    }
}
