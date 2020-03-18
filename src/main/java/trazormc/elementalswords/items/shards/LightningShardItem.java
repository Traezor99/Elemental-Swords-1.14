package trazormc.elementalswords.items.shards;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import trazormc.elementalswords.entities.LightningBossEntity;
import trazormc.elementalswords.init.ModEntityTypes;

public class LightningShardItem extends Item {

	public LightningShardItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if(!world.isRemote && world.isThundering()) {
			LightningBossEntity lightningBoss = new LightningBossEntity(ModEntityTypes.ENTITY_LIGHTNING_BOSS, world);
			lightningBoss.setPosition(context.getPos().getX() + 0.5, context.getPos().getY() + 1, context.getPos().getZ() + 0.5);
			world.addEntity(lightningBoss);
		}
		return ActionResultType.SUCCESS;
	}

}
