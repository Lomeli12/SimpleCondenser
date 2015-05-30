package net.lomeli.simplecondenser.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        this.setBlockTextureName(type.getName() + "Top");
        this.type = type;
    }

    public EnumAlchemicalType getType() {
        return type;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int sideHit, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            if (!world.isRemote) {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileCondenserBase) {
                    TileCondenserBase condenser = (TileCondenserBase) tile;
                    ItemStack stack = player.getCurrentEquippedItem();
                    if (stack != null && stack.getItem() == ModItems.greatStar) {
                        EnergyValue value = ItemGreatStar.getStackEMC(stack);
                        if (value.getValue() == 0) {
                            if (condenser.getStoredEnergyValue().getValue() > 0) {
                                ItemGreatStar.setStackEMC(stack, condenser.getStoredEnergyValue());
                                condenser.setStoredEnergy(0f);
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
                            }
                        } else {
                            condenser.setStoredEnergy(condenser.getStoredEnergyValue().getValue() + value.getValue());
                            ItemGreatStar.setStackEMC(stack, new EnergyValue(0));
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(ModItems.greatStar));
                        }
                    } else
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
