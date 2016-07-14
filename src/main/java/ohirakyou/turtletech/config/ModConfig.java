package ohirakyou.turtletech.config;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import ohirakyou.turtletech.config.values.ConfigBooleans;
import ohirakyou.turtletech.config.values.ConfigLongs;
import ohirakyou.turtletech.data.DataModInfo;
import ohirakyou.turtletech.network.PacketHandler;

import java.io.File;

public class ModConfig {
    // STATIC
    private static ModConfig instance;

    public static void init(FMLPreInitializationEvent event) {
        PacketHandler.CHANNEL.registerMessage(PacketConfigSync.class, PacketConfigSync.class, PacketHandler.nextID(), Side.CLIENT);
        instance = new ModConfig(event.getSuggestedConfigurationFile(), event.getSide().isServer());
    }

    public static Configuration getConfig() {
        return instance.config;
    }

    public static File getConfigFile() {
        return getConfig().getConfigFile();
    }


    // INSTANCE
    private Configuration config;
    private boolean isServerConfig;

    public ModConfig(File configFile, boolean isServerConfig) {
        this.isServerConfig = isServerConfig;
        MinecraftForge.EVENT_BUS.register(this);

        // Config is automatically loaded once in its constructor
        this.config = new Configuration(configFile);
        loadValues();
    }

    private void loadValues(){loadValues(false);}

    private void loadValues(boolean reloadConfigFile){
        if (reloadConfigFile) {config.load();}

        for (ConfigBooleans c : ConfigBooleans.values()){
            if (isCorrectSide(c.isServerValue)) {
                c.value = config.get(c.category.key, c.key, c.defaultValue, c.description).getBoolean();
            }
        }

        for (ConfigLongs c : ConfigLongs.values()){
            if (isCorrectSide(c.isServerValue)) {
                // This has to be saved as a string; longs get .0 appended, which, without replacing, causes a crash
                c.value = config.get(c.category.key, c.key, String.valueOf(c.defaultValue), c.description).getLong();
            }
        }

        // Save changes
        if (config.hasChanged()){config.save();}
    }

    private boolean isCorrectSide(boolean isServerValue) {
        return !isServerConfig || isServerValue;
    }

    @SubscribeEvent
    public void onPlayerLoggon(PlayerEvent.PlayerLoggedInEvent event) {
        PacketHandler.CHANNEL.sendTo(new PacketConfigSync(), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if (event.getModID().equalsIgnoreCase(DataModInfo.MOD_ID)){
            loadValues();
        }
    }
}
