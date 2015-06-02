package net.lomeli.simplecondenser.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.client.IItemRenderer;

import net.lomeli.lomlib.util.RenderUtils;
import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.simplecondenser.SimpleCondenser;
import net.lomeli.simplecondenser.blocks.BlockCondenser;
import net.lomeli.simplecondenser.client.handler.TickHandlerClient;
import net.lomeli.simplecondenser.client.model.ModelAlchemicCondenser;
import net.lomeli.simplecondenser.lib.EnumAlchemicalType;
import net.lomeli.simplecondenser.lib.ItemLib;
import net.lomeli.simplecondenser.tile.TileCondenserBase;

public class RenderAlchemicalCondenser extends TileEntitySpecialRenderer implements IItemRenderer {
    private final ModelAlchemicCondenser model = new ModelAlchemicCondenser();
    private final float defaultSize = 0.0625F;
    private final RenderItem customRenderItem;

    public RenderAlchemicalCondenser() {
        customRenderItem = new RenderItem() {
            @Override
            public boolean shouldBob() {

                return false;
            }
        };
        customRenderItem.setRenderManager(RenderManager.instance);
    }

    // Tile Render

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float renderTick) {
        if (tile instanceof TileCondenserBase)
            renderCondenserTile((TileCondenserBase) tile, x, y, z, ((TileCondenserBase) tile).getType());
    }

    public void renderCondenserTile(TileCondenserBase tile, double x, double y, double z, EnumAlchemicalType type) {
        GL11.glPushMatrix();

        RenderUtils.bindTexture(ResourceUtil.getModelTexture(SimpleCondenser.MOD_ID, "AlchemicCondenser-" + type.getName() + ".png"));

        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glRotatef(180f, 0f, 0f, 1f);

        RenderUtils.applyColor(0xFFFFFF);
        model.render(defaultSize);
        RenderUtils.resetColor();

        GL11.glPopMatrix();

        ItemStack tome = tile.getStackInSlot(TileCondenserBase.TOME_SLOT);
        if (tome != null && ItemLib.isTome(tome) && Minecraft.getMinecraft().gameSettings.fancyGraphics) {
            GL11.glPushMatrix();
            EntityItem ghostEntityItem = new EntityItem(tile.getWorldObj());
            ghostEntityItem.hoverStart = 0.0F;
            ghostEntityItem.setEntityItemStack(tome);

            GL11.glTranslated(x + 0.6F, y + 0.97f, z + 0.4F);
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45F, 0.0F, 0.0F, 1.0F);

            customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
            GL11.glPopMatrix();

            ItemStack target = tile.getStackInSlot(TileCondenserBase.TARGET_SLOT);
            if (target != null) {
                GL11.glPushMatrix();
                ItemStack st = target.copy();
                st.stackSize = 1;
                EntityItem entityItem = new EntityItem(tile.getWorldObj());
                entityItem.hoverStart = 0.0F;
                entityItem.setEntityItemStack(st);

                float angle = TickHandlerClient.clientTicks % 720f;

                GL11.glTranslated(x + 0.5F, y + 1.1f, z + 0.5F);
                GL11.glRotatef(angle, 0, 1f, 0);

                customRenderItem.doRender(entityItem, 0, 0, 0, 0, 0);
                GL11.glPopMatrix();
            }
        }
    }

    //Item Render

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        BlockCondenser block = (BlockCondenser) Block.getBlockFromItem(item.getItem());
        switch (type) {
            case ENTITY: {
                renderCondenserItem(-1F, -1F, 0F, block.getType());
                return;
            }
            case EQUIPPED: {
                renderCondenserItem(-0.5F, -1.5F, 0.5F, block.getType());
                return;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderCondenserItem(-0.2F, -0.85F, 0.5F, block.getType());
                return;
            }
            case INVENTORY: {
                renderCondenserItem(-1.0F, -1.5F, 0.0F, block.getType());
                return;
            }
            default: {
            }
        }
    }

    public void renderCondenserItem(float x, float y, float z, EnumAlchemicalType type) {
        GL11.glPushMatrix();
        //GL11.glDisable(GL11.GL_LIGHTING);

        RenderUtils.bindTexture(ResourceUtil.getModelTexture(SimpleCondenser.MOD_ID, "AlchemicCondenser-" + type.getName() + ".png"));

        GL11.glTranslatef(x + 1F, y + 2.5F, z);
        GL11.glRotatef(180f, 0f, 0f, 1f);

        RenderUtils.applyColor(0xffffff);
        model.render(defaultSize);
        RenderUtils.resetColor();

        //GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
