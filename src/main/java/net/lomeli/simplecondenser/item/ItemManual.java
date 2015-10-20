package net.lomeli.simplecondenser.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.lomeli.lomlib.util.MathHelper;

import net.lomeli.simplecondenser.SimpleCondenser;

public class ItemManual extends ItemSC {

    public ItemManual() {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName("manual");
        this.setTextureName("manual");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!player.isSneaking())
            player.openGui(SimpleCondenser.modInstance, 1, world, MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ));
        return super.onItemRightClick(stack, world, player);
    }
}
