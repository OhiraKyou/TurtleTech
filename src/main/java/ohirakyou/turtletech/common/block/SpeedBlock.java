package ohirakyou.turtletech.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.common.material.MetalMaterialComplex;

import java.util.List;

public class SpeedBlock extends BlockBase {
    private float motionMultiplier;

    public SpeedBlock(MetalMaterialComplex metal, float motionMultiplier) {
        super(Material.IRON);

        setHardness(metal.hardness);
        setResistance(metal.getBlastResistance());

        setSoundType(SoundType.METAL);

        this.motionMultiplier = motionMultiplier;
    }

    public SpeedBlock(Material material, float motionMultiplier) {
        super(material);

        this.motionMultiplier = motionMultiplier;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entity) {
        entity.motionX *= motionMultiplier;
        entity.motionZ *= motionMultiplier;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
    }
}
