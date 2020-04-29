package trazormc.elementalswords.util.handlers;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import trazormc.elementalswords.entities.AirBossEntity;
import trazormc.elementalswords.entities.AmethystMinerEntity;
import trazormc.elementalswords.entities.ChargedFireballEntity;
import trazormc.elementalswords.entities.EarthBossEntity;
import trazormc.elementalswords.entities.FireBossEntity;
import trazormc.elementalswords.entities.HailEntity;
import trazormc.elementalswords.entities.LightningBossEntity;
import trazormc.elementalswords.entities.WaterBossEntity;
import trazormc.elementalswords.entities.WindSeekerEntity;
import trazormc.elementalswords.entities.render.AirBossRenderer;
import trazormc.elementalswords.entities.render.AmethystMinerRenderer;
import trazormc.elementalswords.entities.render.ChargedFireballRenderer;
import trazormc.elementalswords.entities.render.EarthBossRenderer;
import trazormc.elementalswords.entities.render.FireBossRenderer;
import trazormc.elementalswords.entities.render.HailRenderer;
import trazormc.elementalswords.entities.render.LightningBossRenderer;
import trazormc.elementalswords.entities.render.WaterBossRenderer;
import trazormc.elementalswords.entities.render.WindSeekerRenderer;

public class RenderHandler {

	public static void registerEntityRenders() {		
		RenderingRegistry.registerEntityRenderingHandler(AirBossEntity.class, new AirBossRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(AmethystMinerEntity.class, new AmethystMinerRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(ChargedFireballEntity.class, new ChargedFireballRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EarthBossEntity.class, new EarthBossRenderer.Factory());	
		RenderingRegistry.registerEntityRenderingHandler(FireBossEntity.class, new FireBossRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(HailEntity.class, new HailRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(LightningBossEntity.class, new LightningBossRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(WaterBossEntity.class, new WaterBossRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(WindSeekerEntity.class, new WindSeekerRenderer.Factory());
	}
}
