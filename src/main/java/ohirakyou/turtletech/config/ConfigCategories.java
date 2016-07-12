package ohirakyou.turtletech.config;

public enum ConfigCategories {
    BLOCKS("blocks"),
    ITEMS("items"),
    CRAFTING("crafting"),
    ENERGY("energy"),
    GUI("gui"),
    ;

    public final String key;

    ConfigCategories(String name) {
        this.key = name;
    }
}
