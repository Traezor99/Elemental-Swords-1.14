package trazormc.elementalswords.items.shards;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import trazormc.elementalswords.entities.FireBossEntity;
import trazormc.elementalswords.init.ModEntityTypes;

public class FireShardItem extends Item {

	public FireShardItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if(!world.isRemote && world.getDimension().isNether()) {
			FireBossEntity fireBoss = new FireBossEntity(ModEntityTypes.ENTITY_FIRE_BOSS, world);
			fireBoss.setPosition(context.getPos().getX() + 0.5, context.getPos().getY() + 1, context.getPos().getZ() + 0.5);
			world.addEntity(fireBoss);
		}
		return ActionResultType.SUCCESS;
	}

}
