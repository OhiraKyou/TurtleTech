package ohirakyou.turtletech.util;

public class SimpleRotation {
    public float pitch;
    public float roll;
    public float yaw;

    public SimpleRotation(float pitch, float roll, float yaw) {
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }

        if (!SimpleRotation.class.isAssignableFrom(obj.getClass())) { return false; }

        final SimpleRotation other = (SimpleRotation) obj;
        if (pitch != other.pitch || roll != other.roll || yaw != other.yaw) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "[pitch: " + pitch + ", roll: " + roll + ", yaw: " + yaw + "]";
    }
}
