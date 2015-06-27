package net.lomeli.simplecondenser.inventory.slots;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.FMLCommonHandler;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.core.network.PacketKnowledgeUpdate;
import net.lomeli.simplecondenser.inventory.ContainerPortableTablet;
import net.lomeli.simplecondenser.inventory.InventoryPortableTablet;
import net.lomeli.simplecondenser.inventory.InventoryTransmutationTablet;
import net.lomeli.simplecondenser.lib.ItemLib;

import com.pahimar.ee3.api.knowledge.TransmutationKnowledgeRegistryProxy;

public class SlotTome extends Slot {
    private ContainerPortableTablet containerTransmutationTablet;
    private InventoryPortableTablet transmutationTablet;

    public SlotTome(IInventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
        this.containerTransmutationTablet = null;
        this.transmutationTablet = null;
    }

    public SlotTome(ContainerPortableTablet containerTransmutationTablet, IInventory iInventory, int slotIndex, int x, int y) {
        super(iInventory, slotIndex, x, y);
        this.containerTransmutationTablet = containerTransmutationTablet;
        this.transmutationTablet = containerTransmutationTablet.tabletInventory;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
        super.onPickupFromSlot(entityPlayer, itemStack);

        if (this.containerTransmutationTablet != null) {
            this.containerTransmutationTablet.inventoryTransmutationTablet = new InventoryTransmutationTablet();
            this.containerTransmutationTablet.updateInventory();
            if (!entityPlayer.worldObj.isRemote && itemStack != null && isItemValid(itemStack) && ItemLib.hasOwnerUUID(itemStack)) {
                EntityPlayerMP mp = getPlayerMP(entityPlayer);
                if (mp != null)
                    SimpleCondenser.packetHandler.sendTo(new PacketKnowledgeUpdate(entityPlayer.dimension, entityPlayer.getUniqueID(), null), mp);
            }
        }
    }

    @Override
    public void putStack(ItemStack itemStack) {
        super.putStack(itemStack);

        if (this.containerTransmutationTablet != null && this.transmutationTablet != null) {
            if (!this.transmutationTablet.getWorld().isRemote && itemStack != null && ItemLib.isTome(itemStack) && ItemLib.hasOwnerUUID(itemStack)) {
                Set<ItemStack> knownTransmutations = TransmutationKnowledgeRegistryProxy.getPlayerKnownTransmutations(ItemLib.getOwnerUUID(itemStack));
                this.containerTransmutationTablet.inventoryTransmutationTablet = new InventoryTransmutationTablet(knownTransmutations);
                this.containerTransmutationTablet.updateInventory();
                EntityPlayerMP mp = getPlayerMP(this.containerTransmutationTablet.player);
                if (mp != null)
                    SimpleCondenser.packetHandler.sendTo(new PacketKnowledgeUpdate(mp.dimension, mp.getUniqueID(), knownTransmutations), mp);
            }
        }
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return ItemLib.isTome(stack);
    }

    public EntityPlayerMP getPlayerMP(EntityPlayer player) {
        return (EntityPlayerMP) (player instanceof EntityPlayerMP ? player : FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(player.dimension).func_152378_a(player.getUniqueID()));
    }
}
