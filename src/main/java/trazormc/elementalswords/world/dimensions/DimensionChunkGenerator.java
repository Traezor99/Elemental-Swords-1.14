package trazormc.elementalswords.world.dimensions;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.NoiseChunkGenerator;

//Redo if more is deobfuscated

public class DimensionChunkGenerator extends NoiseChunkGenerator<DimensionGenSettings> {
	public DimensionChunkGenerator(IWorld iWorld, BiomeProvider provider, DimensionGenSettings settings) {
		super(iWorld, provider, 8, 4, 128, settings, true);
		settings.getSpawnPos();
	}
	
	@Override
	protected void fillNoiseColumn(double[] arg0, int arg1, int arg2) {
	   this.func_222546_a(arg0, arg1, arg2, 1368.824D, 684.412D, 17.110300000000002D, 4.277575000000001D, 64, -3000);
	}
	@Override
	protected double[] getBiomeNoiseColumn(int arg0, int arg1) {
		return new double[]{(double)this.biomeProvider.func_222365_c(arg0, arg1), 0.0D};
	}
	
	@Override
	protected double func_222545_a(double p_222545_1_, double p_222545_3_, int p_222545_5_) {
		return 8.0D - p_222545_1_;
	}
	
	@Override
	protected double func_222551_g() {
		return super.func_222551_g() / 2;
	}
	
	@Override
	protected double func_222553_h() {
		return 8.0D;
	}
	
	@Override
	public int getGroundHeight() {
		return 50;
	}
	
	@Override
	public int getSeaLevel() {
		return 0;
	}
}