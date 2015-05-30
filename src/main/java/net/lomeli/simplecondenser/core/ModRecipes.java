package net.lomeli.simplecondenser.core;

import net.minecraft.item.ItemStack;

import net.lomeli.simplecondenser.blocks.ModBlocks;
import net.lomeli.simplecondenser.item.ModItems;
import net.lomeli.simplecondenser.lib.ItemLib;

import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.api.recipe.AludelRecipeProxy;

public class ModRecipes {

    public static void initEMCValues() {
        EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ModItems.greatStar, 1, 0), 8194f);
        EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ModItems.chargedStar), 73846f);
        EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ModBlocks.verdantCondenser), 74358f);
        EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ModBlocks.azureCondenser), 107126f);
        EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ModBlocks.miniumCondenser), 369270f);
    }

    public static void initRecipes() {
        AludelRecipeProxy aludelRecipeProxy = new AludelRecipeProxy();
        aludelRecipeProxy.addRecipe(new ItemStack(ModBlocks.verdantCondenser), new ItemStack(ModItems.chargedStar), new ItemStack(ItemLib.alchemicalDust, 8, 1));
        aludelRecipeProxy.addRecipe(new ItemStack(ModBlocks.azureCondenser), new ItemStack(ModBlocks.verdantCondenser), new ItemStack(ItemLib.alchemicalDust, 16, 2));
        aludelRecipeProxy.addRecipe(new ItemStack(ModBlocks.miniumCondenser), new ItemStack(ModBlocks.azureCondenser), new ItemStack(ItemLib.alchemicalDust, 32, 3));
        aludelRecipeProxy.addRecipe(new ItemStack(ModItems.greatStar), new ItemStack(ItemLib.ashBlock), new ItemStack(ItemLib.alchemicalDust, 1, 3));
    }
}
