package net.lomeli.simplecondenser.item;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.ItemUtil;
import net.lomeli.lomlib.util.MathHelper;
import net.lomeli.lomlib.util.NBTUtil;
import net.lomeli.lomlib.util.ObfUtil;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.lib.ItemLib;

import com.pahimar.ee3.api.exchange.EnergyValue;

public class ItemGreatStar extends ItemSC {
    public static final String EMC_TAG = "stack_emc";
    private static DecimalFormat energyValueDecimalFormat = new DecimalFormat("###,###,###,###,###.###");

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public ItemGreatStar() {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName("greatStar");
        this.setTextureName("greatStar");
    }

    public static void setStackEMC(ItemStack stack, EnergyValue value) {
        if (stack == null)
            return;
        NBTTagCompound emcTag = new NBTTagCompound();
        value.writeToNBT(emcTag);
        NBTUtil.setTagCompound(stack, EMC_TAG, emcTag);
        int meta = value.getValue() > 1200000f ? 5 : MathHelper.floor(value.getValue() / 200000f);
        stack.setItemDamage(meta);
    }

    public static EnergyValue getStackEMC(ItemStack stack) {
        EnergyValue value = new EnergyValue(0);

        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound emcTag = NBTUtil.getTagCompound(stack, EMC_TAG);
            value.readFromNBT(emcTag);
        }
        return value;
    }

    private static void consumeDust(EntityPlayer player, int dustMeta, int stackSize) {
        if (player.capabilities.isCreativeMode)
            return;
        int size = 0;
        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
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

    private static boolean playerHasDust(EntityPlayer player, int dustMeta, int stackSize) {
        int size = 0;
        ItemStack dustStack = new ItemStack(ItemLib.alchemicalDust, 1, dustMeta);
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() != null && OreDictionary.itemMatches(dustStack, stack, false))
                size += stack.stackSize;
        }
        return size >= stackSize || player.capabilities.isCreativeMode;
    }

    private static boolean structureFormed(World world, int xCoord, int yCoord, int zCoord, int side) {
        int i = 0;
        xCoord += xChange(side);
        yCoord += yChange(side);
        zCoord += zChange(side);
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

    private static void consumeStructure(World world, int xCoord, int yCoord, int zCoord, int side) {
        xCoord += xChange(side);
        yCoord += yChange(side);
        zCoord += zChange(side);
        for (int x = xCoord - 1; x < xCoord + 2; x++)
            for (int y = yCoord; y < yCoord + 3; y++)
                for (int z = zCoord - 1; z < zCoord + 2; z++) {
                    if (!world.isAirBlock(x, y, z)) {
                        world.spawnParticle("explode", x, y, z, world.rand.nextDouble(), world.rand.nextDouble(), world.rand.nextDouble());
                        world.setBlockToAir(x, y, z);
                    }
                }
    }

    private static boolean isTransmutationTablet(TileEntity tile) {
        try {
            Class<?> clazz = Class.forName("com.pahimar.ee3.tileentity.TileEntityTransmutationTablet");
            return clazz.isInstance(tile);
        } catch (Exception e) {
        }
        return false;
    }

    private static EnergyValue getTabletEnergyValue(Object obj) {
        try {
            Class<?> clazz = Class.forName("com.pahimar.ee3.tileentity.TileEntityTransmutationTablet");
            EnergyValue value = ObfUtil.getFieldValue(clazz, obj, "storedEnergyValue");
            return value != null ? value : new EnergyValue(0);
        } catch (Exception e) {
        }
        return new EnergyValue(0);
    }

    private static void setTabletEnergyValue(EnergyValue value, Object obj) {
        try {
            Class<?> clazz = Class.forName("com.pahimar.ee3.tileentity.TileEntityTransmutationTablet");
            ObfUtil.setFieldValue(clazz, obj, value, "storedEnergyValue");
            ObfUtil.invokeMethod(clazz, obj, new String[]{"updateEnergyValueFromInventory"});
        } catch (Exception e) {
        }
    }

    private static int xChange(int side) {
        switch (side) {
            case 4:
                return 1;
            case 5:
                return -1;
            default:
                return 0;
        }
    }

    private static int yChange(int side) {
        switch (side) {
            case 0:
                return 0;
            case 1:
                return -2;
            default:
                return -1;
        }
    }

    private static int zChange(int side) {
        switch (side) {
            case 2:
                return 1;
            case 3:
                return -1;
            default:
                return 0;
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(x, y, z);
            Block block = world.getBlock(x, y, z);
            if (block == ItemLib.ashBlock) {
                if (structureFormed(world, x, y, z, side) && playerHasDust(player, 3, 8) && getStackEMC(stack).getValue() <= 0f) {
                    SimpleCondenser.proxy.playSoundAtPlayer(player, "transmute", 1f, 1f);
                    ItemUtil.dropItemStackIntoWorld(new ItemStack(ModItems.chargedStar), world, x, y - 2, z, true);
                    consumeDust(player, 3, 8);
                    if (!player.capabilities.isCreativeMode)
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    consumeStructure(world, x, y, z, side);
                    return true;
                }
            } else if (tile != null && isTransmutationTablet(tile)) {
                if (player.isSneaking()) {
                    for (int i = -1; i <= 1; i++)
                        for (int k = -1; k <= 1; k++) {
                            world.setBlockToAir(x + i, y, z + k);
                            world.spawnParticle("explode", x + i, y, z + k, world.rand.nextDouble(), world.rand.nextDouble(), world.rand.nextDouble());
                        }
                    SimpleCondenser.proxy.playSoundAtPlayer(player, "transmute", 1f, 1f);
                    ItemStack tablet = new ItemStack(ModItems.portableTablet);
                    if (!player.capabilities.isCreativeMode)
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, tablet);
                    else
                        player.inventory.addItemStackToInventory(tablet);
                } else {
                    EnergyValue value = getStackEMC(stack);
                    EnergyValue tabletValue = getTabletEnergyValue(tile);
                    if (value.getValue() > 0f) {
                        value = new EnergyValue(value.getValue() + tabletValue.getValue());
                        setTabletEnergyValue(value, tile);
                        setStackEMC(stack, new EnergyValue(0));
                    } else {
                        value = tabletValue;
                        setTabletEnergyValue(new EnergyValue(0), tile);
                        setStackEMC(stack, value);
                    }
                    world.markBlockForUpdate(x, y, z);
                }
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[6];
        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(SimpleCondenser.MOD_ID + ":alchemicStorage" + i);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta < icons.length ? icons[meta] : icons[0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(stack, player, list, flag);
        EnergyValue value = getStackEMC(stack);
        if (value.getValue() > 0)
            list.add(String.format(StatCollector.translateToLocal("text.simplecondenser.storedemc"), energyValueDecimalFormat.format(value.getValue())));
    }
}
