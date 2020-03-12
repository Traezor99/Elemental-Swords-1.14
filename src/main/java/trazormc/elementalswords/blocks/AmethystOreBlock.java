package trazormc.elementalswords.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

public class AmethystOreBlock extends Block {
	
	public AmethystOreBlock(Properties properties) {
		super(properties);	
	}
	
	@Override
	public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? MathHelper.nextInt(new Random(), 4, 10) : 0;
	}
}
