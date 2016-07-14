package ohirakyou.turtletech.config.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import ohirakyou.turtletech.config.ConfigCategories;
import ohirakyou.turtletech.config.ModConfig;
import ohirakyou.turtletech.data.DataModInfo;
import ohirakyou.turtletech.util.GUIUtils;

import java.util.ArrayList;
import java.util.List;

public class ModConfigGUI extends GuiConfig {

    public ModConfigGUI(GuiScreen parent) {
        super(parent, getConfigElements(), DataModInfo.MOD_ID, false, false, GUIUtils.localizeForMod("config.main"));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> categoryList = new ArrayList<>();
        String prefix = GUIUtils.getModPrefix() + "config.";

        for (ConfigCategories category : ConfigCategories.values()) {
            ConfigCategory configCategory = ModConfig.getConfig().getCategory(category.key);

            if (configCategory.values().size() > 0) {
                categoryList.add(new ConfigElement(configCategory.setLanguageKey(prefix + category.key)));
            }
        }

        return categoryList;
    }

}
