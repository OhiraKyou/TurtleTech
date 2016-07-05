package ohirakyou.turtletech.interop;

import net.minecraftforge.fml.common.Optional;
import ohirakyou.turtletech.data.DataMods;

public class IntegrationSteamAdvantage extends ModIntegration {
    private static final String MOD_ID = DataMods.STEAM_ADVANTAGE;

    /** Passes the mod ID to the base {@link ModIntegration} */
    public IntegrationSteamAdvantage() { super(MOD_ID); }

    @Optional.Method(modid = MOD_ID)
    protected void integrate() {

        integrated = true;
    }
}
