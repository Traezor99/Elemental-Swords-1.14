package trazormc.elementalswords.entities.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.entities.AmethystKingEntity;
import trazormc.elementalswords.entities.modals.AmethystKingModel;

public class AmethystKingRenderer extends MobRenderer<AmethystKingEntity, AmethystKingModel> {
	
	protected ResourceLocation texture;

	public AmethystKingRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new AmethystKingModel(), 0.5f);
		texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/entity/amethyst_king.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(AmethystKingEntity entity) {
		return texture;
	}
	
	public static class Factory implements IRenderFactory<AmethystKingEntity> {

		@Override
		public EntityRenderer<? super AmethystKingEntity> createRenderFor(EntityRendererManager manager) {
			return new AmethystKingRenderer(manager);
		}	
	}

}
