package trazormc.elementalswords.items.shards;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import trazormc.elementalswords.entities.FireBossEntity;
import trazormc.elementalswords.holders.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class FireShardItem extends Item {

	public FireShardItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if(!world.isRemote && world.getDimension().isNether()) {
			BlockPos pos = context.getPos();
			if(ModUtils.attemptSpawnEntity(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world, new FireBossEntity(ModEntityTypes.FIRE_BOSS, world), 20, 10))
				context.getItem().grow(-1);
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("An angry foe smolders in the fiery depths."));
	}
}
