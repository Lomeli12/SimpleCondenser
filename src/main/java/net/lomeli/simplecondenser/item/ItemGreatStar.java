package net.lomeli.simplecondenser.item;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.MathHelper;
import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.simplecondenser.SimpleCondenser;

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

    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[6];
        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(SimpleCondenser.MOD_ID + ":alchemicStorage" + i);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta < icons.length ? icons[meta] : icons[0];
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(stack, player, list, flag);
        EnergyValue value = getStackEMC(stack);
        if (value.getValue() > 0)
            list.add(String.format(StatCollector.translateToLocal("text.simplecondenser.storedemc"), energyValueDecimalFormat.format(value.getValue())));
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
}
