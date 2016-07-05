package ohirakyou.turtletech.interop;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.data.DataModInfo;

/**
 * Base class for unique mod integrations.
 */
abstract public class ModIntegration {
    protected String modID;
    public String getModID() { return modID; }

    private String modName;
    public String getModName() { return modName; }

    protected boolean integrated;

    public boolean isIntegrated() { return integrated; }

    /**
     * Sole constructor.
     * <p>
     * Given a mod ID, finds and calls the mod's {@link #integrate unique integration}.
     * This should be invoked by subclass constructors.
     *
     * @param newModID  the mod ID to search for in the list of loaded mods, not null
     */
    public ModIntegration(String newModID) {
        modID = newModID;

        // Get the mod name for logging and display
        for (ModContainer mod : Loader.instance().getModList()) {
            if (mod.getModId().equals(modID)) {
                modName = mod.getName();

                // Log mod detection success
                TurtleTech.logger.info(DataModInfo.MOD_NAME + " has detected mod '" + modName + "'");

                // Integrate mod since we now know it is installed without a specific check
                integrate();

                // Log mod integration success
                TurtleTech.logger.info(DataModInfo.MOD_NAME + " has integrated mod '" + modName + "'");

                break;
            }
        }

        if (modName == null || modName.isEmpty()) {
            TurtleTech.logger.info(DataModInfo.MOD_NAME + " could not find mod ID '" + modID + "' for integration.");
        }
    }

    /**
     * Initializes interoperability features with installed mod.
     * <p>
     * The implementation method will be safely stripped out if the mod is not loaded.
     */
    abstract protected void integrate();
}
