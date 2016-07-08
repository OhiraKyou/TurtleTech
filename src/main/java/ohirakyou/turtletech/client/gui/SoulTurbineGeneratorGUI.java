package ohirakyou.turtletech.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.SlotItemHandler;
import ohirakyou.turtletech.client.data.DataResources;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorContainer;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorTileEntity;

import java.util.List;

public class SoulTurbineGeneratorGUI extends GUIWithInventory {

    public static final ResourceLocation GENERATOR_BACKGROUND = new ResourceLocation(DataResources.SOUL_TURBINE_GENERATOR_BG);

    public IInventory generatorInventory;


    public SoulTurbineGeneratorGUI(InventoryPlayer playerInventory, SoulTurbineGeneratorTileEntity te) {
        super(playerInventory, new SoulTurbineGeneratorContainer(playerInventory, te));
    }

    @Override
    public void initGui() {
        super.initGui();


    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int generatorGuiHeight = 55;

        // Generator GUI
        mc.getTextureManager().bindTexture(GENERATOR_BACKGROUND);

        drawTexturedModalRect(
                fullGuiX, fullGuiY,
                0, 0,
                fullGuiWidth, generatorGuiHeight
        );

    }

}
