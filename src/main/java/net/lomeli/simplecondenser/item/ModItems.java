package net.lomeli.simplecondenser.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {
    public static Item greatStar, chargedStar, condenserUpgrade, portableTablet, alchemicalCompendium;

    public static void initItems() {
        greatStar = new ItemGreatStar();
        chargedStar = new ItemChargedStar();
        condenserUpgrade = new ItemCondenserUpgrade();
        portableTablet = new ItemPortableTablet();
        alchemicalCompendium = new ItemManual();

        GameRegistry.registerItem(greatStar, "greatStar");
        GameRegistry.registerItem(chargedStar, "chargedStar");
        GameRegistry.registerItem(condenserUpgrade, "condenserUpgrade");
        GameRegistry.registerItem(portableTablet, "portableTablet");
        GameRegistry.registerItem(alchemicalCompendium, "alchemicalCompendium");
    }
}
