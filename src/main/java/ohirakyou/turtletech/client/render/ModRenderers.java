package ohirakyou.turtletech.client.render;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import ohirakyou.turtletech.common.block.ModBlocks;
import ohirakyou.turtletech.common.block.turrets.precisionlaserturret.PrecisionLaserTileEntity;
import ohirakyou.turtletech.data.DataModInfo;

public final class ModRenderers {

    public static void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(PrecisionLaserTileEntity.class, new PrecisionLaserTurretRenderer());

//        OBJLoader.INSTANCE.addDomain(DataModInfo.MOD_ID.toLowerCase());
//        Item precisionLaserTurretItem = Item.getItemFromBlock(ModBlocks.precision_laser_turret);
//        ModelLoader.setCustomModelResourceLocation(precisionLaserTurretItem, 0,
//                new ModelResourceLocation(DataModInfo.MOD_ID.toLowerCase()+
//                ":precision_laser_turret_head.obj", "inventory"));
    }

}
