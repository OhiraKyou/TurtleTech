package ohirakyou.turtletech.server;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ohirakyou.turtletech.common.CommonProxy;
import ohirakyou.turtletech.config.ModConfig;

public class ServerProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ModConfig.initServer(event.getSuggestedConfigurationFile());
    }

    /*@Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }*/

    /*@Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }*/
}
