package net.lomeli.simplecondenser.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * AlchemicCondenser - GustoniaEagle
 * Created using Tabula 4.1.1
 */
public class ModelAlchemicCondenser extends ModelBase {
    public ModelRenderer base;
    public ModelRenderer platformBottom;
    public ModelRenderer shaft;
    public ModelRenderer trayBase;
    public ModelRenderer platformTop;
    public ModelRenderer trayLeftSide;
    public ModelRenderer trayRightSide;
    public ModelRenderer trayFrontSide;
    public ModelRenderer trayBackSide;

    public ModelAlchemicCondenser() {
        this.textureWidth = 128;
        this.textureHeight = 32;
        this.trayLeftSide = new ModelRenderer(this, 34, 15);
        this.trayLeftSide.setRotationPoint(6.0F, 8.5F, -7.0F);
        this.trayLeftSide.addBox(0.0F, 0.0F, 0.0F, 1, 1, 14, 0.0F);
        this.trayBackSide = new ModelRenderer(this, 38, 30);
        this.trayBackSide.setRotationPoint(-6.0F, 8.5F, 6.0F);
        this.trayBackSide.addBox(0.0F, 0.0F, 0.0F, 12, 1, 1, 0.0F);
        this.shaft = new ModelRenderer(this, 0, 18);
        this.shaft.setRotationPoint(-1.0F, 11.0F, -1.0F);
        this.shaft.addBox(0.0F, 0.0F, 0.0F, 2, 11, 2, 0.0F);
        this.platformBottom = new ModelRenderer(this, 0, 23);
        this.platformBottom.setRotationPoint(-5.7F, 22.0F, 0.0F);
        this.platformBottom.addBox(0.0F, 0.0F, 0.0F, 8, 1, 8, 0.0F);
        this.setRotateAngle(platformBottom, 0.0F, 0.7853981633974483F, 0.0F);
        this.platformTop = new ModelRenderer(this, 0, 23);
        this.platformTop.setRotationPoint(-5.7F, 10.0F, 0.0F);
        this.platformTop.addBox(0.0F, 0.0F, 0.0F, 8, 1, 8, 0.0F);
        this.setRotateAngle(platformTop, 0.0F, 0.7853981633974483F, 0.0F);
        this.trayRightSide = new ModelRenderer(this, 34, 15);
        this.trayRightSide.setRotationPoint(-7.0F, 8.5F, -7.0F);
        this.trayRightSide.addBox(0.0F, 0.0F, 0.0F, 1, 1, 14, 0.0F);
        this.base = new ModelRenderer(this, 64, 15);
        this.base.setRotationPoint(-8.0F, 23.0F, -8.0F);
        this.base.addBox(0.0F, 0.0F, 0.0F, 16, 1, 16, 0.0F);
        this.trayBase = new ModelRenderer(this, 72, 0);
        this.trayBase.setRotationPoint(-7.0F, 10.0F, -7.0F);
        this.trayBase.addBox(0.0F, -1.0F, 0.0F, 14, 1, 14, 0.0F);
        this.trayFrontSide = new ModelRenderer(this, 38, 30);
        this.trayFrontSide.setRotationPoint(-6.0F, 8.5F, -7.0F);
        this.trayFrontSide.addBox(0.0F, 0.0F, 0.0F, 12, 1, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.trayLeftSide.render(f5);
        this.trayBackSide.render(f5);
        this.shaft.render(f5);
        this.platformBottom.render(f5);
        this.platformTop.render(f5);
        this.trayRightSide.render(f5);
        this.base.render(f5);
        GL11.glPushMatrix();
        GL11.glTranslatef(this.trayBase.offsetX, this.trayBase.offsetY, this.trayBase.offsetZ);
        GL11.glTranslatef(this.trayBase.rotationPointX * f5, this.trayBase.rotationPointY * f5, this.trayBase.rotationPointZ * f5);
        GL11.glScaled(1.0D, 0.5D, 1.0D);
        GL11.glTranslatef(-this.trayBase.offsetX, -this.trayBase.offsetY, -this.trayBase.offsetZ);
        GL11.glTranslatef(-this.trayBase.rotationPointX * f5, -this.trayBase.rotationPointY * f5, -this.trayBase.rotationPointZ * f5);
        this.trayBase.render(f5);
        GL11.glPopMatrix();
        this.trayFrontSide.render(f5);
    }

    public void render(float f0) {
        this.trayLeftSide.render(f0);
        this.trayBackSide.render(f0);
        this.shaft.render(f0);
        this.platformBottom.render(f0);
        this.platformTop.render(f0);
        this.trayRightSide.render(f0);
        this.base.render(f0);
        this.trayBase.render(f0);
        this.trayFrontSide.render(f0);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
