package ohirakyou.turtletech.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import ohirakyou.turtletech.client.data.DataResources;

public abstract class GUIWithInventory extends GuiContainer {

    public static final ResourceLocation INVENTORY_BACKGROUND = new ResourceLocation(DataResources.PLAYER_INVENTORY_BG);


    protected final InventoryPlayer playerInventory;
    protected final Container container;

    private int playerGuiBorder = 7;

    protected int secondaryGuiHeight, playerInventoryHeight;
    protected int fullGuiWidth, fullGuiHeight;
    protected int fullGuiX, fullGuiY;


    public GUIWithInventory(InventoryPlayer playerInventory, Container container) {
        super(container);
        this.playerInventory = playerInventory;
        this.container = container;
    }

    @Override
    public void initGui() {
        super.initGui();

        secondaryGuiHeight = 55;
        playerInventoryHeight = 90;

        fullGuiWidth = 176;
        fullGuiHeight = secondaryGuiHeight + playerInventoryHeight - playerGuiBorder;  // overlap with border

        fullGuiX = (width - fullGuiWidth) / 2;
        fullGuiY = (height - fullGuiHeight) / 2;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Player inventory
        int playerInventoryY = fullGuiY + fullGuiHeight - playerInventoryHeight;  // docked to full GUI bottom

        mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);

        drawTexturedModalRect(
                fullGuiX, playerInventoryY,
                0, 0,
                fullGuiWidth, playerInventoryHeight
        );

    }

}
