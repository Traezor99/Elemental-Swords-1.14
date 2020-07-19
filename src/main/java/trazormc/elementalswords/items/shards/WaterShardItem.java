package trazormc.elementalswords.items.shards;

import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import trazormc.elementalswords.entities.WaterBossEntity;
import trazormc.elementalswords.holders.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class WaterShardItem extends Item {
	
	public WaterShardItem(Properties properties) {
		super(properties);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("In the depths of the sea lies a hidden foe."));
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		World world = entity.world;
		if(!world.isRemote && isOcean(world.getBiome(entity.getPosition())) && entity.isInWater() && world.getSeaLevel() - entity.posY >= 6) {
			WaterBossEntity waterBoss = new WaterBossEntity(ModEntityTypes.WATER_BOSS, world);
			for(int i = 0; i < 15; i++) {
				waterBoss.setPosition(ModUtils.getPos(random, 10, (int)entity.posX + 0.5), (int)entity.posY, ModUtils.getPos(random, 10, (int)entity.posZ + 0.5));
				if(canSpawnHere(waterBoss)) {
					entity.world.addEntity(waterBoss);
					entity.remove();
					break;
				}
			} 
		}
		return super.onEntityItemUpdate(stack, entity);
	}
	
	private boolean canSpawnHere(WaterBossEntity waterBoss) {
		return waterBoss.world.getBlockState(waterBoss.getPosition()).getBlock() == Blocks.WATER && 
				waterBoss.world.getBlockState(waterBoss.getPosition().up()).getBlock() == Blocks.WATER &&
				waterBoss.world.getBlockState(new BlockPos(waterBoss.posX, 52, waterBoss.posZ)).getBlock() == Blocks.WATER;
	}

	/**
	 * Checks if the passed biome is any of the various ocean biomes.
	 * @param biome the biome to be checked
	 * @return true if it is an ocean, false if it is not
	 */
	private boolean isOcean(Biome biome) {
		return (biome.equals(Biomes.OCEAN) || biome.equals(Biomes.COLD_OCEAN) || biome.equals(Biomes.DEEP_COLD_OCEAN) || biome.equals(Biomes.DEEP_FROZEN_OCEAN)
				|| biome.equals(Biomes.DEEP_LUKEWARM_OCEAN) || biome.equals(Biomes.DEEP_OCEAN) || biome.equals(Biomes.DEEP_WARM_OCEAN) 
				|| biome.equals(Biomes.FROZEN_OCEAN) || biome.equals(Biomes.LUKEWARM_OCEAN) || biome.equals(Biomes.WARM_OCEAN));
	}
}