package ohirakyou.turtletech.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import ohirakyou.turtletech.client.data.DataResources;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorContainer;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorTileEntity;

public class SoulTurbineGeneratorGUI extends GUIWithInventory {

    public static final ResourceLocation GENERATOR_BACKGROUND = new ResourceLocation(DataResources.SOUL_TURBINE_GENERATOR_BG);

    public SoulTurbineGeneratorTileEntity generator;


    public SoulTurbineGeneratorGUI(InventoryPlayer playerInventory, SoulTurbineGeneratorTileEntity te) {
        super(playerInventory, new SoulTurbineGeneratorContainer(playerInventory, te));
        generator = te;

        xSize = 176;
        ySize = 138;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int generatorGuiHeight = 55;

        // Background
        mc.getTextureManager().bindTexture(GENERATOR_BACKGROUND);
        drawTexturedModalRect(fullGuiX, fullGuiY, 0, 0, fullGuiWidth, generatorGuiHeight);

        // Progress arrow
        int progressArrowX = fullGuiX + 77;
        int progressArrowY = fullGuiY + 20;
        int progressArrowHeight = 17;
        int progressArrowWidth = (int)Math.ceil(24 * generator.getProgressLevel());

        drawTexturedModalRect(progressArrowX, progressArrowY, fullGuiWidth, 0, progressArrowWidth, progressArrowHeight);
    }

}
