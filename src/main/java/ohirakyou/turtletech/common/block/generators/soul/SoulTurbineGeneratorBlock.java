package ohirakyou.turtletech.common.block.generators.soul;


import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.block.BlockMachine;
import ohirakyou.turtletech.common.material.Materials;

import javax.annotation.Nullable;
import java.util.List;

public class SoulTurbineGeneratorBlock extends BlockMachine {

    public SoulTurbineGeneratorBlock() {
        super(Materials.cast_iron);
    }

    @Override
    public TileEntity createNewTileEntity (World worldIn, int meta) {
        return new SoulTurbineGeneratorTileEntity();
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        //If it will harvest, delay deletion of the block until after getDrops
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack tool) {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        final List<ItemStack> drops = super.getDrops(world, pos, state, fortune);
        SoulTurbineGeneratorTileEntity te = (SoulTurbineGeneratorTileEntity)world.getTileEntity(pos);

        drops.addAll(te.getDrops());

        return drops;
    }

    @Override
    public EnumBlockRenderType getRenderType (IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

}
