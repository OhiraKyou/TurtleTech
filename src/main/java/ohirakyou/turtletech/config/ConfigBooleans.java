package ohirakyou.turtletech.config;

public enum ConfigBooleans {
    SHOW_TOOLTIPS(ConfigCategories.GUI, "showTooltips", true, "Master visibility of all tooltips"),
    SHOW_TOOLTIPS_PRIMARY(ConfigCategories.GUI, "showPrimaryTooltips", true, "Visibility of tooltips that are otherwise always shown"),
    SHOW_TOOLTIPS_HIDDEN(ConfigCategories.GUI, "showHiddenTooltips", true, "Visibility of tooltips that are hidden behind a key prompt"),
    SHOW_TOOLTIPS_HIDDEN_PROMPT(ConfigCategories.GUI, "showHiddenTooltipPrompt", true, "Visibility of the key prompt for hidden tooltips")
    ;

    public final String key;
    public final ConfigCategories category;
    public final boolean defaultValue;
    public final String description;
    public final boolean isServerValue;

    public boolean value;


    ConfigBooleans(ConfigCategories category, String key, boolean defaultValue, String description) {
        this(category, key, defaultValue, description, false);
    }

    ConfigBooleans(ConfigCategories category, String key, boolean defaultValue, String description, boolean isServerValue) {
        this.category = category;
        this.key = key;
        this.description = description;
        this.defaultValue = defaultValue;
        this.isServerValue = isServerValue;
    }
}
