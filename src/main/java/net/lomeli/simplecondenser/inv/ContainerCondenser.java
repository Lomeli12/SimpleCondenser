package net.lomeli.simplecondenser.inv;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.simplecondenser.lib.ItemLib;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class ContainerCondenser extends Container {
    private TileCondenserBase tile;
    private float energy;

    public ContainerCondenser(TileCondenserBase condenser, InventoryPlayer inventoryPlayer) {
        this.tile = condenser;
        this.energy = condenser.getStoredEnergyValue().getValue();
        this.addSlotToContainer(new SlotTome(condenser, 0, 148, 25));
        this.addSlotToContainer(new Slot(condenser, 1, 12, 25));
        int f = 2;
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 9; k++) {
                this.addSlotToContainer(new Slot(condenser, f, 8 + k * 18, 54 + i * 18));
                f++;
            }
        }

        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryPlayer, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + 36));
            }
        }

        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 8 + j * 18, 197));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        int energyValueAsInt = Float.floatToRawIntBits(this.tile.getStoredEnergyValue().getValue());
        crafting.sendProgressBarUpdate(this, 0, energyValueAsInt & 0xffff);
        crafting.sendProgressBarUpdate(this, 1, energyValueAsInt >>> 16);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (this.energy != this.tile.getStoredEnergyValue().getValue()) {
                this.energy = this.tile.getStoredEnergyValue().getValue();
                int energyValueAsInt = Float.floatToRawIntBits(this.tile.getStoredEnergyValue().getValue());
                icrafting.sendProgressBarUpdate(this, 0, energyValueAsInt & 0xffff);
                icrafting.sendProgressBarUpdate(this, 1, energyValueAsInt >>> 16);
            }
        }
    }

    @Override
    public void updateProgressBar(int valueType, int updatedValue) {
        if (valueType == 0) {
            int energyValueAsInt = Float.floatToRawIntBits(energy);
            energyValueAsInt = (energyValueAsInt & 0xffff0000) | updatedValue;
            energy = Float.intBitsToFloat(energyValueAsInt);
        } else if (valueType == 1) {
            int energyValueAsInt = Float.floatToRawIntBits(energy);
            energyValueAsInt = (energyValueAsInt & 0xffff) | (updatedValue << 16);
            energy = Float.intBitsToFloat(energyValueAsInt);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();
            if (slotIndex < TileCondenserBase.CONDENSER_SIZE) {
                if (!this.mergeItemStack(slotItemStack, TileCondenserBase.CONDENSER_SIZE, inventorySlots.size() - 4, false))
                    return null;
            } else {
                if (ItemLib.isTome(slotItemStack)) {
                    if (!this.mergeItemStack(slotItemStack, TileCondenserBase.TOME_SLOT, TileCondenserBase.CONDENSER_SIZE, false))
                        return null;
                } else if (ItemLib.playerKnowsItem(entityPlayer.getUniqueID(), slotItemStack)) {
                    if (!this.mergeItemStack(slotItemStack, TileCondenserBase.TARGET_SLOT, TileCondenserBase.CONDENSER_SIZE, false))
                        return null;
                } else {
                    if (!this.mergeItemStack(slotItemStack, 2, TileCondenserBase.CONDENSER_SIZE, false))
                        return null;
                }
            }
            if (slotItemStack.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();
        }
        return null;
    }

    public float getEnergy() {
        return this.energy;
    }
}
