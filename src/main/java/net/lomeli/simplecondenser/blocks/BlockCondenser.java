package net.lomeli.simplecondenser.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.item.ItemGreatStar;
import net.lomeli.simplecondenser.item.ModItems;
import net.lomeli.simplecondenser.lib.ModRenderIDs;
import net.lomeli.simplecondenser.lib.enums.AlchemicalType;
import net.lomeli.simplecondenser.lib.enums.RedstoneState;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

import com.pahimar.ee3.api.exchange.EnergyValue;

public class BlockCondenser extends BlockSC implements ITileEntityProvider {
    private AlchemicalType type;

    public BlockCondenser(AlchemicalType type) {
        super(Material.rock);
        this.setHardness(2.5f);
        this.setBlockTextureName(type.getName() + "Top");
        this.type = type;
    }

    public AlchemicalType getType() {
        return type;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int sideHit, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileCondenserBase) {
            TileCondenserBase condenser = (TileCondenserBase) tile;
            ItemStack stack = player.getCurrentEquippedItem();
            if (!player.isSneaking()) {
                if (stack != null) {
                    if (stack.getItem() == ModItems.greatStar) {
                        if (!world.isRemote) {
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
                        }
                        return true;
                    } else if (stack.getItem() == Item.getItemFromBlock(Blocks.redstone_torch)) {
                        if (!world.isRemote) {
                            RedstoneState state = RedstoneState.getStateFromType(condenser.getRedstoneState().getType() + 1);
                            condenser.setRedstoneState(state);
                            player.addChatComponentMessage(new ChatComponentTranslation(state.getUnlocal()));
                        }
                        return true;
                    } else {
                        if (!world.isRemote)
                            player.openGui(SimpleCondenser.modInstance, -1, world, x, y, z);
                        return true;
                    }
                } else {
                    if (!world.isRemote)
                        player.openGui(SimpleCondenser.modInstance, -1, world, x, y, z);
                    return true;
                }
            }
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
