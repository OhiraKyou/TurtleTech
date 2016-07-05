package ohirakyou.turtletech.client.render.util;

public class Size3D {
    /** North-south dimension (z). */
    public float length = 1f;

    /** East-west dimension (x). */
    public float width = 1f;

    /** Up-down dimension (y). */
    public float height = 1f;


    public Size3D() {}

    public Size3D(float diameter) { this(diameter, diameter, diameter); }

    public Size3D(float length, float width, float height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public Size3D getHalfSize() {
        return this.divideBy(2);
    }


    public float getX() { return width; }
    public float getY() { return height; }
    public float getZ() { return length; }



    /**
     * Returns a copy of this size, compressed by a divisor.
     *
     * @param divisor  the divisor applied to all dimensions
     * @return a copy of this, compressed by the divisor
     */
    public final Size3D divideBy(float divisor) {
        return new Size3D(this.length / divisor, this.width / divisor, this.height / divisor);
    }

    /**
     * Returns a copy of this size, expanded by a multiplier.
     *
     * @param multiplier  the multiplier applied to all dimensions
     * @return a copy of this, expanded by the multiplier
     */
    public final Size3D multiplyBy(float multiplier) {
        return new Size3D(this.length * multiplier, this.width * multiplier, this.height * multiplier);
    }

    /**
     * Increases each dimension by amount.
     *
     * Radius will be radius + amount / 2, as the amount is distributed evenly between opposite sides.
     *
     * @param amount
     * @return a copy of this, with each dimension lengthened by the given amount
     */
    public final Size3D grow(float amount) {
        return new Size3D(this.length + amount, this.width + amount, this.height + amount);
    }

    public final Size3D growRadially(float amount) {
        return grow(amount * 2);
    }

    public final Size3D shrink(float amount) {
        return grow(amount * -1);
    }

    public final Size3D shrinkRadially(float amount) {
        return growRadially(amount * -1);
    }



    public static Size3D fromXYZ(float x, float y, float z) {
        return new Size3D(z, x, y);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }

        if (!Size3D.class.isAssignableFrom(obj.getClass())) { return false; }

        final Size3D other = (Size3D) obj;
        if (length != other.length || width != other.width || height != other.height) {
            return false;
        }

        return true;
    }
}
