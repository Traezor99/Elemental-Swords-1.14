package trazormc.elementalswords.world.dimensions.amethyst;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.holders.ModBlocks;
import trazormc.elementalswords.world.biomes.DimensionBiomeProviderSettings;
import trazormc.elementalswords.world.biomes.providers.AmethystBiomeProvider;
import trazormc.elementalswords.world.dimensions.DimensionChunkGenerator;
import trazormc.elementalswords.world.dimensions.DimensionGenSettings;

public class AmethystDimension extends Dimension {
	
	public AmethystDimension(World world, DimensionType type) {
		super(world, type);
	}
	
	@Override
	public boolean hasSkyLight() {
		return true;
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator() {
		BiomeProviderType<DimensionBiomeProviderSettings, AmethystBiomeProvider> biomeProviderType = new BiomeProviderType<>(AmethystBiomeProvider::new, DimensionBiomeProviderSettings::new);		
		ChunkGeneratorType<DimensionGenSettings, DimensionChunkGenerator> chunkGenType = new ChunkGeneratorType<>(DimensionChunkGenerator::new, true, DimensionGenSettings::new);
		DimensionGenSettings settings = chunkGenType.createSettings();
		settings.setDefaultBlock(ModBlocks.AMETHYST_STONE.getDefaultState());
		settings.setDefaultFluid(Blocks.AIR.getDefaultState());
		settings.setSpawnPos(getSpawnCoordinate());
		return chunkGenType.create(this.world, biomeProviderType.create(biomeProviderType.createSettings().setSeed(this.world.getSeed())), settings);
	}

	@Override
	public BlockPos findSpawn(ChunkPos pos, boolean checkValid) {
		Random random = new Random(this.world.getSeed());
	    BlockPos blockpos = new BlockPos(pos.getXStart() + random.nextInt(15), 0, pos.getZEnd() + random.nextInt(15));
	    return this.world.getGroundAboveSeaLevel(blockpos).getMaterial().blocksMovement() ? blockpos : null;
	}

	@Override
	public BlockPos findSpawn(int x, int z, boolean checkValid) {
		return this.findSpawn(new ChunkPos(x >> 4, z >> 4), checkValid);
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		return 0.0f;
	}

	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
		float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
	    f = MathHelper.clamp(f, 0.0F, 1.0F);
	    float f1 = 0.627451F;
	    float f2 = 0.5019608F;
	    float f3 = 0.627451F;
	    f1 = f1 * (f * 0.0F + 0.15F);
	    f2 = f2 * (f * 0.0F + 0.15F);
	    f3 = f3 * (f * 0.0F + 0.15F);
	    return new Vec3d((double)f1, (double)f2, (double)f3);
	}

	@Override
	public boolean canRespawnHere() {
		return true;
		//TO DO
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean doesXZShowFog(int x, int z) {
		return false;
	}

	@Override
	public DimensionType getType() {
		return ElementalSwords.AMETHYST_DIM_TYPE;
	}
}
