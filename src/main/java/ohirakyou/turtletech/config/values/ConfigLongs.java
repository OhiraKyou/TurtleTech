package ohirakyou.turtletech.config.values;

import ohirakyou.turtletech.config.ConfigCategories;

public enum ConfigLongs {
    ENERGY_USE_PRECISION_LASER_TURRET(ConfigCategories.ENERGY, "precisionLaserTurretShot", 250L,
            "Energy per laser shot", true),
    ENERGY_TRICKLE_SOUL_TURBINE_GENERATOR(ConfigCategories.ENERGY, "soulTurbineGeneratorTrickle", 1L,
            "Energy per tick generated while on soul sand in the nether", true)
    ;

    public final String key;
    public final ConfigCategories category;
    public final long defaultValue;
    public final String description;
    public final boolean isServerValue;

    public long value;

    ConfigLongs(ConfigCategories category, String key, long defaultValue, String description) {
        this(category, key, defaultValue, description, false);
    }

    ConfigLongs(ConfigCategories category, String key, long defaultValue, String description, boolean isServerValue) {
        this.category = category;
        this.key = key;
        this.description = description;
        this.defaultValue = defaultValue;
        this.isServerValue = isServerValue;
    }
}
