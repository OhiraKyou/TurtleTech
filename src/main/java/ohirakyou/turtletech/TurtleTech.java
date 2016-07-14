package ohirakyou.turtletech;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import ohirakyou.turtletech.common.CommonProxy;
import org.apache.logging.log4j.Logger;

import ohirakyou.turtletech.data.DataModInfo;
import ohirakyou.turtletech.data.DataMods;
import ohirakyou.turtletech.interop.IntegrationElectricAdvantage;
import ohirakyou.turtletech.interop.IntegrationSteamAdvantage;
import ohirakyou.turtletech.interop.ModIntegration;


@Mod(
    modid = DataModInfo.MOD_ID,
    name= DataModInfo.MOD_NAME,
    version = DataModInfo.MOD_VERSION,

    guiFactory = DataModInfo.GUI_FACTORY,

    dependencies =
        "required-after:" + DataMods.FORGE + "@["+ DataModInfo.FORGE_VERSION + ",)" +
        ";required-after:" + DataMods.BASE_METALS +
        ";required-after:" + DataMods.TESLA +
        ";after:" + DataMods.ELECTRIC_ADVANTAGE +
        ";after:" + DataMods.STEAM_ADVANTAGE
)
public class TurtleTech {
    @Mod.Instance(DataModInfo.MOD_ID)
    public static TurtleTech instance;

    @SidedProxy(
            clientSide = "ohirakyou.turtletech.client.ClientProxy",
            serverSide = "ohirakyou.turtletech.server.ServerProxy")
    public static CommonProxy proxy;
    public static Logger logger;

    //public static TurtleTechCreativeTab creativeTab;
    public static ModIntegration[] modIntegrations;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (instance == null) { instance = this; }
        logger = event.getModLog();

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

        integrateMods();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }


    private void integrateMods() {
        modIntegrations = new ModIntegration[] {
                new IntegrationElectricAdvantage(),
                new IntegrationSteamAdvantage()
        };
    }
}
