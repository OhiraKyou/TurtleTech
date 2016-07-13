package ohirakyou.turtletech.config;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConfigSync implements IMessage, IMessageHandler<PacketConfigSync, IMessage> {

    @Override
    public void toBytes(ByteBuf data) {
        // Write server booleans
        for (ConfigBooleans c : ConfigBooleans.values()){
            if (c.isServerValue) {data.writeBoolean(c.value);}
        }

        // Write server longs
        for (ConfigLongs c : ConfigLongs.values()){
            if (c.isServerValue) {data.writeLong(c.value);}
        }
    }

    @Override
    public void fromBytes(ByteBuf data) {
        // Read server booleans
        for (ConfigBooleans c : ConfigBooleans.values()){
            if (c.isServerValue) {c.value = data.readBoolean();}
        }

        // Read server longs
        for (ConfigLongs c : ConfigLongs.values()){
            if (c.isServerValue) {c.value = data.readLong();}
        }
    }

    @Override
    public IMessage onMessage(PacketConfigSync message, MessageContext context) {
        return null;
    }

}
