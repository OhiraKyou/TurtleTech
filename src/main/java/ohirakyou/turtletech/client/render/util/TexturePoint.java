package ohirakyou.turtletech.client.render.util;

public class TexturePoint {
    public float right = 0; // U, but more descriptive
    public float down = 0; // V, but more descriptive

    public TexturePoint(TexturePoint other) {
        this(other.right, other.down);
    }

    public TexturePoint(float right, float down) {
        this.right = right;
        this.down = down;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }

        if (!TexturePoint.class.isAssignableFrom(obj.getClass())) { return false; }

        final TexturePoint other = (TexturePoint) obj;
        if (right != other.right || down != other.down) {
            return false;
        }

        return true;
    }
}
