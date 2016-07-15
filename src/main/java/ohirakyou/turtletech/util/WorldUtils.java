package ohirakyou.turtletech.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class WorldUtils {
    public static Vec3d followRayToSolidBlock(World w, Vec3d origin, Vec3d dir, double maxRange){
        Vec3d max = origin.addVector(maxRange * dir.xCoord, maxRange * dir.yCoord, maxRange * dir.zCoord);
        RayTraceResult impact = w.rayTraceBlocks(origin, max, true, true, false);

        if(impact != null && impact.typeOfHit == RayTraceResult.Type.BLOCK){
            final Vec3d impactSite;
            if(impact.hitVec != null){
                impactSite = impact.hitVec;
            } else {
                BlockPos bp = impact.getBlockPos();
                impactSite = new Vec3d(bp.getX(), bp.getY(), bp.getZ());
            }
            return impactSite;
        } else {
            return max;
        }
    }

    public static List<Entity> getLivingEntitiesInRadius(World world, Vec3d origin, double radius) {
        return getEntitiesInRadius(EntityLivingBase.class, world, origin, radius);
    }

    public static <T extends Entity> List<T> getEntitiesInRadius(Class <? extends T > entityClass, World world, Vec3d origin, double radius) {
        return world.getEntitiesWithinAABB(entityClass, new AxisAlignedBB(
                origin.xCoord - radius, origin.yCoord - radius, origin.zCoord - radius,
                origin.xCoord + radius, origin.yCoord + radius, origin.zCoord + radius));
    }
}
