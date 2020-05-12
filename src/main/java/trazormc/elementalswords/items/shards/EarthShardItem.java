package trazormc.elementalswords.items.shards;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import trazormc.elementalswords.entities.EarthBossEntity;
import trazormc.elementalswords.holders.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class EarthShardItem extends Item {

	public EarthShardItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		if(!world.isRemote && pos.getY() <= 62) {
			Block block = world.getBlockState(pos).getBlock();
			if(block.equals(Blocks.STONE) || block.equals(Blocks.ANDESITE) || block.equals(Blocks.DIORITE) || block.equals(Blocks.GRANITE)) {
				if(world.getLightFor(LightType.SKY, pos.up(1)) == 0) {
					if(ModUtils.attemptSpawnEntity(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world, new EarthBossEntity(ModEntityTypes.EARTH_BOSS, world), 20, 10))
						context.getItem().grow(-1);
				}
			}
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("Down in the darkness roams a secret enemy."));
	}

}