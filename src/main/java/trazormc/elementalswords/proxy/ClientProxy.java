package trazormc.elementalswords.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientProxy implements IProxy {
	
	@Override
	public void setup(FMLCommonSetupEvent event) {
		
	}
	
	@Override
	public PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}
	
	@Override
	public World getClientWorld() {
		return Minecraft.getInstance().world;
	}

}
