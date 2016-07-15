package ohirakyou.turtletech.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Contains custom math utility functions
 */
public abstract class MathUtils {
    public static final float RADIANS_TO_DEGREES = (float)(180 / Math.PI);
    public static final float DEGREES_TO_RADIANS = (float)(Math.PI / 180);

    /** A wrapper for using min and max at the same time. */
    public static float clamp(float val, float min, float max) {return Math.max(min, Math.min(max, val));}

    /** A wrapper for using min and max at the same time. */
    public static int clamp(int val, int min, int max) {return Math.max(min, Math.min(max, val));}

    /** Clamps value between negative and positive minMax. */
    public static float clampSymmetrical(float value, float minMax) {return Math.max(-minMax, Math.min(minMax, value));}

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
        // Algorithm from http://stackoverflow.com/a/5852628/599884

        if (min > max) {
            // Swap min and max
            float temp = min;
            min = max;
            max = temp;
        }

        float range = max - min;
        if (range == 0) {return max;}

        return (float) (value - range * Math.floor((value - min) / range));
    }

    public static double wrapBetween(double value, double min, double max) {
        // Algorithm from http://stackoverflow.com/a/5852628/599884

        if (min > max) {
            // Swap min and max
            double temp = min;
            min = max;
            max = temp;
        }

        double range = max - min;
        if (range == 0) {return max;}

        return (value - range * Math.floor((value - min) / range));
    }

    public static float wrapAngle180(float value){return wrapBetween(value, -180f, 180f);}

    public static float wrapAngle360(float value){return wrapBetween(value, 0, 360f);}

    public static double wrapAngle360(double value){return wrapBetween(value, 0, 360f);}

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

    public static Vec3d calculateDirection(float pitch, float yaw) {
        double pitchRadians = Math.toRadians(pitch);
        double yawRadians = Math.toRadians(yaw);

        double sinPitch = Math.sin(pitchRadians);
        double cosPitch = Math.cos(pitchRadians);
        double sinYaw = Math.sin(yawRadians);
        double cosYaw = Math.cos(yawRadians);

        //return new Vec3d(-cosPitch * sinYaw, sinPitch, -cosPitch * cosYaw);
        //return new Vec3d(-cosYaw * sinPitch, -cosPitch, sinYaw * sinPitch);
        return new Vec3d(sinYaw * cosPitch, sinPitch, -cosYaw * cosPitch).normalize();
        //return new Vec3d(Math.sin(pitch) * Math.sin(yaw), Math.cos(pitch), Math.sin(pitch) * Math.sin(yaw));

        /*
        return new Vec3d(
                Math.sin(yaw),
                Math.sin(pitch),
                Math.cos(DEGREES_TO_RADIANS * yaw)
        ).normalize();*/
    }

    public static Vec3d calculateDirection(Vec3d origin, Entity target) {
        Vec3d entityLocalCenter = new Vec3d(0, 0.5 * target.height, 0);
        Vec3d targetPoint = target.getPositionVector().add(entityLocalCenter);

        return targetPoint.subtract(origin).normalize();
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

    public static double distance(double x1,double y1,double z1,double x2,double y2,double z2){
        double dx = x2-x1;
        double dy = y2-y1;
        double dz = z2-z1;
        return MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
    }
}
