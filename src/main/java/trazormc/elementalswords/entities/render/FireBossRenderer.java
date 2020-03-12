package trazormc.elementalswords.entities.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.FireBossEntity;
import trazormc.elementalswords.entities.modals.FireBossModel;

public class FireBossRenderer extends MobRenderer<FireBossEntity, FireBossModel> {
	
	protected ResourceLocation texture;

	public FireBossRenderer(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new FireBossModel(), 0.5f);
		texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/fire_boss.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(FireBossEntity entity) {
		return texture;
	}
	
	public static class Factory implements IRenderFactory<FireBossEntity> {

		@Override
		public EntityRenderer<? super FireBossEntity> createRenderFor(EntityRendererManager manager) {
			return new FireBossRenderer(manager);
		}	
	}
}
