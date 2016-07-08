package ohirakyou.turtletech.common.block.generators.soul;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SoulTurbineGeneratorOutputSlot extends SlotItemHandler {
    /** The player that is using the GUI where this slot resides. */
    private EntityPlayer thePlayer;
    private int removeCount;

    public SoulTurbineGeneratorOutputSlot(EntityPlayer player, IItemHandler inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.thePlayer = player;
    }

    public boolean isItemValid(@Nullable ItemStack stack)
    {
        return false;
    }

    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            ItemStack stack = this.getStack();
            if (stack != null) {
                this.removeCount += Math.min(amount, stack.stackSize);
            }
        }

        return super.decrStackSize(amount);
    }

    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        this.onCrafting(stack);
        super.onPickupFromSlot(playerIn, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onCrafting(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack) {
        stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.removeCount);

        this.removeCount = 0;
    }
}