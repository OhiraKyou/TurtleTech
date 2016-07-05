package ohirakyou.turtletech.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChatUtils {
    public void printMessage(String message) {
        TextComponentString text = new TextComponentString(message);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(text);
    }

    @SideOnly(Side.CLIENT)
    public static void sendSpamlessMessage (int messageID, ITextComponent message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(message, messageID);
    }

}
