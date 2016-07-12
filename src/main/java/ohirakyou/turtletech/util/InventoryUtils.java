package ohirakyou.turtletech.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InventoryUtils {
    public static List<ItemStack> dropItemHandlerContents(IItemHandler itemHandler, Random random) {
        final List<ItemStack> drops = new ArrayList<>();

        for (int slot = 0; slot < itemHandler.getSlots(); ++slot) {
            while (itemHandler.getStackInSlot(slot) != null) {
                final int amount = random.nextInt(21) + 10;

                if (itemHandler.extractItem(slot, amount, true) != null) {
                    final ItemStack itemStack = itemHandler.extractItem(slot, amount, false);
                    drops.add(itemStack);
                }
            }
        }

        return drops;
    }
}
