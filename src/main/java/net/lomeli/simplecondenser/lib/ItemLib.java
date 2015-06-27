package net.lomeli.simplecondenser.lib;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.lomlib.util.NBTUtil;

import com.pahimar.ee3.api.knowledge.TransmutationKnowledgeRegistryProxy;

public class ItemLib {
    @GameRegistry.ObjectHolder("EE3:alchemicalTome")
    public static Item tome;
    @GameRegistry.ObjectHolder("EE3:alchemicalDust")
    public static Item alchemicalDust;
    @GameRegistry.ObjectHolder("EE3:stoneMinium")
    public static Item stoneMinium;
    @GameRegistry.ObjectHolder("EE3:stonePhilosophers")
    public static Item stonePhilosophers;

    @GameRegistry.ObjectHolder("EE3:alchemicalChest")
    public static Block alchemicalChest;
    @GameRegistry.ObjectHolder("EE3:ashInfusedStone")
    public static Block ashBlock;
    @GameRegistry.ObjectHolder("EE3:alchemyArray")
    public static Block alchemyArray;

    public static boolean isTome(ItemStack stack) {
        return (stack != null && stack.getItem() != null) ? stack.getItem() == tome : false;
    }

    public static boolean hasOwnerUUID(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() != null && NBTUtil.hasTag(itemStack, "ownerUUIDMostSig") && NBTUtil.hasTag(itemStack, "ownerUUIDLeastSig");
    }

    public static UUID getOwnerUUID(ItemStack itemStack) {
        return hasOwnerUUID(itemStack) ? new UUID(NBTUtil.getLong(itemStack, "ownerUUIDMostSig"), NBTUtil.getLong(itemStack, "ownerUUIDLeastSig")) : null;
    }

    public static boolean playerKnowsItem(UUID playerUUID, ItemStack stack) {
        return playerUUID != null ? TransmutationKnowledgeRegistryProxy.doesPlayerKnow(playerUUID, stack) : false;
    }

    public static boolean isTransmutationStone(ItemStack stack) {
        return (stack != null && stack.getItem() != null) ? (stack.getItem() == stoneMinium || stack.getItem() == stonePhilosophers) : false;
    }

    public static boolean equalsIgnoreStackSize(ItemStack itemStack1, ItemStack itemStack2) {
        if (itemStack1 != null && itemStack2 != null) {
            // Sort on itemID
            if (Item.getIdFromItem(itemStack1.getItem()) - Item.getIdFromItem(itemStack2.getItem()) == 0) {
                // Sort on item
                if (itemStack1.getItem() == itemStack2.getItem()) {
                    // Then sort on meta
                    if (itemStack1.getItemDamage() == itemStack2.getItemDamage()) {
                        // Then sort on NBT
                        if (itemStack1.hasTagCompound() && itemStack2.hasTagCompound()) {
                            // Then sort on stack size
                            if (ItemStack.areItemStackTagsEqual(itemStack1, itemStack2))
                                return true;
                        } else if (!itemStack1.hasTagCompound() && !itemStack2.hasTagCompound())
                            return true;
                    }
                }
            }
        }

        return false;
    }
}
