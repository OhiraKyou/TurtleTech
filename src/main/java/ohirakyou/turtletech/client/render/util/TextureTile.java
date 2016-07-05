package ohirakyou.turtletech.client.render.util;

public class TextureTile {
    // U, but more descriptive
    public float rightStart = 0;
    public float rightEnd = 1;

    // V, but more descriptive
    public float downStart = 0;
    public float downEnd = 1;


    // Constructors
    public TextureTile() {}

    public TextureTile(TextureTile other) {
        this(other.rightStart, other.rightEnd, other.downStart, other.downEnd);
    }

    public TextureTile(float rightStart, float rightEnd, float downStart, float downEnd) {
        this.rightStart = rightStart;
        this.rightEnd = rightEnd;
        this.downStart = downStart;
        this.downEnd = downEnd;
    }


    // Corners
    public TexturePoint getTopLeft(){
        return new TexturePoint(rightStart, downStart);
    }

    public TexturePoint getTopRight(){
        return new TexturePoint(rightEnd, downStart);
    }

    public TexturePoint getBottomLeft(){
        return new TexturePoint(rightStart, downEnd);
    }

    public TexturePoint getBottomRight(){
        return new TexturePoint(rightEnd, downEnd);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }

        if (!TextureTile.class.isAssignableFrom(obj.getClass())) { return false; }

        final TextureTile other = (TextureTile) obj;
        if (rightStart != other.rightStart || rightEnd != other.rightEnd ||
                downStart != other.downStart || downEnd != other.downEnd) {
            return false;
        }

        return true;
    }
}
