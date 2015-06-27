package net.lomeli.simplecondenser.item;

import org.lwjgl.input.Keyboard;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemChargedStar extends ItemSC {

    public ItemChargedStar() {
        super();
        this.setTextureName("chargedAlchemicStorage");
        this.setUnlocalizedName("chargedStar");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(stack, player, list, flag);
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            String text = StatCollector.translateToLocal("text.simplecondenser.recipe.chargedStar");
            String[] breakedLines = text.split("/n");
            if (breakedLines != null && breakedLines.length > 0) {
                for (String st : breakedLines)
                    list.add(st);
            }
        } else
            list.add(EnumChatFormatting.GREEN + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("text.simplecondenser.info") + EnumChatFormatting.RESET);
    }
}
