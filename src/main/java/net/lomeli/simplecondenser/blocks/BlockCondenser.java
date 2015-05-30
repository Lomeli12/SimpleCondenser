package net.lomeli.simplecondenser.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.ItemUtil;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.item.ItemGreatStar;
import net.lomeli.simplecondenser.item.ModItems;
import net.lomeli.simplecondenser.lib.EnumAlchemicalType;
import net.lomeli.simplecondenser.lib.ModRenderIDs;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

import com.pahimar.ee3.api.exchange.EnergyValue;

public class BlockCondenser extends BlockSC implements ITileEntityProvider {
    @SideOnly(Side.CLIENT)
    private IIcon ash, circle;
    private EnumAlchemicalType type;

    public BlockCondenser(EnumAlchemicalType type) {
        super(Material.rock);
        this.setHardness(2.5f);
        this.type = type;
    }

    public EnumAlchemicalType getType() {
        return type;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        ash = register.registerIcon(SimpleCondenser.MOD_ID + ":condenserSide");
        circle = register.registerIcon(SimpleCondenser.MOD_ID + ":" + type.getName() + "Top");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? circle : ash;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int sideHit, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            if (!world.isRemote) {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileCondenserBase) {
                    TileCondenserBase condenser = (TileCondenserBase) tile;
                    ItemStack stack = player.getCurrentEquippedItem();
                    if (stack != null) {
                        if (stack.getItem() == Items.nether_star && condenser.getStoredEnergyValue().getValue() > 0) {
                            ItemStack star = new ItemStack(ModItems.greatStar);
                            ItemGreatStar.setStackEMC(star, condenser.getStoredEnergyValue());
                            condenser.setStoredEnergy(0f);
                            ItemUtil.dropItemStackIntoWorld(star, world, x, y, z, false);
                            if (!player.capabilities.isCreativeMode) {
                                stack.stackSize--;
                                if (stack.stackSize <= 0)
                                    stack = null;
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
                            }
                            return true;
                        } else if (stack.getItem() == ModItems.greatStar) {
                            EnergyValue value = ItemGreatStar.getStackEMC(stack);
                            condenser.setStoredEnergy(condenser.getStoredEnergyValue().getValue() + value.getValue());
                            ItemUtil.dropItemStackIntoWorld(new ItemStack(Items.nether_star), world, x, y, z, false);
                            if (!player.capabilities.isCreativeMode) {
                                stack.stackSize--;
                                if (stack.stackSize <= 0)
                                    stack = null;
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
                            }
                            return true;
                        }
                    }
                    player.openGui(SimpleCondenser.modInstance, -1, world, x, y, z);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileCondenserBase(type);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        if (stack.hasDisplayName())
            ((TileCondenserBase) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
    }

    @Override
    public int getRenderType() {
        return ModRenderIDs.condenserRenderID;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}
