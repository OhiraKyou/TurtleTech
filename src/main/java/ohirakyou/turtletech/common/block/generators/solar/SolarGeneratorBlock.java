package ohirakyou.turtletech.common.block.generators.solar;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.block.BlockMachine;
import ohirakyou.turtletech.common.material.Materials;

public class SolarGeneratorBlock extends BlockMachine {
    public SolarGeneratorBlock() {
        super(Materials.cast_iron);
    }

    @Override
    public TileEntity createNewTileEntity (World worldIn, int meta) {
        return new SolarGeneratorTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType (IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
