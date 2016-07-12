package ohirakyou.turtletech.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.data.DataModInfo;

import java.io.File;

public class ModConfig {
    private static ModConfig clientConfig;
    private static ModConfig serverConfig;

    private Configuration config;
    private boolean isServerConfig;

    public static void initClient(File configFile) {
        clientConfig = new ModConfig(configFile, false);
        MinecraftForge.EVENT_BUS.register(clientConfig);
    }

    public static void initServer(File configFile){
        serverConfig = new ModConfig(configFile, true);

        // Sync server values with the client
    }

    public ModConfig(File configFile, boolean isServerConfig) {
        this.isServerConfig = isServerConfig;
        this.config = new Configuration(configFile);
        config.load();

        loadValues();
    }

    private void loadValues(){
        for (ConfigBooleans b : ConfigBooleans.values()){
            b.value = config.get(b.category.key, b.key, b.defaultValue, b.description).getBoolean();
        }

        for (ConfigLongs l : ConfigLongs.values()){
            String longString = config.get(l.category.key, l.key, l.defaultValue, l.description).getString();

            TurtleTech.logger.info("Trying to load a long. String: " + longString +
                    ", after replace: " + longString.replace(".0", "") +
                    ", parsed: " + Long.parseLong(longString.replace(".0", "")));

            l.value = Long.parseLong(longString.replace(".0", ""));
        }

        // Save changes
        if (config.hasChanged()){config.save();}
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event){
        if (event.getModID().equalsIgnoreCase(DataModInfo.MOD_ID)){
            loadValues();
        }
    }
}
