package trazormc.elementalswords.entities.modals;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

import com.mojang.blaze3d.platform.GlStateManager;

/**
 * WindSeekerModel - TrazorMC
 * Created using Tabula 7.1.0
 */
public class WindSeekerModel extends Model {
    public float[] modelScale = new float[] {40.0f, 40.0f, 40.0f};
    public RendererModel center;
    public RendererModel top;
    public RendererModel right;
    public RendererModel bottom;
    public RendererModel back;
    public RendererModel front;
    public RendererModel left;

    public WindSeekerModel() {
        this.textureWidth = 800;
        this.textureHeight = 100;
        this.back = new RendererModel(this, 252, 0);
        this.back.setRotationPoint(0.0F, 0.0F, 10.5F);
        this.back.addBox(-9.5F, -9.5F, -0.5F, 19, 19, 1, 0.0F);
        this.bottom = new RendererModel(this, 176, 0);
        this.bottom.setRotationPoint(0.0F, 10.5F, 0.0F);
        this.bottom.addBox(-9.5F, -0.5F, -9.5F, 19, 1, 19, 0.0F);
        this.front = new RendererModel(this, 292, 0);
        this.front.setRotationPoint(0.0F, 0.0F, -10.5F);
        this.front.addBox(-9.5F, -9.5F, -0.5F, 19, 19, 1, 0.0F);
        this.center = new RendererModel(this, 0, 0);
        this.center.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.center.addBox(-10.0F, -10.0F, -10.0F, 20, 20, 20, 0.0F);
        this.right = new RendererModel(this, 136, 0);
        this.right.setRotationPoint(10.5F, 0.0F, 0.0F);
        this.right.addBox(-0.5F, -9.5F, -9.5F, 1, 19, 19, 0.0F);
        this.left = new RendererModel(this, 332, 0);
        this.left.setRotationPoint(-10.5F, 0.0F, 0.0F);
        this.left.addBox(-0.5F, -9.5F, -9.5F, 1, 19, 19, 0.0F);
        this.top = new RendererModel(this, 60, 0);
        this.top.setRotationPoint(0.0F, -10.5F, 0.0F);
        this.top.addBox(-9.5F, -0.5F, -9.5F, 19, 1, 19, 0.0F);
        this.center.addChild(this.back);
        this.center.addChild(this.bottom);
        this.center.addChild(this.front);
        this.center.addChild(this.right);
        this.center.addChild(this.left);
        this.center.addChild(this.top);
    }

    public void render(float x, float y, float z, float scale) { 
    	this.setRotateAngle(center, x, y, z);
        GlStateManager.pushMatrix();
        GlStateManager.scalef(1f / modelScale[0], 1f / modelScale[1], 1f / modelScale[2]);
        this.center.render(scale);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x * (float)(Math.PI / 180);
        modelRenderer.rotateAngleY = y * (float)(Math.PI / 180);
        modelRenderer.rotateAngleZ = z * (float)(Math.PI / 180);
    }
}
