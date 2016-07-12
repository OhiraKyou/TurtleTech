package ohirakyou.turtletech.common.block.turrets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.block.BlockMachine;
import ohirakyou.turtletech.common.material.Materials;
import ohirakyou.turtletech.common.material.MetalMaterialComplex;
import ohirakyou.turtletech.common.tileentity.TileEntityTurret;

abstract public class BlockTurret extends BlockMachine {

    public BlockTurret() {
        super(Materials.cast_iron);
    }

    public BlockTurret(MetalMaterialComplex metal) {
        super(metal);
    }

    @Override
    public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(w, pos, state, placer, stack);
        TileEntity te = w.getTileEntity(pos);
        if(placer instanceof EntityPlayer && te instanceof TileEntityTurret){
            ((TileEntityTurret)te).setOwner((EntityPlayer)placer);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
