package ohirakyou.turtletech.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Contains custom math utility functions
 */
public class MathUtils {
    /** A wrapper for using min and max at the same time. */
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    /** A wrapper for using min and max at the same time. */
    public static float clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    /** Clamps value between negative and positive minMax. */
    public static float clampSymmetrical(float value, float minMax) {
        return Math.max(-minMax, Math.min(minMax, value));
    }

    /** CLamps value between 0 and 1 */
    public static float clamp01 (float value) {
        return clamp(value, 0, 1f);
    }

    /** Returns 1 if value is positive or -1 if value is negative. */
    public static float sign(float value) {
        if (value == 0) { return 0; }
        return value / Math.abs(value);
    }

    public static float wrapBetween(float value, float min, float max) {
        // Expanded for readability: min + (max - min) % (max - min)

        // Tear open the fabric of mathematical space-time to create a pocket dimension in the number line
        // where only the values between min and max exist.
        final float range = max - min;

        // Depending on where our value was when the rupture occurred, it may now be either safely within
        // the new dimension or dangling from the left or right side of the dimension by an existential thread.
        // Find its coordinate relative to min.
        final float offsetFromMin = value - min;

        // Wind our value's existential thread around the number line pocket dimension until the end of
        // its thread ends neatly within the dimension.
        final float wrappedOffset = offsetFromMin % range;

        // Transplant the pocket dimension, with our value now offset, into the number line proper
        return min + wrappedOffset;
    }

    public static double wrapBetween(double value, double min, double max) {
        final double range = max - min;
        final double offsetFromMin = value - min;
        final double wrappedOffset = offsetFromMin % range;

        return min + wrappedOffset;
    }

    public static float wrapAngle180(float value){
        return wrapBetween(value, -180f, 180f);
    }

    public static float wrapAngle360(float value){
        return wrapBetween(value, 0, 360f);
    }

    public static double wrapAngle360(double value){
        return wrapBetween(value, 0, 360f);
    }

    /**
     * Linearly interpolates between two otherwise disparate values.
     * <p>
     * Because ticks are too infrequent to use for smooth rendering, you can simply save a canonical
     * current and previous (or current and next) value in a ticking entity and use interpolation to
     * smoothly transition from one to the other on each render tick.
     * <p>
     * It is also possible to cast from a boolean and use it as a switch to toggle between start and end.
     *
     * @param start  usually represents a previous value
     * @param end  usually represents a new value that must be smoothed due to its distance from start
     * @param mapValue  a value between 0 and 1 representing a point between start and end
     * @return the point between start and end represented by mapValue
     */
    public static float lerp (float start, float end, float mapValue) {
        return (end - start) * mapValue + start;
    }


    public static float interpolateAroundThreshold(float value, float threshold) {
        return interpolateAroundThreshold(value, threshold, 0, 1);
    }

    /**
     * Gets the y at value distance along a curve between 0 and 1, with a hill at threshold.
     * <p>
     * Great for animating things that should quickly inflate and then slowly deflate.
     *
     * @param value  the distance along the curve (x) at which to look up the interpolated value (y)
     * @param threshold  the location of the curve's hill, between 0 and 1
     * @param exponent  applied to the final value before smoothing
     * @param smoothing  multiplied by and then added to the final value, to smooth the curve
     * @return
     */
    public static float interpolateAroundThreshold(float value, float threshold, float smoothing, float exponent) {
        if (value > threshold) {
            value =  1 - (value - threshold) / (1f - threshold);
        } else {
            value = value / threshold;
        }

        value = (float)Math.pow(value, exponent);

        return clamp01(value + value * smoothing);
    }

    public static float randomInRange(float min, float max) {
        return (float)Math.random() * (max-min) + min;
    }

    public static double randomInRange(double min, double max) {
        return Math.random() * (max-min) + min;
    }

    public static BlockPos getUpPos(BlockPos start) {
        return new BlockPos(start.getX(), start.getY() + 1, start.getZ());
    }

    public static BlockPos getDownPos(BlockPos start) {
        return new BlockPos(start.getX(), start.getY() - 1, start.getZ());
    }

    public static Vec3d followRayToSolidBlock(World w, Vec3d origin, Vec3d dir, double maxRange){
        Vec3d max = origin.add(dir).addVector(maxRange * dir.xCoord, maxRange * dir.yCoord, maxRange * dir.zCoord);
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

    public static float getYawFromFacing(EnumFacing facing) {
        return facing.getOpposite().getHorizontalAngle();
    }

    /**
     * Makes a yaw that is relative to one forward facing relative to another instead.
     * <p>
     * Sometimes, what two pieces of code consider the "forward" direction don't align.
     * In those cases, proper comparison requires the transformation from one relative facing to another.
     * <p>
     * This function adds the undesired forward facing's deviation from the expected yaw to the given yaw,
     * creating the expected yaw relative to the desired forward facing.
     *
     * @param expectedForward
     * @param foreignForward
     * @param degreesRelativeToForeign
     * @return
     */
    public static float changeYawRelativity(
            EnumFacing expectedForward, EnumFacing foreignForward, float degreesRelativeToForeign) {

        float deviationFromForward = getYawFromFacing(foreignForward) - getYawFromFacing(expectedForward);
        float degreesRelativeToForward = deviationFromForward + degreesRelativeToForeign;

        return degreesRelativeToForward;
    }
}
