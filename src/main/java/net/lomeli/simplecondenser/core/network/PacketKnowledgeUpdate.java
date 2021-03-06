package net.lomeli.simplecondenser.core.network;

import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;

import net.lomeli.lomlib.core.network.AbstractPacket;
import net.lomeli.lomlib.core.network.SidedPacket;

import net.lomeli.simplecondenser.inventory.ContainerPortableTablet;

import com.pahimar.ee3.knowledge.TransmutationKnowledge;
import com.pahimar.ee3.util.CompressionHelper;


import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


public class PacketKnowledgeUpdate implements IMessage, IMessageHandler<PacketKnowledgeUpdate, IMessage> {
    private TransmutationKnowledge transmutationKnowledge;

    public PacketKnowledgeUpdate() {
    }

    public PacketKnowledgeUpdate(Collection<ItemStack> knownTransmutationsCollection) {
        this.transmutationKnowledge = new TransmutationKnowledge();
        if (knownTransmutationsCollection != null)
            this.transmutationKnowledge = new TransmutationKnowledge(knownTransmutationsCollection);
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        byte[] compressedString = null;

        if (transmutationKnowledge != null)
            compressedString = CompressionHelper.compressStringToByteArray(transmutationKnowledge.toJson());

        if (compressedString != null) {
            buffer.writeInt(compressedString.length);
            buffer.writeBytes(compressedString);
        } else
            buffer.writeInt(0);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        byte[] compressedString = null;
        int readableBytes = buffer.readInt();

        if (readableBytes > 0)
            compressedString = buffer.readBytes(readableBytes).array();


        if (compressedString != null) {
            String uncompressedString = CompressionHelper.decompressStringFromByteArray(compressedString);
            this.transmutationKnowledge = TransmutationKnowledge.createFromJson(uncompressedString);
        }
    }

    @Override
    public IMessage onMessage(PacketKnowledgeUpdate message, MessageContext ctx) {
	if (FMLClientHandler.instance().getClient().currentScreen != null && FMLClientHandler.instance().getClient().currentScreen instanceof GuiContainer) {
	    GuiContainer guiContainer = (GuiContainer) FMLClientHandler.instance().getClient().currentScreen;
	    if (guiContainer.inventorySlots instanceof ContainerPortableTablet) {
		ContainerPortableTablet tablet = (ContainerPortableTablet) guiContainer.inventorySlots;
		tablet.handleTransmutationKnowledgeUpdate(message.transmutationKnowledge);
	    }
	}
	return null;
    }
}
