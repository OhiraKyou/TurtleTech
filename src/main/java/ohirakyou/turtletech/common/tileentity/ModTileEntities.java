package ohirakyou.turtletech.common.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;
import ohirakyou.turtletech.common.block.foundry.FoundryTileEntity;
import ohirakyou.turtletech.common.block.generators.creative.CreativeGeneratorBlock;
import ohirakyou.turtletech.common.block.generators.creative.CreativeGeneratorTileEntity;
import ohirakyou.turtletech.common.block.generators.reflex.RedstoneReflexGeneratorTileEntity;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorTileEntity;
import ohirakyou.turtletech.common.block.turrets.TurretExtenderTileEntity;
import ohirakyou.turtletech.common.block.turrets.precisionlaserturret.PrecisionLaserTileEntity;
import ohirakyou.turtletech.data.DataModInfo;

public abstract class ModTileEntities {

    private static boolean initDone = false;

    public static void init() {
        if (initDone) return;

        // Turrets
        registerTileEntity(PrecisionLaserTileEntity.class);
        registerTileEntity(TurretExtenderTileEntity.class);

        // Generators
        registerTileEntity(CreativeGeneratorTileEntity.class);
        registerTileEntity(RedstoneReflexGeneratorTileEntity.class);
        registerTileEntity(SoulTurbineGeneratorTileEntity.class);

        // Machines
        registerTileEntity(FoundryTileEntity.class);

        initDone = true;
    }

    private static void registerTileEntity(Class tileEntityClass){
        String name = tileEntityClass.getSimpleName();
        if(name.endsWith("TileEntity")){
            name = name.substring(0, name.lastIndexOf("TileEntity"));
        }
        GameRegistry.registerTileEntity(tileEntityClass, DataModInfo.MOD_ID + ":" + toUnderscoreStyle(name));
    }

    private static String toUnderscoreStyle(String camelCase){
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(camelCase.charAt(0)));
        for(int i = 1; i < camelCase.length(); i++){
            char c = camelCase.charAt(i);
            if(Character.isUpperCase(c)){
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
