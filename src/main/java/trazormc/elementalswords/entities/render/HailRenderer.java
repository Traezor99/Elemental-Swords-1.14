package trazormc.elementalswords.entities.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.HailEntity;
import trazormc.elementalswords.entities.modals.HailModel;

public class HailRenderer extends EntityRenderer<HailEntity> {
	private ResourceLocation texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/hail.png");
	private HailModel model = new HailModel();
	
	public HailRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(HailEntity entity) {
		return texture;
	}
	
	@Override
	public void doRender(HailEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translatef((float)x, (float)y, (float)z);
		this.bindEntityTexture(entity);
		if(this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
		}

		this.model.render(1.0f);
		
		if(this.renderOutlines) {
			GlStateManager.tearDownSolidRenderingTextureCombine();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	public static class Factory implements IRenderFactory<HailEntity> {
		@Override
		public EntityRenderer<? super HailEntity> createRenderFor(EntityRendererManager manager) {
			return new HailRenderer(manager);
		}
	}

}
