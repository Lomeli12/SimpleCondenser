package net.lomeli.simplecondenser.core;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.blocks.ModBlocks;
import net.lomeli.simplecondenser.client.gui.GuiHandler;
import net.lomeli.simplecondenser.core.handler.ItemEventHandler;
import net.lomeli.simplecondenser.item.ModItems;
import net.lomeli.simplecondenser.lib.ItemLib;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class Proxy {
    public void preInit() {
        ModBlocks.initBlocks();
        ModItems.initItems();
    }

    public void init() {
        GameRegistry.registerTileEntity(TileCondenserBase.class, SimpleCondenser.MOD_ID + ".simpleCondenser");
        NetworkRegistry.INSTANCE.registerGuiHandler(SimpleCondenser.modInstance, new GuiHandler());

        MinecraftForge.EVENT_BUS.register(new ItemEventHandler());
    }

    public void postInit() {
        ItemLib.init();
        ModBlocks.blockRecipes();
    }

    public void playSoundAtPlayer(EntityPlayer player, String sound, float volume, float pitch) {
    }
}
