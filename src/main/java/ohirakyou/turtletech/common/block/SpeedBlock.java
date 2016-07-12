package ohirakyou.turtletech.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.material.MetalMaterialComplex;

public class SpeedBlock extends BlockBase {
    private float motionMultiplier;

    public SpeedBlock(MetalMaterialComplex metal, float motionMultiplier) {
        super(metal);
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
}
