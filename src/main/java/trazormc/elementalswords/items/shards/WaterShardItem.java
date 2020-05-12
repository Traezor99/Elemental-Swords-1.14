package trazormc.elementalswords.items.shards;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
		if(!entity.world.isRemote && isOcean(entity.world.getBiome(entity.getPosition())) && entity.isInWater()) {
			//Doesn't work quite right
			ModUtils.attemptSpawnEntity(entity, new WaterBossEntity(ModEntityTypes.WATER_BOSS, entity.world), 10, 10);
			entity.remove();
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