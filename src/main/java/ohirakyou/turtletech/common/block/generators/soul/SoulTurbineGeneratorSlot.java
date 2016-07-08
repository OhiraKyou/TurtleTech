package ohirakyou.turtletech.common.block.generators.soul;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class SoulTurbineGeneratorSlot extends SlotItemHandler {

    public SoulTurbineGeneratorSlot(IItemHandler inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    public boolean isItemValid(@Nullable ItemStack stack) {
        if (SoulTurbineGeneratorTileEntity.getItemSoulEnergy(stack) > 0 &&
                SoulTurbineGeneratorTileEntity.getItemEnergyRate(stack) > 0) {
            return true;
        }

        return false;
    }

    public int getItemStackLimit(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack != null && stack.getItem() != null && stack.getItem() == Items.BUCKET;
    }

}