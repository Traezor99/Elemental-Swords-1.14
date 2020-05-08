package trazormc.elementalswords.entities.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.WaterBossEntity;
import trazormc.elementalswords.entities.modals.WaterBossModel;

public class WaterBossRenderer extends MobRenderer<WaterBossEntity, WaterBossModel> {
	
	protected ResourceLocation texture;

	public WaterBossRenderer(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new WaterBossModel(), 0.5f);
		texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/water_boss.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(WaterBossEntity entity) {
		return texture;
	}
	
	public static class Factory implements IRenderFactory<WaterBossEntity> {

		@Override
		public EntityRenderer<? super WaterBossEntity> createRenderFor(EntityRendererManager manager) {
			return new WaterBossRenderer(manager);
		}	
	}
}
