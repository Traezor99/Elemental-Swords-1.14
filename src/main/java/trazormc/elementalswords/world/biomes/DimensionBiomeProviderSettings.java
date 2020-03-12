package trazormc.elementalswords.world.biomes;

import net.minecraft.world.biome.provider.IBiomeProviderSettings;

public class DimensionBiomeProviderSettings implements IBiomeProviderSettings {
	
	private long seed;
	
	public DimensionBiomeProviderSettings setSeed(long seed) {
		this.seed = seed;
		return this;
	}

	public long getSeed() {
		return this.seed;
	}

}
