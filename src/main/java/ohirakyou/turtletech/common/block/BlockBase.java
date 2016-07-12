package ohirakyou.turtletech.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ohirakyou.turtletech.common.item.ItemGroups;
import ohirakyou.turtletech.common.material.MetalMaterialComplex;
import ohirakyou.turtletech.util.GUIUtils;

import java.util.List;

public class BlockBase extends Block {
    public BlockBase(MetalMaterialComplex metal) {
        this(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(metal.hardness);
        setResistance(metal.getBlastResistance());
        setHarvestLevel("pickaxe", 0);
    }

    public BlockBase(Material materialIn) {
        super(materialIn);
        setCreativeTab(ItemGroups.tab_blocks);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        GUIUtils.setTooltip(tooltip, getUnlocalizedName());
    }
}
