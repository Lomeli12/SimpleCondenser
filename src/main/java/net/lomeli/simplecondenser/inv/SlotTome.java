package net.lomeli.simplecondenser.inv;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.simplecondenser.lib.ItemLib;

public class SlotTome extends Slot {
    public SlotTome(IInventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return ItemLib.isTome(stack);
    }
}
