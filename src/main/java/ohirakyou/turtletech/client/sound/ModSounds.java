package ohirakyou.turtletech.client.sound;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ohirakyou.turtletech.data.DataModInfo;
import ohirakyou.turtletech.util.MathUtils;

import java.util.List;

public final class ModSounds {
    private static SoundEvent precisionLaserShot;

    public static void init () {
        precisionLaserShot = registerSound("precisionLaserShot");
    }

    private static SoundEvent registerSound(String soundName){
        ResourceLocation location = new ResourceLocation(DataModInfo.MOD_ID, soundName);
        SoundEvent event = new SoundEvent(location);
        GameRegistry.register(event, location);
        return event;
    }


    private static void playSound(World world, BlockPos pos, SoundEvent soundEvent, float volume, float pitch) {
        playSound(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), soundEvent, volume, pitch);
    }

    private static void playSound(World world, double x, double y, double z, SoundEvent soundEvent, float volume, float pitch) {
        if(world.isRemote) return;
        final double range = 16;

        List<EntityPlayerMP> players = world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(
                x - range, y - range, z - range,
                x + range, y + range, z + range));

        for(EntityPlayerMP player : players){
            player.connection.sendPacket(new SPacketCustomSound(soundEvent.getRegistryName().toString(), SoundCategory.BLOCKS,
                    x, y, z, volume, pitch));
        }
    }

    public static void playPrecisionLaserShot(World world, BlockPos pos) {
        playSound(world, pos, precisionLaserShot, 0.4f, MathUtils.randomInRange(0.9f, 1.1f));
    }

    //public static void stopSound(World worldObj, BlockPos pos) {}
}
