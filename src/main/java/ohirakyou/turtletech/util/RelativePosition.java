package ohirakyou.turtletech.util;

public class RelativePosition {
    /** On a compass, north */
    public float forward;
    /** On a compass, east */
    public float rightward;
    public float upward;

    public RelativePosition() {}

    public RelativePosition(float forward, float rightward, float upward) {
        this.forward = forward;
        this.rightward = rightward;
        this.upward = upward;
    }

    public static RelativePosition fromXYZ(float x, float y, float z) {
        return new RelativePosition(z, x, y);
    }


    public float getX() { return rightward; }
    public float getY() { return upward; }
    public float getZ() { return forward; }

    public boolean isOrigin() {
        return forward == 0 && rightward == 0 && upward == 0;
    }

    public boolean isUp() {
        return forward == 0 && rightward == 0 && upward > 0;
    }

    public boolean isDown() {
        return forward == 0 && rightward == 0 && upward < 0;
    }

    public boolean isRight() {
        return forward == 0 && rightward > 0 && upward == 0;
    }

    public boolean isLeft() {
        return forward == 0 && rightward < 0 && upward == 0;
    }

    public boolean isForward() {
        return forward > 0 && rightward == 0 && upward == 0;
    }

    public boolean isBackward() {
        return forward < 0 && rightward == 0 && upward == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }

        if (!RelativePosition.class.isAssignableFrom(obj.getClass())) { return false; }

        final RelativePosition other = (RelativePosition) obj;
        if (forward != other.forward || rightward != other.rightward || upward != other.upward) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "[forward: " + forward + ", rightward: " + rightward + ", upward: " + upward + "]";
    }
}
