package trazormc.elementalswords.entities.modals;

import trazormc.elementalswords.entities.WindSeekerEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

import com.mojang.blaze3d.platform.GlStateManager;

/**
 * WindSeekerModel - TrazorMC
 * Created using Tabula 7.1.0
 */
public class WindSeekerModel extends EntityModel<WindSeekerEntity> {
    public RendererModel shape1;
    public RendererModel shape2;
    public RendererModel shape3;
    public RendererModel shape4;
    public RendererModel shape5;
    public RendererModel shape6;
    public RendererModel shape7;

    public WindSeekerModel() {
        this.textureWidth = 16;
        this.textureHeight = 8;
        this.shape2 = new RendererModel(this, 4, 0);
        this.shape2.setRotationPoint(0.025F, -0.025F, 0.025F);
        this.shape2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.shape3 = new RendererModel(this, 8, 0);
        this.shape3.setRotationPoint(0.5F, 0.025F, 0.025F);
        this.shape3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.shape1 = new RendererModel(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.shape4 = new RendererModel(this, 11, 1);
        this.shape4.setRotationPoint(0.025F, 0.5F, 0.025F);
        this.shape4.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.shape6 = new RendererModel(this, 4, 2);
        this.shape6.setRotationPoint(0.025F, 0.025F, -0.025F);
        this.shape6.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.shape7 = new RendererModel(this, 8, 2);
        this.shape7.setRotationPoint(-0.025F, 0.025F, 0.025F);
        this.shape7.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.shape5 = new RendererModel(this, 0, 2);
        this.shape5.setRotationPoint(0.025F, 0.025F, 0.5F);
        this.shape5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
    }

    @Override
    public void render(WindSeekerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.shape2.offsetX, this.shape2.offsetY, this.shape2.offsetZ);
        GlStateManager.translatef(this.shape2.rotationPointX * scale, this.shape2.rotationPointY * scale, this.shape2.rotationPointZ * scale);
        GlStateManager.scalef(0.45f, 0.025f, 0.45f);
        GlStateManager.translatef(-this.shape2.offsetX, -this.shape2.offsetY, -this.shape2.offsetZ);
        GlStateManager.translatef(-this.shape2.rotationPointX * scale, -this.shape2.rotationPointY * scale, -this.shape2.rotationPointZ * scale);
        this.shape2.render(scale);
        GlStateManager.popMatrix();
        
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.shape3.offsetX, this.shape3.offsetY, this.shape3.offsetZ);
        GlStateManager.translatef(this.shape3.rotationPointX * scale, this.shape3.rotationPointY * scale, this.shape3.rotationPointZ * scale);
        GlStateManager.scalef(0.025f, 0.45f, 0.45f);
        GlStateManager.translatef(-this.shape3.offsetX, -this.shape3.offsetY, -this.shape3.offsetZ);
        GlStateManager.translatef(-this.shape3.rotationPointX * scale, -this.shape3.rotationPointY * scale, -this.shape3.rotationPointZ * scale);
        this.shape3.render(scale);
        GlStateManager.popMatrix();
        
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.shape1.offsetX, this.shape1.offsetY, this.shape1.offsetZ);
        GlStateManager.translatef(this.shape1.rotationPointX * scale, this.shape1.rotationPointY * scale, this.shape1.rotationPointZ * scale);
        GlStateManager.scalef(0.5f, 0.5f, 0.5f);
        GlStateManager.translatef(-this.shape1.offsetX, -this.shape1.offsetY, -this.shape1.offsetZ);
        GlStateManager.translatef(-this.shape1.rotationPointX * scale, -this.shape1.rotationPointY * scale, -this.shape1.rotationPointZ * scale);
        this.shape1.render(scale);
        GlStateManager.popMatrix();
        
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.shape4.offsetX, this.shape4.offsetY, this.shape4.offsetZ);
        GlStateManager.translatef(this.shape4.rotationPointX * scale, this.shape4.rotationPointY * scale, this.shape4.rotationPointZ * scale);
        GlStateManager.scalef(0.45f, 0.025f, 0.45f);
        GlStateManager.translatef(-this.shape4.offsetX, -this.shape4.offsetY, -this.shape4.offsetZ);
        GlStateManager.translatef(-this.shape4.rotationPointX * scale, -this.shape4.rotationPointY * scale, -this.shape4.rotationPointZ * scale);
        this.shape4.render(scale);
        GlStateManager.popMatrix();
        
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.shape6.offsetX, this.shape6.offsetY, this.shape6.offsetZ);
        GlStateManager.translatef(this.shape6.rotationPointX * scale, this.shape6.rotationPointY * scale, this.shape6.rotationPointZ * scale);
        GlStateManager.scalef(0.45f, 0.45f, 0.025f);
        GlStateManager.translatef(-this.shape6.offsetX, -this.shape6.offsetY, -this.shape6.offsetZ);
        GlStateManager.translatef(-this.shape6.rotationPointX * scale, -this.shape6.rotationPointY * scale, -this.shape6.rotationPointZ * scale);
        this.shape6.render(scale);
        GlStateManager.popMatrix();
        
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.shape7.offsetX, this.shape7.offsetY, this.shape7.offsetZ);
        GlStateManager.translatef(this.shape7.rotationPointX * scale, this.shape7.rotationPointY * scale, this.shape7.rotationPointZ * scale);
        GlStateManager.scalef(0.025f, 0.45f, 0.45f);
        GlStateManager.translatef(-this.shape7.offsetX, -this.shape7.offsetY, -this.shape7.offsetZ);
        GlStateManager.translatef(-this.shape7.rotationPointX * scale, -this.shape7.rotationPointY * scale, -this.shape7.rotationPointZ * scale);
        this.shape7.render(scale);
        GlStateManager.popMatrix();
        
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.shape5.offsetX, this.shape5.offsetY, this.shape5.offsetZ);
        GlStateManager.translatef(this.shape5.rotationPointX * scale, this.shape5.rotationPointY * scale, this.shape5.rotationPointZ * scale);
        GlStateManager.scalef(0.45f, 0.45f, 0.025f);
        GlStateManager.translatef(-this.shape5.offsetX, -this.shape5.offsetY, -this.shape5.offsetZ);
        GlStateManager.translatef(-this.shape5.rotationPointX * scale, -this.shape5.rotationPointY * scale, -this.shape5.rotationPointZ * scale);
        this.shape5.render(scale);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
