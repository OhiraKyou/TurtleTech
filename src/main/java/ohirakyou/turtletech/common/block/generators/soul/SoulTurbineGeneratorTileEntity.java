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

public class SoulTurbineGeneratorTileEntity extends TileEntityPoweredMachine {

    private static final long ENERGY_IO = 10L;
    private static final int POWERED_STATE_DURATION = 10;

    private boolean previouslyRedstonePowered;
    private int remainingPoweredStateTicks;

    public ItemStackHandler inventory = new ItemStackHandler(2);


    public SoulTurbineGeneratorTileEntity() {
        //super(SoulTurbineGeneratorTileEntity.class.getSimpleName(), 2);
        super(SoulTurbineGeneratorTileEntity.class.getSimpleName());
        becomePureEnergyGenerator(ENERGY_IO);
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

    @Override
    public final void tickUpdate(boolean isServerWorld) {
        super.tickUpdate(isServerWorld);

        if (isServer()) {
            if (!previouslyRedstonePowered && hasRedstoneSignal()) {
                generateAndDistributeEnergy();
                remainingPoweredStateTicks = POWERED_STATE_DURATION;
            }

            int previousPoweredTicks = remainingPoweredStateTicks;
            remainingPoweredStateTicks = Math.max(remainingPoweredStateTicks - 1, 0);

            // Has the generator gone too long without a redstone pulse?
            if (previousPoweredTicks > 0 && remainingPoweredStateTicks == 0) {
                // Tell the client about it so visuals can be updated
                sync();
            }

        }

        previouslyRedstonePowered = hasRedstoneSignal();
    }

    public boolean itemHasSoulEnergy(ItemStack stack) {
        return getItemSoulEnergy(stack) > 0 && getItemEnergyRate(stack) > 0;
    }

    public static int getItemSoulEnergy(ItemStack itemStack) {
        Item item = itemStack.getItem();

        if (item == Item.getItemFromBlock(Blocks.SOUL_SAND)) {
            return 100;
        }

        return 0;
    }

    public static int getItemEnergyRate(ItemStack itemStack) {
        Item item = itemStack.getItem();

        if (item == Item.getItemFromBlock(Blocks.SOUL_SAND)) {
            return 10;
        }

        return 0;
    }


    @Override
    public boolean isPowered() {
        return remainingPoweredStateTicks > 0;
    }



    // Data management

    @Override
    public void readFromNBT(NBTTagCompound root) {
        super.readFromNBT(root);
        inventory.deserializeNBT((NBTTagCompound) root.getTag("inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound root) {
        super.writeToNBT(root);
        root.setTag("inventory", inventory.serializeNBT());
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
