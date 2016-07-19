package ohirakyou.turtletech.common.block;

import cyano.basemetals.blocks.BlockMetalPlate;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrate extends BlockMetalPlate {
    public BlockGrate(MetalMaterial metal) {super(metal);}

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {return BlockRenderLayer.CUTOUT;}
}
