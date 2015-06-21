package net.lomeli.simplecondenser.blocks;

import net.minecraft.block.Block;

import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.simplecondenser.lib.enums.AlchemicalType;

public class ModBlocks {
    public static Block verdantCondenser, azureCondenser, miniumCondenser;

    public static void initBlocks() {
        verdantCondenser = new BlockCondenser(AlchemicalType.VERDANT).setBlockName("verdantCondenser");
        azureCondenser = new BlockCondenser(AlchemicalType.AZURE).setBlockName("azureCondenser");
        miniumCondenser = new BlockCondenser(AlchemicalType.MINIUM).setBlockName("miniumCondenser");

        GameRegistry.registerBlock(verdantCondenser, "verdantCondenser");
        GameRegistry.registerBlock(azureCondenser, "azureCondenser");
        GameRegistry.registerBlock(miniumCondenser, "miniumCondenser");
    }
}
