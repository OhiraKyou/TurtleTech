package ohirakyou.turtletech.util;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import ohirakyou.turtletech.config.ConfigBooleans;
import ohirakyou.turtletech.data.DataModInfo;

import java.util.List;

public abstract class GUIUtils {
    public static void setTooltip(List<String> tooltip, String keyBase) {
        if (ConfigBooleans.SHOW_TOOLTIPS.value) {

            // Primary tooltip (always visible)
            if (ConfigBooleans.SHOW_TOOLTIPS_PRIMARY.value) {

                String tooltipKey = keyBase + ".tooltip";
                String tooltipText = I18n.format(tooltipKey);

                if (!tooltipText.equals(tooltipKey)) {
                    tooltip.add(tooltipText);
                }

            }

            // Secondary tooltip (hidden behind key prompt)
            if (ConfigBooleans.SHOW_TOOLTIPS_HIDDEN.value) {

                String moreKey = keyBase + ".more";
                String moreText = I18n.format(moreKey);

                if (!moreText.equals(moreKey)) {
                    if (GuiScreen.isShiftKeyDown()) {
                        tooltip.add(moreText);
                    } else {
                        if (ConfigBooleans.SHOW_TOOLTIPS_HIDDEN_PROMPT.value) {
                            String morePrompt = localizeForMod("misc.morePrompt");
                            tooltip.add(morePrompt);
                        }
                    }
                }

            }

        }
    }

    /**
     * Localizes a string with this mod's prefix automatically applied.
     *
     * @param key  the value after ':' in modname:key
     * @return the localized string associated with this mod and the given key
     */
    public static String localizeForMod(String key) {
        return localize(DataModInfo.MOD_ID + ":" + key);
    }


    /**
     * Localizes a string without a prefix automatically applied.
     *
     * @param key  the entire key, including any mod prefixes
     * @return the localized string associated with the specified key
     */
    public static String localize(String key) {
        return I18n.format(key);
    }
}
