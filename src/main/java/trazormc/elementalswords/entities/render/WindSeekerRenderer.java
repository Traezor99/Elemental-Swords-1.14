package trazormc.elementalswords.entities.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.WindSeekerEntity;
import trazormc.elementalswords.entities.modals.WindSeekerModel;

public class WindSeekerRenderer extends EntityRenderer<WindSeekerEntity> {
	private ResourceLocation texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/wind_seeker.png");
	private WindSeekerModel model = new WindSeekerModel();

	public WindSeekerRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(WindSeekerEntity entity) {
		return texture;
	}
	
	//Maybe remove this method
	private float getRenderYaw(float prevYaw, float yaw, float partialTicks) {
	      float f;
	      for(f = yaw - prevYaw; f < -180.0F; f += 360.0F) {
	         ;
	      }

	      while(f >= 180.0F) {
	         f -= 360.0F;
	      }

	      return prevYaw + partialTicks * f;
	   }

	@Override //Still does weird stuff with rotation vertically
	public void doRender(WindSeekerEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translatef((float)x, (float)y, (float)z);
		this.bindEntityTexture(entity);
		if(this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
		}

		this.model.render(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch), getRenderYaw(entity.prevRotationYaw, entity.rotationYaw, partialTicks), 180.0f, 1.0f);
		
		if(this.renderOutlines) {
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
