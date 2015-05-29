package net.lomeli.simplecondenser.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

import net.lomeli.simplecondenser.inv.ContainerCondenser;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            if (tileEntity instanceof TileCondenserBase)
                return new ContainerCondenser((TileCondenserBase) tileEntity, player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            if (tileEntity instanceof TileCondenserBase)
                return new GuiCondenser((TileCondenserBase) tileEntity, player.inventory);
        }
        return null;
    }
}
