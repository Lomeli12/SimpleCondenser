package net.lomeli.simplecondenser.core.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;

import net.lomeli.lomlib.core.network.AbstractPacket;
import net.lomeli.lomlib.core.network.SidedPacket;


import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSaveTablet implements IMessage, IMessageHandler<PacketSaveTablet, IMessage> {
    private NBTTagCompound tag;
    private UUID playerUUID;
    private int dim;

    public PacketSaveTablet() {
    }

    public PacketSaveTablet(NBTTagCompound tag, UUID playerUUID, int dim) {
        this.tag = tag;
        this.playerUUID = playerUUID;
        this.dim = dim;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeLong(this.playerUUID.getMostSignificantBits());
        buffer.writeLong(this.playerUUID.getLeastSignificantBits());
        buffer.writeInt(this.dim);
        ByteBufUtils.writeTag(buffer, this.tag);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        long most = buffer.readLong();
        long least = buffer.readLong();
        this.playerUUID = new UUID(most, least);
        this.dim = buffer.readInt();
        this.tag = ByteBufUtils.readTag(buffer);
    }

    @Override
    public IMessage onMessage(PacketSaveTablet message, MessageContext ctx) {
	EntityPlayer player = ctx.getServerHandler().playerEntity;
	if (player != null) {
	    ItemStack stack = player.getCurrentEquippedItem();
	    if (stack != null && stack.getItem() != null) {
		stack.stackTagCompound = message.tag;
		player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
	    }
	}
	return null;
    }
}
