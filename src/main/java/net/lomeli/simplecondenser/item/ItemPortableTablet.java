package net.lomeli.simplecondenser.item;

import org.lwjgl.input.Keyboard;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.MathHelper;

import net.lomeli.simplecondenser.SimpleCondenser;

import com.pahimar.ee3.api.exchange.EnergyValue;

public class ItemPortableTablet extends ItemSC {
    public static final int TABLE_SIZE = 10;

    public ItemPortableTablet() {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName("portableTablet");
        this.setTextureName("portableTransmutationTablet");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        openTable(player, world, MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ));
        return super.onItemRightClick(stack, world, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(stack, player, list, flag);
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            String text = StatCollector.translateToLocal("text.simplecondenser.recipe.tablet");
            String[] breakedLines = text.split("/n");
            if (breakedLines != null && breakedLines.length > 0) {
                for (String st : breakedLines)
                    list.add(st);
            }
        } else
            list.add(EnumChatFormatting.GREEN + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("text.simplecondenser.info") + EnumChatFormatting.RESET);
    }

    private boolean openTable(EntityPlayer player, World world, int x, int y, int z) {
        if (!world.isRemote && !player.isSneaking()) {
            player.openGui(SimpleCondenser.modInstance, 0, world, x, y, z);
            return true;
        }
        return false;
    }

    public static ItemStack setTabletEnergy(ItemStack stack, EnergyValue value) {
        if (stack != null && stack.getItem() instanceof ItemPortableTablet) {
            NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            if (value != null && value.getValue() > 0f) {
                NBTTagCompound energy = new NBTTagCompound();
                value.writeToNBT(energy);
                tag.setTag("storedEnergyValue", energy);
                stack.setTagCompound(tag);
            }
        }
        return stack;
    }

    public static EnergyValue getTabletEnergy(ItemStack stack) {
        EnergyValue value = new EnergyValue(0f);
        if (stack != null && stack.hasTagCompound() && stack.getItem() instanceof ItemPortableTablet) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("storedEnergyValue", 10)) {
                NBTTagCompound energyTag = tag.getCompoundTag("storedEnergyValue");
                if (!energyTag.hasNoTags())
                    value = EnergyValue.loadEnergyValueFromNBT(energyTag);
            }
        }
        return value;
    }
}
