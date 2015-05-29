package net.lomeli.simplecondenser.core;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.blocks.ModBlocks;
import net.lomeli.simplecondenser.client.gui.GuiHandler;
import net.lomeli.simplecondenser.lib.ItemLib;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class Proxy {
    public void preInit() {
        ModBlocks.initBlocks();
    }

    public void init() {
        GameRegistry.registerTileEntity(TileCondenserBase.class, SimpleCondenser.MOD_ID + ".simpleCondenser");
        NetworkRegistry.INSTANCE.registerGuiHandler(SimpleCondenser.modInstance, new GuiHandler());
    }

    public void postInit() {
        ItemLib.init();
        ModBlocks.blockRecipes();
    }
}
