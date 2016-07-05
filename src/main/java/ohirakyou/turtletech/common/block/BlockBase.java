package ohirakyou.turtletech.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.common.item.ItemGroups;

public class BlockBase extends Block {
    public BlockBase(Material materialIn) {
        super(materialIn);
        setCreativeTab(ItemGroups.tab_blocks);
    }
}
