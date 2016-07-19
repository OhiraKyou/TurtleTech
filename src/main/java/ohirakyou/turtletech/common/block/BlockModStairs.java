package ohirakyou.turtletech.common.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockModStairs extends BlockStairs {
    public BlockModStairs(IBlockState state) {
        super(state);
        useNeighborBrightness = true;
    }
}
