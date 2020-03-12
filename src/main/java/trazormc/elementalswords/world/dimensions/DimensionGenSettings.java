package trazormc.elementalswords.world.dimensions;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.GenerationSettings;

public class DimensionGenSettings extends GenerationSettings {
	private BlockPos spawnPos;

	public DimensionGenSettings setSpawnPos(BlockPos pos) {
		this.spawnPos = pos;
		return this;
	}

	public BlockPos getSpawnPos() {
		return this.spawnPos;
	}
}
