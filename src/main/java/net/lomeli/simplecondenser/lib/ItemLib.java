package net.lomeli.simplecondenser.lib;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.lomeli.lomlib.util.BlockUtil;
import net.lomeli.lomlib.util.ItemUtil;
import net.lomeli.lomlib.util.NBTUtil;

import com.pahimar.ee3.api.knowledge.TransmutationKnowledgeRegistryProxy;

public class ItemLib {
    public static Item tome, alchemicalDust, stoneMinium;
    public static Block alchemicalChest;

    public static void init() {
        // Items
        tome = ItemUtil.getItem("alchemicalTome", "com.pahimar.ee3.init.ModItems").getItem();
        alchemicalDust = ItemUtil.getItem("alchemicalDust", "com.pahimar.ee3.init.ModItems").getItem();
        stoneMinium = ItemUtil.getItem("stoneMinium", "com.pahimar.ee3.init.ModItems").getItem();

        // Blocks
        alchemicalChest = Block.getBlockFromItem(BlockUtil.getBlockFromMod("alchemicalChest", "com.pahimar.ee3.init.ModBlocks").getItem());
    }

    public static boolean isTome(ItemStack stack) {
        if (stack != null && stack.getItem() != null)
            return stack.getItem() == tome;
        return false;
    }

    public static UUID getOwnerUUID(ItemStack itemStack) {
        if (NBTUtil.hasTag(itemStack, "ownerUUIDMostSig") && NBTUtil.hasTag(itemStack, "ownerUUIDLeastSig"))
            return new UUID(NBTUtil.getLong(itemStack, "ownerUUIDMostSig"), NBTUtil.getLong(itemStack, "ownerUUIDLeastSig"));
        return null;
    }

    public static boolean playerKnowsItem(UUID playerUUID, ItemStack stack) {
        if (playerUUID != null)
            return TransmutationKnowledgeRegistryProxy.doesPlayerKnow(playerUUID, stack);
        return false;
    }
}
