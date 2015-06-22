package net.lomeli.simplecondenser.item;

import org.lwjgl.input.Keyboard;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.blocks.ModBlocks;
import net.lomeli.simplecondenser.lib.enums.AlchemicalType;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class ItemCondenserUpgrade extends ItemSC {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public ItemCondenserUpgrade() {
        super();
        this.setUnlocalizedName("condenserUpgrade");
        this.setHasSubtypes(true);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        AlchemicalType type = AlchemicalType.getType(stack.getItemDamage());
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileCondenserBase) {
            if (!world.isRemote) {
                TileCondenserBase condenser = (TileCondenserBase) tile;
                AlchemicalType condenserType = condenser.getType();
                if (type.getIndex() == condenserType.getIndex() + 1 || type.getIndex() == condenserType.getIndex() - 1) {
                    NBTTagCompound tag = new NBTTagCompound();
                    condenser.writeToNBT(tag);
                    condenser.readFromNBT(new NBTTagCompound());
                    tag.setInteger("CondenserType", type.getIndex());
                    world.setBlockToAir(x, y, z);
                    world.setBlock(x, y, z, type.getIndex() == 1 ? ModBlocks.azureCondenser : type.getIndex() == 2 ? ModBlocks.miniumCondenser : ModBlocks.verdantCondenser, 0, 2);
                    TileCondenserBase newCondenser = (TileCondenserBase) world.getTileEntity(x, y, z);
                    newCondenser.readFromNBT(tag);
                    if (!player.capabilities.isCreativeMode)
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    return true;
                }
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[3];
        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(SimpleCondenser.MOD_ID + ":condenserUpgrade" + AlchemicalType.getType(i).getName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta < icons.length ? icons[meta] : icons[0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean something) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            list.add(StatCollector.translateToLocal("item.simplecondenser.condenserUpgrade." + AlchemicalType.getType(stack.getItemDamage()).getName() + ".desc"));
        else
            list.add(EnumChatFormatting.GREEN + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("text.simplecondenser.info") + EnumChatFormatting.RESET);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < icons.length; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "." + AlchemicalType.getType(stack.getItemDamage()).getName();
    }
}
