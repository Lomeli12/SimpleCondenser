package net.lomeli.simplecondenser.inventory;

import java.util.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.FMLCommonHandler;

import net.lomeli.lomlib.util.ItemUtil;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.core.network.PacketSaveTablet;
import net.lomeli.simplecondenser.core.Proxy;
import net.lomeli.simplecondenser.inventory.slots.SlotTabletInput;
import net.lomeli.simplecondenser.inventory.slots.SlotTabletOutput;
import net.lomeli.simplecondenser.inventory.slots.SlotTome;
import net.lomeli.simplecondenser.item.ItemPortableTablet;
import net.lomeli.simplecondenser.lib.ItemLib;

import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.api.knowledge.TransmutationKnowledgeRegistryProxy;
import com.pahimar.ee3.inventory.ContainerEE;
import com.pahimar.ee3.inventory.element.IElementButtonHandler;
import com.pahimar.ee3.inventory.element.IElementSliderHandler;
import com.pahimar.ee3.inventory.element.IElementTextFieldHandler;
import com.pahimar.ee3.knowledge.TransmutationKnowledge;
import com.pahimar.ee3.reference.Comparators;
import com.pahimar.ee3.util.FilterUtils;

public class ContainerPortableTablet extends ContainerEE implements IElementTextFieldHandler, IElementSliderHandler, IElementButtonHandler {
    public final EntityPlayer player;
    public InventoryTransmutationTablet inventoryTransmutationTablet;
    public InventoryPortableTablet tabletInventory;
    private float energyValue;
    private String searchTerm;
    private int sortOption;
    private int sortOrder;
    private int scrollBarPosition;

