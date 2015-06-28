package net.lomeli.simplecondenser.core;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.lomlib.core.network.PacketHandler;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.blocks.ModBlocks;
import net.lomeli.simplecondenser.client.handler.GuiHandler;
import net.lomeli.simplecondenser.core.network.PacketKnowledgeUpdate;
import net.lomeli.simplecondenser.core.network.PacketSaveTablet;
import net.lomeli.simplecondenser.item.ModItems;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class Proxy {
    public void preInit() {
        SimpleCondenser.config.loadConfig();
        if (ModConfig.checkForUpdates)
            SimpleCondenser.checker.checkForUpdates();
        ModBlocks.initBlocks();
        ModItems.initItems();
        ModRecipes.initEMCValues();
    }

    public void init() {
        SimpleCondenser.packetHandler = new PacketHandler(SimpleCondenser.MOD_ID, PacketSaveTablet.class, PacketKnowledgeUpdate.class);
        GameRegistry.registerTileEntity(TileCondenserBase.class, SimpleCondenser.MOD_ID + ".simpleCondenser");
        NetworkRegistry.INSTANCE.registerGuiHandler(SimpleCondenser.modInstance, new GuiHandler());
    }

    public void postInit() {
        ModRecipes.initRecipes();
    }

    public void playSoundAtPlayer(EntityPlayer player, String sound, float volume, float pitch) {
    }
}
