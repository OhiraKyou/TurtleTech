package ohirakyou.turtletech.common.block.generators.soul;


import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.block.BlockMachine;
import ohirakyou.turtletech.data.DataGuiIDs;

import java.util.List;

public class SoulTurbineGeneratorBlock extends BlockMachine {

    public SoulTurbineGeneratorBlock() {
        super(Material.IRON);
    }

    @Override
    public TileEntity createNewTileEntity (World worldIn, int meta) {
        return new SoulTurbineGeneratorTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType (IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
    }

}