package net.lomeli.simplecondenser.core;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.blocks.ModBlocks;
import net.lomeli.simplecondenser.client.handler.GuiHandler;
import net.lomeli.simplecondenser.core.network.PacketKnowledgeUpdate;
import net.lomeli.simplecondenser.core.network.PacketSaveTablet;
import net.lomeli.simplecondenser.item.ModItems;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

import cpw.mods.fml.relauncher.Side;

public class Proxy {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SimpleCondenser.MOD_ID);

    public void preInit() {
        SimpleCondenser.config.loadConfig();
        if (ModConfig.checkForUpdates)
            SimpleCondenser.checker.checkForUpdates();
        ModBlocks.initBlocks();
        ModItems.initItems();
        ModRecipes.initEMCValues();
    }

    public void init() {
	INSTANCE.registerMessage(PacketSaveTablet.class, PacketSaveTablet.class, 0, Side.SERVER);
	INSTANCE.registerMessage(PacketKnowledgeUpdate.class, PacketKnowledgeUpdate.class, 1, Side.CLIENT);
	GameRegistry.registerTileEntity(TileCondenserBase.class, SimpleCondenser.MOD_ID + ".simpleCondenser");
	NetworkRegistry.INSTANCE.registerGuiHandler(SimpleCondenser.modInstance, new GuiHandler());
    }

    public void postInit() {
        ModRecipes.initRecipes();
    }

    public void playSoundAtPlayer(EntityPlayer player, String sound, float volume, float pitch) {
    }
}
