package net.lomeli.simplecondenser.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {
    public static Item greatStar, chargedStar;

    public static void initItems() {
        greatStar = new ItemGreatStar();
        chargedStar = new ItemChargedStar();

        GameRegistry.registerItem(greatStar, "greatStar");
        GameRegistry.registerItem(chargedStar, "chargedStar");
    }
}
