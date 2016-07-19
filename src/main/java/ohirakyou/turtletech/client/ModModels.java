package ohirakyou.turtletech.client;

import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.model.ModelLoader;
import ohirakyou.turtletech.common.block.BlockModSlab;
import ohirakyou.turtletech.common.block.ModBlocks;

public class ModModels {
    public static void init() {
        //todo automate ignoring of slab dummy variants instead of entering them manually
        // Ignore dummy variant in slabs
        ModelLoader.setCustomStateMapper(ModBlocks.cast_iron_slab, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY).build());
        ModelLoader.setCustomStateMapper(ModBlocks.cast_iron_slab_double, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY).build());
    }
}
