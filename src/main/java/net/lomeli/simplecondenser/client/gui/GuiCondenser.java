package net.lomeli.simplecondenser.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.inv.ContainerCondenser;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class GuiCondenser extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation(SimpleCondenser.MOD_ID + ":textures/gui/condenser.png");
    private static DecimalFormat energyValueDecimalFormat = new DecimalFormat("###,###,###,###,###.###");
    private TileCondenserBase tile;

    public GuiCondenser(TileCondenserBase tile, InventoryPlayer player) {
        super(new ContainerCondenser(tile, player));
        this.tile = tile;
        short short1 = 222;
        int i = short1 - 108;
        this.ySize = i + 6 * 18;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.fontRendererObj.drawString(I18n.format(tile.getInventoryName(), new Object[0]), 8, 6, 0xB0B0B0);
        String unlocalized = "text.simplecondenser.exchangeEnergy.short";
        int xPos = 40, yPos = 29;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            xPos = 8;
            yPos = 128;
            unlocalized = "text.simplecondenser.exchangeEnergy";
        }
        String localized = StatCollector.translateToLocal(unlocalized);//unlocalized, new Object[0]);
        String formated = energyValueDecimalFormat.format(((ContainerCondenser) this.inventorySlots).getEnergy());
        this.fontRendererObj.drawString(String.format(localized, formated), xPos, yPos, 0xFFFFFF);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float opacity, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, 6 * 18 + 17);
        this.drawTexturedModalRect(k, l + 6 * 18 + 17, 0, 126, this.xSize, 96);
    }
}
