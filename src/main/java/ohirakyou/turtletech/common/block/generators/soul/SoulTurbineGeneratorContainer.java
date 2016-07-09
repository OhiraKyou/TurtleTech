package ohirakyou.turtletech.common.block.generators.soul;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class SoulTurbineGeneratorContainer extends Container {

    private final SoulTurbineGeneratorTileEntity generator;
    private final IItemHandler generatorInventory;


    public SoulTurbineGeneratorContainer(InventoryPlayer playerInventory, SoulTurbineGeneratorTileEntity te) {

        this.generator = te;
        this.generatorInventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);


        // Generator inventory slots
        int generatorInputSlotX = 48;
        int generatorInputSlotY = 21;
        int generatorOutputSlotX = 116;
        int generatorOutputSlotY = generatorInputSlotY;

        this.addSlotToContainer(new SoulTurbineGeneratorSlot(
                generatorInventory, 0, generatorInputSlotX, generatorInputSlotY));
        this.addSlotToContainer(new SoulTurbineGeneratorOutputSlot(
                playerInventory.player, generatorInventory, 1, generatorOutputSlotX, generatorOutputSlotY));


        // Player inventory slots
        int totalRows = 3;
        int totalColumns = 9;

        int playerGuiSlotBorder = 1;
        int hotbarTopPadding = 4;
        int slotSize = 16 + playerGuiSlotBorder * 2;

        int firstPlayerInventorySlotX = 8;
        int firstPlayerInventorySlotY = 56;

        int hotbarY = firstPlayerInventorySlotY + slotSize * totalRows + hotbarTopPadding;

        // Player inventory slots
        for (int row = 0; row < totalRows; ++row) {
            for (int column = 0; column < totalColumns; ++column) {
                this.addSlotToContainer(new Slot(
                        playerInventory,
                        column + row * totalColumns + totalColumns,  // index (plus a row for the hotbar)
                        firstPlayerInventorySlotX + column * slotSize,  // x
                        firstPlayerInventorySlotY + row * slotSize  // y
                ));
            }
        }

        // Hotbar slots
        for (int hotbarColumn = 0; hotbarColumn < totalColumns; ++hotbarColumn) {
            this.addSlotToContainer(new Slot(
                    playerInventory, hotbarColumn,
                    firstPlayerInventorySlotX + hotbarColumn * slotSize,  // x
                    hotbarY  // y
            ));
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(index);

        int generatorSlots = 2;
        int inputSlotIndex = 0;
        int outputSlotIndex = 1;
        int playerInventoryRows = 3;
        int playerInventoryColumns = 9;
        int playerInventorySlots = playerInventoryColumns * playerInventoryRows;

        int firstPlayerInventorySlotIndex = generatorSlots;
        int lastPlayerInventorySlotIndex = firstPlayerInventorySlotIndex + playerInventorySlots;

        int firstPlayerHotBarSlot = lastPlayerInventorySlotIndex + 1;
        int lastPlayerHotBarSlot = lastPlayerInventorySlotIndex + playerInventoryColumns;


        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemstack = slotStack.copy();


            if (index < generatorSlots) {
                // Is a generator slot; move to player inventory
                // Normally, this is done in reverse order, prioritizing the hotbar. But, that's annoying.
                if (!this.mergeItemStack(slotStack, firstPlayerInventorySlotIndex, lastPlayerHotBarSlot, false)) {
                    return null;
                }

                if (index == outputSlotIndex) {
                    slot.onSlotChange(slotStack, itemstack);
                }

            } else {
                // Is a player slot)

                if (generator.itemHasSoulEnergy(slotStack)) {
                    // Is fuel; move to input
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return null;
                    }
                } else if (index >= generatorSlots && index < firstPlayerHotBarSlot) {
                    // Is player inventory slot but isn't fuel; move to player hotbar
                    if (!this.mergeItemStack(slotStack, firstPlayerHotBarSlot, lastPlayerHotBarSlot, false)) {
                        return null;
                    }
                } else if (index >= firstPlayerHotBarSlot && index < lastPlayerHotBarSlot) {
                    // Is player hotbar slot but isn't fuel; move to player inventory
                    if (!this.mergeItemStack(slotStack, firstPlayerInventorySlotIndex, lastPlayerInventorySlotIndex, false)) {
                        return null;
                    }
                }
            }


            if (slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(playerIn, slotStack);
        }

        return itemstack;
    }
}
