package net.lomeli.simplecondenser.item;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.lomeli.lomlib.util.NBTUtil;

import com.pahimar.ee3.api.exchange.EnergyValue;

public class ItemGreatStar extends ItemSC {
    public static final String EMC_TAG = "stack_emc";
    private static DecimalFormat energyValueDecimalFormat = new DecimalFormat("###,###,###,###,###.###");

    public ItemGreatStar() {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName("greatStar");
        this.setTextureName("greatStar");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(stack, player, list, flag);
        EnergyValue value = getStackEMC(stack);
        if (value.getValue() > 0)
            list.add("EMC: " + energyValueDecimalFormat.format(value.getValue()));
    }

    public static void setStackEMC(ItemStack stack, EnergyValue value) {
        if (stack == null)
            return;
        NBTTagCompound emcTag = new NBTTagCompound();
        value.writeToNBT(emcTag);
        NBTUtil.setTagCompound(stack, EMC_TAG, emcTag);
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
