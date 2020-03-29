package trazormc.elementalswords.entities.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.WindSeekerEntity;

@OnlyIn(Dist.CLIENT)
public class WindSeekerRenderer extends EntityRenderer<WindSeekerEntity> {
	private ResourceLocation texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/wind_seeker.png");
	private GenericHeadModel model = new GenericHeadModel();

	public WindSeekerRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(WindSeekerEntity entity) {
		return texture;
	}

	private float getRenderYaw(float p_82400_1_, float p_82400_2_, float p_82400_3_) {
		float f;
		for(f = p_82400_2_ - p_82400_1_; f < -180.0F; f += 360.0F) {
			;
		}

		while(f >= 180.0F) {
			f -= 360.0F;
		}

		return p_82400_1_ + p_82400_3_ * f;
	}
	
	@Override
	public void doRender(WindSeekerEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		float f = this.getRenderYaw(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
		float f1 = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
		GlStateManager.translatef((float)x, (float)y, (float)z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
		GlStateManager.enableAlphaTest();
		this.bindEntityTexture(entity);
		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
		}

		this.model.func_217104_a(0.0F, 0.0F, 0.0F, f, f1, 0.0625F);
		if (this.renderOutlines) {
			GlStateManager.tearDownSolidRenderingTextureCombine();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	public static class Factory implements IRenderFactory<WindSeekerEntity> {
		@Override
		public EntityRenderer<? super WindSeekerEntity> createRenderFor(EntityRendererManager manager) {
			return new WindSeekerRenderer(manager);
		}
	}

}
