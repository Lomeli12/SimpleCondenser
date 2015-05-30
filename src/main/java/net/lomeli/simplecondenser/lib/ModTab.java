package net.lomeli.simplecondenser.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.item.ModItems;

public class ModTab {
    public static final CreativeTabs modTab = new CreativeTabs(SimpleCondenser.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return ModItems.chargedStar;
        }
    };
}
