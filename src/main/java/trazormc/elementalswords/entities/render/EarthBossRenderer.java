package trazormc.elementalswords.entities.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.EarthBossEntity;
import trazormc.elementalswords.entities.modals.EarthBossModel;

public class EarthBossRenderer extends MobRenderer<EarthBossEntity, EarthBossModel> {
	
	protected ResourceLocation texture;

	public EarthBossRenderer(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new EarthBossModel(), 0.5f);
		texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/earth_boss.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(EarthBossEntity entity) {
		return texture;
	}
	
	public static class Factory implements IRenderFactory<EarthBossEntity> {

		@Override
		public EntityRenderer<? super EarthBossEntity> createRenderFor(EntityRendererManager manager) {
			return new EarthBossRenderer(manager);
		}	
	}

}
