package ohirakyou.turtletech.client.render.util;

public class TexturedCubeMap {
    public TextureTile northTile;
    public TextureTile eastTile;
    public TextureTile southTile;
    public TextureTile westTile;
    public TextureTile upTile;
    public TextureTile downTile;

    public TexturedCubeMap() {}

    public TexturedCubeMap(TextureTile defaultTile) {
        this(defaultTile, defaultTile, defaultTile, defaultTile, defaultTile, defaultTile);
    }

    public TexturedCubeMap(TextureTile northTile, TextureTile eastTile, TextureTile southTile, TextureTile westTile,
                           TextureTile upTile, TextureTile downTile) {
        this.northTile = northTile;
        this.eastTile = eastTile;
        this.southTile = southTile;
        this.westTile = westTile;
        this.upTile = upTile;
        this.downTile = downTile;
    }

    public final TextureTile[] getTiles() {
        return new TextureTile[] {northTile, eastTile, southTile, westTile, upTile, downTile};
    }
}
