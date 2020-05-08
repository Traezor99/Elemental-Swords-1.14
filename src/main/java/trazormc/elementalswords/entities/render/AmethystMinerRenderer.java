package trazormc.elementalswords.entities.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.AmethystMinerEntity;
import trazormc.elementalswords.entities.modals.AmethystMinerModel;

public class AmethystMinerRenderer extends MobRenderer<AmethystMinerEntity, AmethystMinerModel> {
	
	protected ResourceLocation texture;
	
	public AmethystMinerRenderer(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new AmethystMinerModel(), 0.5f);
		this.texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/amethyst_miner.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(AmethystMinerEntity entity) {
		return texture;
	}
	
	public static class Factory implements IRenderFactory<AmethystMinerEntity> {

		@Override
		public EntityRenderer<? super AmethystMinerEntity> createRenderFor(EntityRendererManager manager) {
			return new AmethystMinerRenderer(manager);
		}	
	}
}
