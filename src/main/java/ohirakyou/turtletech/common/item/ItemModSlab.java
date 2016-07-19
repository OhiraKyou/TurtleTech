package ohirakyou.turtletech.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import ohirakyou.turtletech.common.block.BlockModSlab;

public class ItemModSlab extends ItemSlab {
    public ItemModSlab(Block block) {
        super(block, ((BlockModSlab)block).getSingleBlock(), ((BlockModSlab)block).getDoubleBlock());
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return block.getUnlocalizedName();
    }
}
