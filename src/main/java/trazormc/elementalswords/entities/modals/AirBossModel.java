package trazormc.elementalswords.entities.modals;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import trazormc.elementalswords.entities.AirBossEntity;

public class AirBossModel extends BipedModel<AirBossEntity> {
	
	public RendererModel right_arm;
    public RendererModel right_leg;
    public RendererModel head;
    public RendererModel body;
    public RendererModel left_arm;
    public RendererModel left_leg;
    public RendererModel outer_head;

    public AirBossModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.right_leg = new RendererModel(this, 0, 16);
        this.right_leg.setRotationPoint(-1.899999976158142F, 12.0F, 0.10000000149011612F);
        this.right_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.head = new RendererModel(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.outer_head = new RendererModel(this, 32, 0);
        this.outer_head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.outer_head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.body = new RendererModel(this, 16, 16);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.right_arm = new RendererModel(this, 40, 16);
        this.right_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.right_arm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(right_arm, -0.5462880558742251F, -0.10000736613927509F, 0.10000736613927509F);
        this.left_arm = new RendererModel(this, 40, 16);
        this.left_arm.mirror = true;
        this.left_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.left_arm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(left_arm, -0.5462880558742251F, 0.10000736613927509F, -0.10000736613927509F);
        this.left_leg = new RendererModel(this, 0, 16);
        this.left_leg.mirror = true;
        this.left_leg.setRotationPoint(1.899999976158142F, 12.0F, 0.10000000149011612F);
        this.left_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
    }

    @Override
    public void render(AirBossEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
        this.right_leg.render(scale);
        this.head.render(scale);
        this.outer_head.render(scale);
        this.body.render(scale);
        this.right_arm.render(scale);
        this.left_arm.render(scale);
        this.left_leg.render(scale);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(AirBossEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    	this.head.rotateAngleX = headPitch * 0.017453292F;
    	
    	this.right_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.left_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    }

}
