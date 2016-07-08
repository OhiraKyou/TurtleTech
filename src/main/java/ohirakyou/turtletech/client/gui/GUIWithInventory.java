package ohirakyou.turtletech.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.SlotItemHandler;
import ohirakyou.turtletech.client.data.DataResources;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorContainer;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorTileEntity;

import java.util.List;

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
        int playerInventoryY = fullGuiY + fullGuiHeight - playerInventoryHeight;

        mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);

        drawTexturedModalRect(
                fullGuiX, playerInventoryY,
                0, 0,
                fullGuiWidth, playerInventoryHeight
        );



        /*
        List<Slot> slotList = inventorySlots.inventorySlots;
        for (Slot slot : slotList) {
            if (slot instanceof SlotItemHandler) {

                SlotItemHandler slotItemHandler = (SlotItemHandler) slot;

                if (!slotItemHandler.getHasStack()) {
                    ItemStack stack = new ItemStack(ModBlocks.flower, 0, slotItemHandler.getSlotIndex());
                    int x = guiLeft + slotItemHandler.xDisplayPosition;
                    int y = guiTop + slotItemHandler.yDisplayPosition;
                    RenderHelper.enableGUIStandardItemLighting();
                    mc.getRenderItem().renderItemIntoGUI(stack, x, y);
                    RenderHelper.disableStandardItemLighting();
                    mc.fontRendererObj.drawStringWithShadow("0", x + 11, y + 9, 0xFF6666);
                }

            }
        }
        */

    }

}
