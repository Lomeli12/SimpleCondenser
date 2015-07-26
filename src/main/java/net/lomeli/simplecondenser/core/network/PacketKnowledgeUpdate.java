package net.lomeli.simplecondenser.core.network;

import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;

import net.lomeli.lomlib.core.network.AbstractPacket;
import net.lomeli.lomlib.core.network.SidedPacket;

import net.lomeli.simplecondenser.inventory.ContainerPortableTablet;

import com.pahimar.ee3.knowledge.TransmutationKnowledge;
import com.pahimar.ee3.util.CompressionHelper;


@SidedPacket(acceptedServerSide = false)
public class PacketKnowledgeUpdate extends AbstractPacket {
    private TransmutationKnowledge transmutationKnowledge;
    private UUID playerUUID;
    private int dim;

    public PacketKnowledgeUpdate() {
    }

    public PacketKnowledgeUpdate(int dim, UUID uuid, Collection<ItemStack> knownTransmutationsCollection) {
        this.dim = dim;
        this.playerUUID = uuid;
        this.transmutationKnowledge = new TransmutationKnowledge();
        if (knownTransmutationsCollection != null)
            this.transmutationKnowledge = new TransmutationKnowledge(knownTransmutationsCollection);
    }

    @Override
    public void encodeInto(ByteBuf buffer) {
        buffer.writeLong(this.playerUUID.getMostSignificantBits());
        buffer.writeLong(this.playerUUID.getLeastSignificantBits());
        buffer.writeInt(this.dim);
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
    public void decodeInto(ByteBuf buffer) {
        long most = buffer.readLong();
        long least = buffer.readLong();
        this.playerUUID = new UUID(most, least);
        this.dim = buffer.readInt();
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
    public void handlePacket(Side side) {
	EntityClientPlayerMP player = FMLClientHandler.instance().getClientPlayerEntity();
	if (player instanceof EntityPlayer) {
	    if (FMLClientHandler.instance().getClient().currentScreen != null && FMLClientHandler.instance().getClient().currentScreen instanceof GuiContainer) {
		GuiContainer guiContainer = (GuiContainer) FMLClientHandler.instance().getClient().currentScreen;
		if (guiContainer.inventorySlots instanceof ContainerPortableTablet) {
		    ContainerPortableTablet tablet = (ContainerPortableTablet) guiContainer.inventorySlots;
		    if (tablet.canInteractWith(player))
			tablet.handleTransmutationKnowledgeUpdate(this.transmutationKnowledge);
		}
	    }
	}
    }
}
