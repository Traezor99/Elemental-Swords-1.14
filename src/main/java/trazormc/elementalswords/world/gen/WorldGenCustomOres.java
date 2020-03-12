package trazormc.elementalswords.world.gen;

import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRange;
import net.minecraft.world.gen.placement.CountRangeConfig;
import trazormc.elementalswords.init.ModBiomes;
import trazormc.elementalswords.init.ModBlocks;

public class WorldGenCustomOres {
	
	private static final Predicate<BlockState> IS_AMETHYST = state -> state.getBlock() == ModBlocks.AMETHYST_BLOCK;
	
	public static void setupOreGen() {
		Biome biome = ModBiomes.AMETHYST_DIMENSION_BIOME;
		
		CountRangeConfig placementConfig = new CountRangeConfig(3, 0, 0, 100);
		
		//biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(Feature.ORE, new OreFeatureConfig(IS_AMETHYST, ModBlocks.AMETHYST_BLOCK.getDefaultState(), 10), new CountRange(), placementConfig));
	}
}
