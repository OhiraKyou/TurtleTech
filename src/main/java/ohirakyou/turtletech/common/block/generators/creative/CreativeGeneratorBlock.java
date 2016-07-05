package ohirakyou.turtletech.common.block.generators.creative;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class CreativeGeneratorBlock extends BlockContainer {

    public CreativeGeneratorBlock() {
        super(Material.IRON);
    }

    @Override
    public TileEntity createNewTileEntity (World worldIn, int meta) {
        return new CreativeGeneratorTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType (IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}