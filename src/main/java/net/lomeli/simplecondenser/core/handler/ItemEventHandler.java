package net.lomeli.simplecondenser.core.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.lomlib.util.ItemUtil;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.blocks.ModBlocks;
import net.lomeli.simplecondenser.lib.ItemLib;

public class ItemEventHandler {
    @SubscribeEvent
    public void playerRightClickBlockEvent(PlayerInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        World world = event.world;
        PlayerInteractEvent.Action action = event.action;
        if (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            Block block = world.getBlock(event.x, event.y, event.z);
            ItemStack currentItem = player.getCurrentEquippedItem();
            if (currentItem != null && OreDictionary.itemMatches(new ItemStack(ItemLib.stoneMinium), currentItem, false) && block == ItemLib.ashBlock && event.face == 1) {
                if (structureFormed(world, event.x, event.y - 2, event.z) && playerHasDust(player, 3, 8)) {
                    SimpleCondenser.proxy.playSoundAtPlayer(player, "transmute", 1f, 1f);
                    if (!world.isRemote)
                        ItemUtil.dropItemStackIntoWorld(new ItemStack(ModBlocks.condenserBase), world, event.x, event.y - 2, event.z, true);
                    consumeDust(player, 3, 8);
                    consumeStructure(world, event.x, event.y - 2, event.z);
                }
            }
        }
    }

    private void consumeDust(EntityPlayer player, int dustMeta, int stackSize) {
        if (player.capabilities.isCreativeMode)
            return;
        int size = 0;
        ItemStack dustStack = new ItemStack(ItemLib.alchemicalDust, 1, dustMeta);
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (size >= stackSize)
                break;
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() != null && OreDictionary.itemMatches(dustStack, stack, false)) {
                int min = Math.min(stackSize - size, stack.stackSize);
                player.inventory.decrStackSize(i, min);
                size += min;
            }
        }
    }

    private boolean playerHasDust(EntityPlayer player, int dustMeta, int stackSize) {
        int size = 0;
        ItemStack dustStack = new ItemStack(ItemLib.alchemicalDust, 1, dustMeta);
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() != null && OreDictionary.itemMatches(dustStack, stack, false))
                size += stack.stackSize;
        }
        return size >= stackSize;
    }

    private boolean structureFormed(World world, int xCoord, int yCoord, int zCoord) {
        int i = 0;
        for (int x = xCoord - 1; x < xCoord + 2; x++)
            for (int y = yCoord; y < yCoord + 3; y++)
                for (int z = zCoord - 1; z < zCoord + 2; z++) {
                    if (!world.isAirBlock(x, y, z)) {
                        Block block = world.getBlock(x, y, z);
                        if (block == ItemLib.ashBlock)
                            i++;
                    }
                }
        return i > 25 && (world.getBlock(xCoord, yCoord + 1, zCoord) == ItemLib.alchemicalChest && world.getBlockMetadata(xCoord, yCoord + 1, zCoord) == 2);
    }

    private void consumeStructure(World world, int xCoord, int yCoord, int zCoord) {
        for (int x = xCoord - 1; x < xCoord + 2; x++)
            for (int y = yCoord; y < yCoord + 3; y++)
                for (int z = zCoord - 1; z < zCoord + 2; z++) {
                    if (!world.isAirBlock(x, y, z)) {
                        world.spawnParticle("explode", x, y, z, world.rand.nextDouble(), world.rand.nextDouble(), world.rand.nextDouble());
                        world.setBlockToAir(x, y, z);
                    }
                }
    }
}
