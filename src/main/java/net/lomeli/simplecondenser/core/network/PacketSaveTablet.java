package net.lomeli.simplecondenser.core.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;

import net.lomeli.lomlib.core.network.AbstractPacket;
import net.lomeli.lomlib.core.network.SidedPacket;

@SidedPacket(acceptedClientSide = false)
public class PacketSaveTablet extends AbstractPacket {
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
    public void encodeInto(ByteBuf buffer) {
        buffer.writeLong(this.playerUUID.getMostSignificantBits());
        buffer.writeLong(this.playerUUID.getLeastSignificantBits());
        buffer.writeInt(this.dim);
        ByteBufUtils.writeTag(buffer, this.tag);
    }

    @Override
    public void decodeInto(ByteBuf buffer) {
        long most = buffer.readLong();
        long least = buffer.readLong();
        this.playerUUID = new UUID(most, least);
        this.dim = buffer.readInt();
        this.tag = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void handlePacket(Side side) {
        EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim).func_152378_a(playerUUID);
        if (player != null) {
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null && stack.getItem() != null) {
                stack.stackTagCompound = tag;
                player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
            }
        }
    }
}
