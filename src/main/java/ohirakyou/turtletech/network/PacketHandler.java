package ohirakyou.turtletech.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import ohirakyou.turtletech.data.DataModInfo;

public class PacketHandler {
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(DataModInfo.MOD_ID);

    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void sendToAllAround(IMessage message, TileEntity te, int range) {
        CHANNEL.sendToAllAround(message, new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(),
                te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), range));
    }

    public static void sendToAllAround(IMessage message, TileEntity te) {
        sendToAllAround(message, te, 64);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        CHANNEL.sendTo(message, player);
    }
}
