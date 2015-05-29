package net.lomeli.simplecondenser.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class BlockCondenser extends BlockSC implements ITileEntityProvider {
    @SideOnly(Side.CLIENT)
    private IIcon ash, circle;
    private long speed;
    private String type;

    public BlockCondenser(long speed, String type) {
        super(Material.rock);
        this.setHardness(2.5f);
        this.speed = speed;
        this.type = type;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        ash = register.registerIcon(SimpleCondenser.MOD_ID + ":condenserSide");
        circle = register.registerIcon(SimpleCondenser.MOD_ID + ":" + type + "Top");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? circle : ash;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int sideHit, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            if (!world.isRemote)
                player.openGui(SimpleCondenser.modInstance, -1, world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileCondenserBase(speed, type);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        if (stack.hasDisplayName())
            ((TileCondenserBase) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
    }
}
