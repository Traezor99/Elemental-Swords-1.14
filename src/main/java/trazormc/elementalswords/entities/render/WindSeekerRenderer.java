package trazormc.elementalswords.entities.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.WindSeekerEntity;
import trazormc.elementalswords.entities.modals.WindSeekerModel;

@OnlyIn(Dist.CLIENT)
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
	
	@Override
	public void doRender(WindSeekerEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translatef((float)x, (float)y, (float)z);
		this.bindEntityTexture(entity);
		if(this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
		}

		this.model.render(entity, 0f, 0f, 0f, 0f, 0f, 1.0f);
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
