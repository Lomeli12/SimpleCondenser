package net.lomeli.simplecondenser.inventory.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.simplecondenser.inventory.ContainerPortableTablet;

import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.api.knowledge.AbilityRegistryProxy;

public class SlotTabletInput extends Slot {
    private ContainerPortableTablet containerTransmutationTablet;

    public SlotTabletInput(ContainerPortableTablet containerTransmutationTablet, IInventory iInventory, int slotIndex, int x, int y) {
        super(iInventory, slotIndex, x, y);
        this.containerTransmutationTablet = containerTransmutationTablet;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return EnergyValueRegistryProxy.hasEnergyValue(itemStack) && AbilityRegistryProxy.isRecoverable(itemStack);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
        super.onPickupFromSlot(entityPlayer, itemStack);
        this.containerTransmutationTablet.tabletInventory.updateEnergyValueFromInventory();
        this.containerTransmutationTablet.updateInventory();
    }

    @Override
    public void putStack(ItemStack itemStack) {
        super.putStack(itemStack);
        this.containerTransmutationTablet.tabletInventory.updateEnergyValueFromInventory();
        this.containerTransmutationTablet.updateInventory();
    }
}
