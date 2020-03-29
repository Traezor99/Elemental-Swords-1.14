package trazormc.elementalswords.entities.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.entities.ChargedFireballEntity;

@OnlyIn(Dist.CLIENT)
public class ChargedFireballRenderer extends SpriteRenderer<ChargedFireballEntity> {

	public ChargedFireballRenderer(EntityRendererManager renderManagerIn, ItemRenderer itemRenderer) {
		super(renderManagerIn, itemRenderer);
	}
	
	public static class Factory implements IRenderFactory<ChargedFireballEntity> {

		@Override
		public EntityRenderer<? super ChargedFireballEntity> createRenderFor(EntityRendererManager manager) {
			return new ChargedFireballRenderer(manager, Minecraft.getInstance().getItemRenderer());
		}
		
	}

}
