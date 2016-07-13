package ohirakyou.turtletech.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import ohirakyou.turtletech.client.gui.ModGUI;
import ohirakyou.turtletech.client.sound.ModSounds;
import ohirakyou.turtletech.common.block.ModBlocks;
import ohirakyou.turtletech.common.item.ItemGroups;
import ohirakyou.turtletech.common.tileentity.ModTileEntities;
import ohirakyou.turtletech.common.crafting.ModCrafting;
import ohirakyou.turtletech.common.item.ModItems;
import ohirakyou.turtletech.common.material.Materials;
import ohirakyou.turtletech.config.ModConfig;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ModConfig.init(event);
        ItemGroups.init();
        ModSounds.init();

        Materials.init();  // prerequisite for blocks and items

        ModBlocks.init(); // comes before items
        ModItems.init();
        ModTileEntities.init();
        //ModFluids.init();
        //ModVillagerTrades.init();
    }

    public void init(FMLInitializationEvent event) {
        ModCrafting.init();
        //ModEntities.init();
        ModGUI.init();
    }

    public void postInit(FMLPostInitializationEvent event) {}
}
