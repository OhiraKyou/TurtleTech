package ohirakyou.turtletech.client;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ohirakyou.turtletech.client.render.ModRenderers;
import ohirakyou.turtletech.client.sound.ModSounds;
import ohirakyou.turtletech.common.block.ModBlocks;
import ohirakyou.turtletech.common.CommonProxy;
import ohirakyou.turtletech.common.item.ModItems;
import ohirakyou.turtletech.config.ModConfig;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ModConfig.initClient(event.getSuggestedConfigurationFile());

        ModRenderers.init();
        ModSounds.init();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ModItems.registerItemRenders();
        ModBlocks.registerItemRenders();
    }

    /*
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }*/
}
