package net.lomeli.simplecondenser.client;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

import net.lomeli.simplecondenser.blocks.ModBlocks;
import net.lomeli.simplecondenser.client.handler.TickHandlerClient;
import net.lomeli.simplecondenser.client.render.RenderAlchemicalCondenser;
import net.lomeli.simplecondenser.core.Proxy;
import net.lomeli.simplecondenser.lib.ModRenderIDs;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class ClientProxy extends Proxy {
    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        ModRenderIDs.condenserRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderAlchemicalCondenser condenserRenderer = new RenderAlchemicalCondenser();
        ClientRegistry.bindTileEntitySpecialRenderer(TileCondenserBase.class, condenserRenderer);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.verdantCondenser), condenserRenderer);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.azureCondenser), condenserRenderer);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.miniumCondenser), condenserRenderer);

        FMLCommonHandler.instance().bus().register(new TickHandlerClient());
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    @Override
    public void playSoundAtPlayer(EntityPlayer player, String sound, float volume, float pitch) {
        FMLClientHandler.instance().getClient().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation("ee3", sound), volume, pitch, (float) player.posX, (float) player.posY, (float) player.posZ));
    }
}
