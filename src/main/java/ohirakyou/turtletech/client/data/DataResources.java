package ohirakyou.turtletech.client.data;

import ohirakyou.turtletech.data.DataModInfo;

//todo move TESR resource locations here
public class DataResources {
    public static final String PREFIX_MOD = DataModInfo.MOD_ID + ":";

    // GUI
    public static final String PREFIX_GUI = PREFIX_MOD + "textures/gui/";
    public static final String PREFIX_GUI_CONTAINER = PREFIX_GUI + "container/";

    public static final String PLAYER_INVENTORY_BG = PREFIX_GUI_CONTAINER + "player_inventory.png";
    public static final String SOUL_TURBINE_GENERATOR_BG = PREFIX_GUI_CONTAINER + "soul_turbine_generator.png";
}
