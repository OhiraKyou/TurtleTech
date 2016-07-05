package ohirakyou.turtletech.common.block.foundry;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.block.BlockMachine;
import ohirakyou.turtletech.common.material.Materials;

import javax.annotation.Nullable;
import java.util.List;

public class FoundryBlock extends BlockMachine implements IMetalObject {
    public FoundryBlock() {
        super(Material.ROCK);

        setHardness(5f);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);

        setLightLevel(1f);
    }

    public boolean onBlockActivated(
            World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
            @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        if(player.isSneaking()) { return false; }

        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof FoundryTileEntity) {
                FoundryTileEntity fte = (FoundryTileEntity)tileentity;
                fte.setActiveState(!state.getValue(ACTIVE));
            }
        }

        return true;
    }

    @Override
    public FoundryTileEntity createNewTileEntity(World world, int meta) {
        return new FoundryTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    /*
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        return 1f;
    }
    */

    @Override
    public MetalMaterial getMetalMaterial() { return (MetalMaterial)Materials.cast_iron; }


    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
    }
}