package net.lomeli.simplecondenser.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {
    public static Item greatStar;

    public static void initItems() {
        greatStar = new ItemGreatStar();

        GameRegistry.registerItem(greatStar, "greatStar");
    }
}
