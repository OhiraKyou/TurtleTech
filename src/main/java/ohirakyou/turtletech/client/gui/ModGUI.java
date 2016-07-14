package ohirakyou.turtletech.client.gui;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.common.block.ModBlocks;
import ohirakyou.turtletech.data.DataGuiIDs;

public abstract class ModGUI {

    //private static final Map<Integer,ITileEntityGUI> guiTable = new HashMap<Integer,ITileEntityGUI>();

    public static void init(){
        ModBlocks.soul_turbine_generator.guiId = DataGuiIDs.SOUL_TURBINE_GENERATOR;

        NetworkRegistry.INSTANCE.registerGuiHandler(TurtleTech.instance, new GUIHandler());
    }

}
