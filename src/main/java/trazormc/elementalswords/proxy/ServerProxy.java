package trazormc.elementalswords.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ServerProxy implements IProxy {

	@Override
	public void setup(FMLCommonSetupEvent event) {
		
	}

	@Override
	public PlayerEntity getClientPlayer() {
		throw new IllegalStateException("Client side only!");
	}

	@Override
	public World getClientWorld() {
		throw new IllegalStateException("Client side only!");
	}

}
