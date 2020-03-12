package trazormc.elementalswords.entities.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.AirBossEntity;
import trazormc.elementalswords.entities.modals.AirBossModel;

public class AirBossRenderer extends MobRenderer<AirBossEntity, AirBossModel> {

	protected ResourceLocation texture;

	public AirBossRenderer(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new AirBossModel(), 0.5f);
		texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/air_boss.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(AirBossEntity entity) {
		return texture;
	}
	
	public static class Factory implements IRenderFactory<AirBossEntity> {

		@Override
		public EntityRenderer<? super AirBossEntity> createRenderFor(EntityRendererManager manager) {
			return new AirBossRenderer(manager);
		}	
	}
}
