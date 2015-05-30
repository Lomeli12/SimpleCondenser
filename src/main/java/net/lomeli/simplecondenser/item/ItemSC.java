package net.lomeli.simplecondenser.item;

import net.minecraft.item.Item;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.lib.ModTab;

public class ItemSC extends Item {

    public ItemSC() {
        super();
        this.setCreativeTab(ModTab.modTab);
    }

    @Override
    public Item setUnlocalizedName(String name) {
        return super.setUnlocalizedName(SimpleCondenser.MOD_ID + "." + name);
    }

    @Override
    public Item setTextureName(String texture) {
        return super.setTextureName(SimpleCondenser.MOD_ID + ":" + texture);
    }
}
