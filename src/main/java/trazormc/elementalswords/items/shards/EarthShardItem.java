package trazormc.elementalswords.items.shards;

import java.util.List;

import net.minecraft.block.AirBlock;
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
import trazormc.elementalswords.init.ModEntityTypes;

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
				if(world.getLightFor(LightType.SKY, pos.up(1)) == 0 &&  world.getBlockState(pos.up(1)).getBlock() instanceof AirBlock && 
						world.getBlockState(pos.up(2)).getBlock() instanceof AirBlock) {
					EarthBossEntity earthBoss = new EarthBossEntity(ModEntityTypes.ENTITY_EARTH_BOSS, world);
					earthBoss.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
					world.addEntity(earthBoss);
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