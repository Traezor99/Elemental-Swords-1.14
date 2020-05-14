package trazormc.elementalswords.items.shards;

import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import trazormc.elementalswords.entities.AirBossEntity;
import trazormc.elementalswords.holders.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class AirShardItem extends Item {

	public AirShardItem(Properties properties) {
		super(properties);
	}
		
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		if(!entity.world.isRemote && entity.posY >= 250) {
			AirBossEntity airBoss = new AirBossEntity(ModEntityTypes.AIR_BOSS, entity.world);
			for(int i = 0; i < 10; i++) {
				airBoss.setPosition(ModUtils.getPos(random, 10, (int)entity.posX + 0.5), (int)entity.posY, ModUtils.getPos(random, 10, (int)entity.posZ + 0.5));
				if(entity.world.getBlockState(airBoss.getPosition()).getBlock() == Blocks.AIR && entity.world.getBlockState(airBoss.getPosition().up()).getBlock() == Blocks.AIR) {
					entity.world.addEntity(airBoss);
					entity.remove();
					break;
				}
			} 
		}
		return super.onEntityItemUpdate(stack, entity);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("A secret foe flies high in the sky."));
	}
}
