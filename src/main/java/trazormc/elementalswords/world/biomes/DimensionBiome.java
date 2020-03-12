package trazormc.elementalswords.world.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class DimensionBiome extends Biome {
	public DimensionBiome(SurfaceBuilderConfig config) {
		super(new Biome.Builder().category(Category.THEEND).surfaceBuilder(SurfaceBuilder.DEFAULT, config).precipitation(Biome.RainType.NONE).depth(0.1f).scale(0.2f).temperature(0.5f).downfall(0.0f).waterColor(4159204).waterFogColor(329011).parent((String)null));
	}
}
