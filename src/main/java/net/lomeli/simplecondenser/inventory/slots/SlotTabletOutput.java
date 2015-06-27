package net.lomeli.simplecondenser.inventory.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.simplecondenser.inventory.ContainerPortableTablet;

public class SlotTabletOutput extends Slot {
    private ContainerPortableTablet containerTransmutationTablet;

    public SlotTabletOutput(ContainerPortableTablet containerTransmutationTablet, IInventory iInventory, int slotIndex, int x, int y) {
        super(iInventory, slotIndex, x, y);
        this.containerTransmutationTablet = containerTransmutationTablet;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer entityPlayer) {
        return this.getHasStack();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
        super.onPickupFromSlot(entityPlayer, itemStack);

        if (this.getHasStack()) {
            this.containerTransmutationTablet.tabletInventory.consumeInventoryForEnergyValue(itemStack);
            this.containerTransmutationTablet.updateInventory();
        }
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();

        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
            this.containerTransmutationTablet.tabletInventory.updateEnergyValueFromInventory();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_111238_b() {
        return this.getHasStack();
    }
}
