package net.lomeli.simplecondenser.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.simplecondenser.lib.ItemLib;

import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.api.recipe.AludelRecipeProxy;

public class ModBlocks {
    public static Block verdantCondenser, azureCondenser, miniumCondenser;

    public static void initBlocks() {
        verdantCondenser = new BlockCondenser(20L, "verdant").setBlockName("verdantCondenser");
        azureCondenser = new BlockCondenser(10L, "azure").setBlockName("azureCondenser");
        miniumCondenser = new BlockCondenser(1L, "minium").setBlockName("miniumCondenser");

        GameRegistry.registerBlock(verdantCondenser, "verdantCondenser");
        GameRegistry.registerBlock(azureCondenser, "azureCondenser");
        GameRegistry.registerBlock(miniumCondenser, "miniumCondenser");
    }

    public static void blockRecipes() {
        EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(verdantCondenser, 1, OreDictionary.WILDCARD_VALUE), 131136);
        AludelRecipeProxy aludelRecipeProxy = new AludelRecipeProxy();
        // TODO: Come up with a funnier way of crafting the condensers
        aludelRecipeProxy.addRecipe(new ItemStack(verdantCondenser), new ItemStack(ItemLib.alchemicalChest, 1), new ItemStack(ItemLib.alchemicalDust, 8, 3));
        aludelRecipeProxy.addRecipe(new ItemStack(azureCondenser), new ItemStack(ItemLib.alchemicalChest, 1, 1), new ItemStack(ItemLib.alchemicalDust, 8, 3));
        aludelRecipeProxy.addRecipe(new ItemStack(miniumCondenser), new ItemStack(ItemLib.alchemicalChest, 1, 2), new ItemStack(ItemLib.alchemicalDust, 8, 3));
    }
}
