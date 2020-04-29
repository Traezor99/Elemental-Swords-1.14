package trazormc.elementalswords.entities.modals;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

import com.mojang.blaze3d.platform.GlStateManager;

/**
 * HailEntity - TrazorMC
 * Created using Tabula 7.1.0
 */
public class HailModel extends Model {
    public RendererModel shape1;

    public HailModel() {
        this.textureWidth = 8;
        this.textureHeight = 4;
        this.shape1 = new RendererModel(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
    }

    public void render(float scale) { 
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.shape1.offsetX, this.shape1.offsetY, this.shape1.offsetZ);
        GlStateManager.translatef(this.shape1.rotationPointX * scale, this.shape1.rotationPointY * scale, this.shape1.rotationPointZ * scale);
        GlStateManager.scalef(0.1f, 0.1f, 0.1f);
        GlStateManager.translatef(-this.shape1.offsetX, -this.shape1.offsetY, -this.shape1.offsetZ);
        GlStateManager.translatef(-this.shape1.rotationPointX * scale, -this.shape1.rotationPointY * scale, -this.shape1.rotationPointZ * scale);
        this.shape1.render(scale);
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
