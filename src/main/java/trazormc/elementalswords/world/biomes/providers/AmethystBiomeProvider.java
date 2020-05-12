package trazormc.elementalswords.world.biomes.providers;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import trazormc.elementalswords.holders.ModBiomes;
import trazormc.elementalswords.world.biomes.DimensionBiomeProviderSettings;

public  class AmethystBiomeProvider extends BiomeProvider {	
	private final SimplexNoiseGenerator generator;
	private final SharedSeedRandom random;
	
	public AmethystBiomeProvider(DimensionBiomeProviderSettings settings) {
		this.random = new SharedSeedRandom(settings.getSeed());
	    this.random.skip(17292);
	    this.generator = new SimplexNoiseGenerator(this.random);
	}

	@Override
	public Biome getBiome(int x, int y) {
		return ModBiomes.AMETHYST_DIMENSION_BIOME;
	}

	@Override
	public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
		Biome[] abiome = new Biome[width * length];
	      Long2ObjectMap<Biome> long2objectmap = new Long2ObjectOpenHashMap<>();

	      for(int i = 0; i < width; ++i) {
	         for(int j = 0; j < length; ++j) {
	            int k = i + x >> 4;
	            int l = j + z >> 4;
	            long i1 = ChunkPos.asLong(k, l);
	            Biome biome = long2objectmap.get(i1);
	            if (biome == null) {
	               biome = this.getBiome1(k, l);
	               long2objectmap.put(i1, biome);
	            }

	            abiome[i + j * width] = biome;
	         }
	      }

	      return abiome;
	}
	
	private Biome getBiome1(int x, int z) {
		if ((long)x * (long)x + (long)z * (long)z <= 4096L) {
			return ModBiomes.AMETHYST_DIMENSION_BIOME;
	    } else {
	    	return Biomes.THE_VOID;
	    }
	}

	@Override
	public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
		int i = centerX - sideLength >> 2;
	      int j = centerZ - sideLength >> 2;
	      int k = centerX + sideLength >> 2;
	      int l = centerZ + sideLength >> 2;
	      int i1 = k - i + 1;
	      int j1 = l - j + 1;
	      return Sets.newHashSet(this.getBiomeBlock(i, j, i1, j1));
	}

	@Override
	public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
		int i = x - range >> 2;
	      int j = z - range >> 2;
	      int k = x + range >> 2;
	      int l = z + range >> 2;
	      int i1 = k - i + 1;
	      int j1 = l - j + 1;
	      Biome[] abiome = this.getBiomeBlock(i, j, i1, j1);
	      BlockPos blockpos = null;
	      int k1 = 0;

	      for(int l1 = 0; l1 < i1 * j1; ++l1) {
	         int i2 = i + l1 % i1 << 2;
	         int j2 = j + l1 / i1 << 2;
	         if (biomes.contains(abiome[l1])) {
	            if (blockpos == null || random.nextInt(k1 + 1) == 0) {
	               blockpos = new BlockPos(i2, 0, j2);
	            }

	            ++k1;
	         }
	      }

	      return blockpos;
	}
	
	@Override
	public float func_222365_c(int p_222365_1_, int p_222365_2_) {
		int i = p_222365_1_ / 2;
	      int j = p_222365_2_ / 2;
	      int k = p_222365_1_ % 2;
	      int l = p_222365_2_ % 2;
	      float f = 100.0F - MathHelper.sqrt((float)(p_222365_1_ * p_222365_1_ + p_222365_2_ * p_222365_2_)) * 8.0F;
	      f = MathHelper.clamp(f, -100.0F, 80.0F);

	      for(int i1 = -12; i1 <= 12; ++i1) {
	         for(int j1 = -12; j1 <= 12; ++j1) {
	            long k1 = (long)(i + i1);
	            long l1 = (long)(j + j1);
	            if (k1 * k1 + l1 * l1 > 4096L && this.generator.getValue((double)k1, (double)l1) < (double)-0.9F) {
	               float f1 = (MathHelper.abs((float)k1) * 3439.0F + MathHelper.abs((float)l1) * 147.0F) % 13.0F + 9.0F;
	               float f2 = (float)(k - i1 * 2);
	               float f3 = (float)(l - j1 * 2);
	               float f4 = 100.0F - MathHelper.sqrt(f2 * f2 + f3 * f3) * f1;
	               f4 = MathHelper.clamp(f4, -100.0F, 80.0F);
	               f = Math.max(f, f4);
	            }
	         }
	      }

	      return f;
	}

	@Override
	public boolean hasStructure(Structure<?> structureIn) {
		return this.hasStructureCache.computeIfAbsent(structureIn, (mappingFunction) -> {
			if(ModBiomes.AMETHYST_DIMENSION_BIOME.hasStructure(structureIn)) {
				return true;
			}
			
			return false;
		});
	}

	@Override
	public Set<BlockState> getSurfaceBlocks() {
		if(this.topBlocksCache.isEmpty()) {
			this.topBlocksCache.add(ModBiomes.AMETHYST_DIMENSION_BIOME.getSurfaceBuilderConfig().getTop());
		}
		
		return this.topBlocksCache;
	}
}
