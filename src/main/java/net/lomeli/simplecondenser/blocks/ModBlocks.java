package net.lomeli.simplecondenser.blocks;

import net.minecraft.block.Block;

import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.simplecondenser.lib.EnumAlchemicalType;

public class ModBlocks {
    public static Block verdantCondenser, azureCondenser, miniumCondenser;

    public static void initBlocks() {
        verdantCondenser = new BlockCondenser(EnumAlchemicalType.VERDANT).setBlockName("verdantCondenser");
        azureCondenser = new BlockCondenser(EnumAlchemicalType.AZURE).setBlockName("azureCondenser");
        miniumCondenser = new BlockCondenser(EnumAlchemicalType.MINIUM).setBlockName("miniumCondenser");

        GameRegistry.registerBlock(verdantCondenser, "verdantCondenser");
        GameRegistry.registerBlock(azureCondenser, "azureCondenser");
        GameRegistry.registerBlock(miniumCondenser, "miniumCondenser");
    }
}
