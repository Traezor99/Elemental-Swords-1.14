package trazormc.elementalswords.entities.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.LightningBossEntity;
import trazormc.elementalswords.entities.modals.LightningBossModel;

public class LightningBossRenderer extends MobRenderer<LightningBossEntity, LightningBossModel> {
	
	protected ResourceLocation texture;

	public LightningBossRenderer(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new LightningBossModel(), 0.5f);
		texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/lightning_boss.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(LightningBossEntity entity) {
		return texture;
	}
	
	public static class Factory implements IRenderFactory<LightningBossEntity> {

		@Override
		public EntityRenderer<? super LightningBossEntity> createRenderFor(EntityRendererManager manager) {
			return new LightningBossRenderer(manager);
		}	
	}

}