    public ContainerPortableTablet(EntityPlayer player, InventoryPortableTablet tabletInventory) {
        this.player = player;
        InventoryPlayer inventoryPlayer = player.inventory;
        this.tabletInventory = tabletInventory;

        TreeSet<ItemStack> knownTransmutations = new TreeSet<ItemStack>(Comparators.displayNameComparator);
        if (tabletInventory.getStackInSlot(InventoryPortableTablet.ALCHEMICAL_TOME_INDEX) != null) {
            ItemStack itemStack = tabletInventory.getStackInSlot(InventoryPortableTablet.ALCHEMICAL_TOME_INDEX);
            if (ItemLib.isTome(itemStack) && ItemLib.hasOwnerUUID(itemStack))
                knownTransmutations.addAll(TransmutationKnowledgeRegistryProxy.getPlayerKnownTransmutations(ItemLib.getOwnerUUID(itemStack)));
        }
        inventoryTransmutationTablet = new InventoryTransmutationTablet(knownTransmutations);

        this.sortOption = 0;
        this.scrollBarPosition = 0;
        this.energyValue = tabletInventory.getAvailableEnergyValue().getValue();

        this.addSlotToContainer(new SlotTabletInput(this, tabletInventory, InventoryPortableTablet.ITEM_INPUT_1, 62, 24));
        this.addSlotToContainer(new SlotTabletInput(this, tabletInventory, InventoryPortableTablet.ITEM_INPUT_2, 35, 35));
        this.addSlotToContainer(new SlotTabletInput(this, tabletInventory, InventoryPortableTablet.ITEM_INPUT_3, 26, 61));
        this.addSlotToContainer(new SlotTabletInput(this, tabletInventory, InventoryPortableTablet.ITEM_INPUT_4, 35, 87));
        this.addSlotToContainer(new SlotTabletInput(this, tabletInventory, InventoryPortableTablet.ITEM_INPUT_5, 62, 99));
        this.addSlotToContainer(new SlotTabletInput(this, tabletInventory, InventoryPortableTablet.ITEM_INPUT_6, 89, 87));
        this.addSlotToContainer(new SlotTabletInput(this, tabletInventory, InventoryPortableTablet.ITEM_INPUT_7, 98, 61));
        this.addSlotToContainer(new SlotTabletInput(this, tabletInventory, InventoryPortableTablet.ITEM_INPUT_8, 89, 35));
        this.addSlotToContainer(new Slot(tabletInventory, InventoryPortableTablet.STONE_INDEX, 62, 61) {
            @Override
            public int getSlotStackLimit() {
                return 1;
            }

            @Override
            public boolean isItemValid(ItemStack itemStack) {
                return ItemLib.isTransmutationStone(itemStack);
            }
        });
        this.addSlotToContainer(new SlotTome(this, tabletInventory, InventoryPortableTablet.ALCHEMICAL_TOME_INDEX, 152, 15));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlotToContainer(new SlotTabletOutput(this, inventoryTransmutationTablet, i * 3 + j, 175 + j * 20, 38 + i * 20));
            }
        }

        // Add the player's inventory slots to the container
        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex) {
            for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex) {
                this.addSlotToContainer(new Slot(inventoryPlayer, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 164 + inventoryRowIndex * 18));
            }
        }

        // Add the player's action bar slots to the container
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarSlotIndex) {
            this.addSlotToContainer(new Slot(inventoryPlayer, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 222));
        }

        this.updateInventory();
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters) {
            ICrafting iCrafting = (ICrafting) crafter;

            if (this.energyValue != this.tabletInventory.getAvailableEnergyValue().getValue()) {
                this.energyValue = this.tabletInventory.getAvailableEnergyValue().getValue();
                this.updateInventory();
                int energyValueAsInt = Float.floatToRawIntBits(this.tabletInventory.getAvailableEnergyValue().getValue());
                iCrafting.sendProgressBarUpdate(this, 0, energyValueAsInt & 0xffff);
                iCrafting.sendProgressBarUpdate(this, 1, energyValueAsInt >>> 16);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int valueType, int updatedValue) {
        if (valueType == 0) {
            int energyValueAsInt = Float.floatToRawIntBits(energyValue);
            energyValueAsInt = (energyValueAsInt & 0xffff0000) | updatedValue;
            energyValue = Float.intBitsToFloat(energyValueAsInt);
        } else if (valueType == 1) {
            int energyValueAsInt = Float.floatToRawIntBits(energyValue);
            energyValueAsInt = (energyValueAsInt & 0xffff) | (updatedValue << 16);
            energyValue = Float.intBitsToFloat(energyValueAsInt);
        } else if (valueType == 2)
            sortOption = updatedValue;
        else if (valueType == 3)
            scrollBarPosition = updatedValue;
        else if (valueType == 4)
            sortOrder = updatedValue;

        if (valueType >= 0 && valueType <= 4)
            updateInventory();
    }

    @Override
    public void handleElementTextFieldUpdate(String elementName, String updatedText) {
        if (elementName.equalsIgnoreCase("searchField")) {
            this.searchTerm = updatedText;
            updateInventory();
        }
    }

    @Override
    public void handleElementSliderUpdate(String elementName, int elementValue) {
        if (elementName.equals("scrollBar")) {
            this.scrollBarPosition = elementValue;
            updateInventory();
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            this.tabletInventory.onGuiSave();
            // -_- I REALLY REALLY wished I didn't have to use a packet, but for whatever
            // reason the tablet won't save for some reason, and I have no idea why
            // as I've done portable inventory's before. Oh well ¯\_(?)_/¯
            Proxy.INSTANCE.sendToServer(new PacketSaveTablet(this.tabletInventory.getParentStack().getTagCompound()));
        }
    }

    public void handleTransmutationKnowledgeUpdate(TransmutationKnowledge transmutationKnowledge) {
        if (transmutationKnowledge != null) {
            this.inventoryTransmutationTablet = new InventoryTransmutationTablet(transmutationKnowledge.getKnownTransmutations());
            this.updateInventory();
        }
    }

    public void updateInventory() {
        ItemStack[] newInventory = new ItemStack[30];

        Set<ItemStack> filteredSet = FilterUtils.filterByNameContains(this.inventoryTransmutationTablet.getKnownTransmutations(), searchTerm);
        List<ItemStack> filteredList = new ArrayList(FilterUtils.filterByEnergyValue(filteredSet, energyValue));

        int adjustedStartIndex = (int) ((scrollBarPosition / 187f) * filteredList.size());

        if (sortOption == 0) {
            if (sortOrder == 0)
                Collections.sort(filteredList, Comparators.displayNameComparator);
            else
                Collections.sort(filteredList, Comparators.reverseDisplayNameComparator);
        } else if (sortOption == 1) {
            if (sortOrder == 0)
                Collections.sort(filteredList, Comparators.energyValueItemStackComparator);
            else
                Collections.sort(filteredList, Comparators.reverseEnergyValueComparator);
        } else if (sortOption == 2) {
            if (sortOrder == 0)
                Collections.sort(filteredList, Comparators.idComparator);
            else
                Collections.sort(filteredList, Comparators.reverseIdComparator);
        }

        if (filteredList.size() <= 30)
            newInventory = filteredList.toArray(newInventory);
        else if (adjustedStartIndex + 30 <= filteredList.size())
            newInventory = filteredList.subList(adjustedStartIndex, adjustedStartIndex + 30).toArray(newInventory);
        else
            newInventory = filteredList.subList(filteredList.size() - 30, filteredList.size()).toArray(newInventory);

        for (int i = 0; i < 30; i++) {
            this.getSlot(i + 10).putStack(newInventory[i]);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();

            /**
             * If we are shift-clicking an item out of the Transmutation Tablet's container,
             * attempt to put it in the first available slot in the entityPlayer's
             * inventory
             */
            if (slotIndex < ItemPortableTablet.TABLE_SIZE) {
                if (!this.mergeItemStack(slotItemStack, ItemPortableTablet.TABLE_SIZE, inventorySlots.size(), false))
                    return null;
            } else if (slotIndex >= ItemPortableTablet.TABLE_SIZE && slotIndex < 40) {
                if (!this.mergeTransmutatedItemStack(entityPlayer, slot, slotItemStack, 40, inventorySlots.size(), false))
                    return null;
            } else {
                if (ItemLib.isTome(slotItemStack)) {
                    if (!this.mergeItemStack(slotItemStack, InventoryPortableTablet.ALCHEMICAL_TOME_INDEX, ItemPortableTablet.TABLE_SIZE, false))
                        return null;
                } else if (ItemLib.isTransmutationStone(slotItemStack)) {
                    if (!this.mergeItemStack(slotItemStack, InventoryPortableTablet.STONE_INDEX, ItemPortableTablet.TABLE_SIZE, false))
                        return null;
                } else {
                    if (!this.mergeItemStack(slotItemStack, InventoryPortableTablet.ITEM_INPUT_1, ItemPortableTablet.TABLE_SIZE, false))
                        return null;
                }
            }

            if (slotItemStack.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();
        }

        return itemStack;
    }

    protected boolean mergeTransmutatedItemStack(EntityPlayer entityPlayer, Slot transmutationOutputSlot, ItemStack itemStack, int slotMin, int slotMax, boolean ascending) {
        if (this.tabletInventory.getAvailableEnergyValue().compareTo(EnergyValueRegistryProxy.getEnergyValue(itemStack)) < 0)
            return false;

        transmutationOutputSlot.onPickupFromSlot(entityPlayer, itemStack);

        boolean slotFound = false;
        int currentSlotIndex = ascending ? slotMax - 1 : slotMin;

        Slot slot;
        ItemStack stackInSlot;

        if (itemStack.isStackable()) {
            while (itemStack.stackSize > 0 && (!ascending && currentSlotIndex < slotMax || ascending && currentSlotIndex >= slotMin)) {
                slot = (Slot) this.inventorySlots.get(currentSlotIndex);
                stackInSlot = slot.getStack();

                if (slot.isItemValid(itemStack) && ItemLib.equalsIgnoreStackSize(itemStack, stackInSlot)) {
                    int combinedStackSize = stackInSlot.stackSize + itemStack.stackSize;
                    int slotStackSizeLimit = Math.min(stackInSlot.getMaxStackSize(), slot.getSlotStackLimit());

                    if (combinedStackSize <= slotStackSizeLimit) {
                        itemStack.stackSize = 0;
                        stackInSlot.stackSize = combinedStackSize;
                        slot.onSlotChanged();
                        slotFound = true;
                    } else if (stackInSlot.stackSize < slotStackSizeLimit) {
                        itemStack.stackSize -= slotStackSizeLimit - stackInSlot.stackSize;
                        stackInSlot.stackSize = slotStackSizeLimit;
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                }

                currentSlotIndex += ascending ? -1 : 1;
            }
        }

        if (itemStack.stackSize > 0) {
            currentSlotIndex = ascending ? slotMax - 1 : slotMin;

            while (!ascending && currentSlotIndex < slotMax || ascending && currentSlotIndex >= slotMin) {
                slot = (Slot) this.inventorySlots.get(currentSlotIndex);
                stackInSlot = slot.getStack();

                if (slot.isItemValid(itemStack) && stackInSlot == null) {
                    slot.putStack(ItemUtil.cloneStack(itemStack, Math.min(itemStack.stackSize, slot.getSlotStackLimit())));
                    slot.onSlotChanged();

                    if (slot.getStack() != null) {
                        itemStack.stackSize = 0;
                        slotFound = true;
                    }

                    break;
                }

                currentSlotIndex += ascending ? -1 : 1;
            }
        }
        itemStack.stackSize = 1;
        return slotFound;
    }

    @Override
    public void handleElementButtonClick(String elementName, int mouseButton) {
        if (elementName.equals("sortOption")) {
            if (mouseButton == 0) {
                if (sortOption == 0)
                    sortOption = 1;
                else if (sortOption == 1)
                    sortOption = 2;
                else if (sortOption == 2)
                    sortOption = 0;
            } else if (mouseButton == 1) {
                if (sortOption == 0)
                    sortOption = 2;
                else if (sortOption == 1)
                    sortOption = 0;
                else if (sortOption == 2)
                    sortOption = 1;
            }
        } else if (elementName.equals("sortOrder")) {
            if (sortOrder == 0)
                sortOrder = 1;
            else if (sortOrder == 1)
                sortOrder = 0;
        }

        for (Object crafter : this.crafters) {
            ICrafting iCrafting = (ICrafting) crafter;
            iCrafting.sendProgressBarUpdate(this, 2, sortOption);
            iCrafting.sendProgressBarUpdate(this, 4, sortOrder);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tabletInventory.matchesParentStack(player.getCurrentEquippedItem());
    }
}
