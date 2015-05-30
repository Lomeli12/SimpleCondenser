package net.lomeli.simplecondenser.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.simplecondenser.lib.EnumAlchemicalType;
import net.lomeli.simplecondenser.lib.ItemLib;

import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.api.recipe.AludelRecipeProxy;

public class ModBlocks {
    public static Block verdantCondenser, azureCondenser, miniumCondenser, condenserBase;

    public static void initBlocks() {
        condenserBase = new BlockSC(Material.rock).setBlockName("condenserBase").setBlockTextureName("condenserBase");
        verdantCondenser = new BlockCondenser(EnumAlchemicalType.VERDANT).setBlockName("verdantCondenser");
        azureCondenser = new BlockCondenser(EnumAlchemicalType.AZURE).setBlockName("azureCondenser");
        miniumCondenser = new BlockCondenser(EnumAlchemicalType.MINIUM).setBlockName("miniumCondenser");

        GameRegistry.registerBlock(condenserBase, "condenserBase");
        GameRegistry.registerBlock(verdantCondenser, "verdantCondenser");
        GameRegistry.registerBlock(azureCondenser, "azureCondenser");
        GameRegistry.registerBlock(miniumCondenser, "miniumCondenser");
    }

    public static void blockRecipes() {
        EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(verdantCondenser, 1, OreDictionary.WILDCARD_VALUE), 131136);
        AludelRecipeProxy aludelRecipeProxy = new AludelRecipeProxy();
        // TODO: Come up with a funnier way of crafting the condensers
        aludelRecipeProxy.addRecipe(new ItemStack(verdantCondenser), new ItemStack(condenserBase), new ItemStack(ItemLib.alchemicalDust, 8, 1));
        aludelRecipeProxy.addRecipe(new ItemStack(azureCondenser), new ItemStack(verdantCondenser), new ItemStack(ItemLib.alchemicalDust, 16, 2));
        aludelRecipeProxy.addRecipe(new ItemStack(miniumCondenser), new ItemStack(azureCondenser), new ItemStack(ItemLib.alchemicalDust, 32, 3));
    }
}
