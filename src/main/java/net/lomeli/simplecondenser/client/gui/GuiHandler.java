package net.lomeli.simplecondenser.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

import net.lomeli.simplecondenser.inventory.ContainerCondenser;
import net.lomeli.simplecondenser.inventory.ContainerPortableTablet;
import net.lomeli.simplecondenser.inventory.InventoryPortableTablet;
import net.lomeli.simplecondenser.item.ItemPortableTablet;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == -1) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity != null) {
                if (tileEntity instanceof TileCondenserBase)
                    return new ContainerCondenser((TileCondenserBase) tileEntity, player.inventory);
            }
        } else if (ID == 0) {
            InventoryPortableTablet tabletInventory = new InventoryPortableTablet(world, ItemPortableTablet.TABLE_SIZE, player.getCurrentEquippedItem());
            tabletInventory.tickInventory();
            return new ContainerPortableTablet(player, tabletInventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == -1) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity != null) {
                if (tileEntity instanceof TileCondenserBase)
                    return new GuiCondenser((TileCondenserBase) tileEntity, player.inventory);
            }
        } else if (ID == 0) {
            InventoryPortableTablet tabletInventory = new InventoryPortableTablet(world, ItemPortableTablet.TABLE_SIZE, player.getCurrentEquippedItem());
            tabletInventory.tickInventory();
            return new GuiPortableTablet(player, tabletInventory);
        }
        return null;
    }
}
