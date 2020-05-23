package trazormc.elementalswords.entities.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import trazormc.elementalswords.entities.BubbleEntity;

public class BubbleRenderer extends SpriteRenderer<BubbleEntity> {

	public BubbleRenderer(EntityRendererManager renderManagerIn, ItemRenderer itemRendererIn) {
		super(renderManagerIn, itemRendererIn);
	}
	
	public static class Factory implements IRenderFactory<BubbleEntity> {

		@Override
		public EntityRenderer<? super BubbleEntity> createRenderFor(EntityRendererManager manager) {
			return new BubbleRenderer(manager, Minecraft.getInstance().getItemRenderer());
		}
		
	}

}
