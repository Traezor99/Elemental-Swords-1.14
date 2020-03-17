package trazormc.elementalswords.items.shards;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import trazormc.elementalswords.entities.WaterBossEntity;
import trazormc.elementalswords.init.ModEntityTypes;

public class WaterShardItem extends Item {

	public WaterShardItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		if(entity.isInWater()) {
			if(!entity.world.isRemote) {
				if(isOcean(entity.world.getBiome(entity.getPosition()))) {
					WaterBossEntity waterBoss = new WaterBossEntity(ModEntityTypes.ENTITY_WATER_BOSS, entity.world);
					waterBoss.setPosition(entity.posX, entity.posY, entity.posZ);
					entity.world.addEntity(waterBoss);
					entity.remove();
				}
			}
		}
		return super.onEntityItemUpdate(stack, entity);
	}
	
	/**
	 * Checks if the passed biome is any of the various ocean biomes.
	 * @param biome the biome to be checked
	 * @return true if it is an ocean, false if it is not
	 */
	private boolean isOcean(Biome biome) {
		if(biome.equals(Biomes.OCEAN) || biome.equals(Biomes.COLD_OCEAN) || biome.equals(Biomes.DEEP_COLD_OCEAN) || biome.equals(Biomes.DEEP_FROZEN_OCEAN)
				|| biome.equals(Biomes.DEEP_LUKEWARM_OCEAN) || biome.equals(Biomes.DEEP_OCEAN) || biome.equals(Biomes.DEEP_WARM_OCEAN) 
				|| biome.equals(Biomes.FROZEN_OCEAN) || biome.equals(Biomes.LUKEWARM_OCEAN) || biome.equals(Biomes.WARM_OCEAN))
			return true;
		else
			return false;
	}
}