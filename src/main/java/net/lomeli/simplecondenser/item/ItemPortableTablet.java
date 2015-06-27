package net.lomeli.simplecondenser.item;

import org.lwjgl.input.Keyboard;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.MathHelper;
import net.lomeli.lomlib.util.entity.EntityUtil;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.inventory.InventoryPortableTablet;

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

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int pos, boolean equipped) {
        if (equipped) {
            InventoryPortableTablet table = new InventoryPortableTablet(world, TABLE_SIZE, stack);
            table.tickInventory();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(stack, player, list, flag);
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(StatCollector.translateToLocal("text.simplecondenser.tablet.chargedStar.0"));
            list.add(StatCollector.translateToLocal("text.simplecondenser.tablet.chargedStar.1"));
        } else
            list.add(EnumChatFormatting.GREEN + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("text.simplecondenser.info") + EnumChatFormatting.RESET);
    }

    private boolean openTable(EntityPlayer player, World world, int x, int y, int z) {
        if (!world.isRemote && !EntityUtil.isFakePlayer(player) && !player.isSneaking()) {
            player.openGui(SimpleCondenser.modInstance, 0, world, x, y, z);
            return true;
        }
        return false;
    }
}
