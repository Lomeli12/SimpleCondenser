package net.lomeli.simplecondenser.client;

import java.util.Collection;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

import net.lomeli.repackaged.blusunrize.lib.manual.ManualPages;
import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.blocks.ModBlocks;
import net.lomeli.simplecondenser.client.gui.SCManualInstance;
import net.lomeli.simplecondenser.client.handler.TickHandlerClient;
import net.lomeli.simplecondenser.client.render.RenderAlchemicalCondenser;
import net.lomeli.simplecondenser.core.Proxy;
import net.lomeli.simplecondenser.inventory.ContainerPortableTablet;
import net.lomeli.simplecondenser.item.ModItems;
import net.lomeli.simplecondenser.lib.ModRenderIDs;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

import com.pahimar.ee3.knowledge.TransmutationKnowledge;

public class ClientProxy extends Proxy {
    public static SCManualInstance manual;

    @Override
    public void preInit() {
        super.preInit();
        SimpleCondenser.config.initEvent();
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
    public void loadComplete() {
        super.loadComplete();
        manual = new SCManualInstance();
        manual.addEntry("introduction", "basics",
                new ManualPages.Text(manual, "introduction0"));
        manual.addEntry("part2", "basics",
                new ManualPages.Text(manual, "introduction1"),
                new ManualPages.Text(manual, "introduction2"));
        manual.addEntry("book", "tools",
                new ManualPages.Text(manual, "compendiumInfo"),
                new ManualPages.Crafting(manual, "compendiumCraft", new ItemStack(ModItems.alchemicalCompendium)));
    }

    @Override
    public void playSoundAtPlayer(EntityPlayer player, String sound, float volume, float pitch) {
        FMLClientHandler.instance().getClient().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation("ee3", sound), volume, pitch, (float) player.posX, (float) player.posY, (float) player.posZ));
    }

    @Override
    public void updateTablet(Collection<ItemStack> knownItems) {
        TransmutationKnowledge knowledge = new TransmutationKnowledge();
        if (knownItems != null && knownItems.size() > 0)
            knowledge = new TransmutationKnowledge(knownItems);
        EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        if (player != null) {
            if (FMLClientHandler.instance().getClient().currentScreen != null && FMLClientHandler.instance().getClient().currentScreen instanceof GuiContainer) {
                GuiContainer guiContainer = (GuiContainer) FMLClientHandler.instance().getClient().currentScreen;
                if (guiContainer.inventorySlots instanceof ContainerPortableTablet) {
                    ContainerPortableTablet tablet = (ContainerPortableTablet) guiContainer.inventorySlots;
                    if (tablet.canInteractWith(player))
                        tablet.handleTransmutationKnowledgeUpdate(knowledge);
                }
            }
        }
    }
}
