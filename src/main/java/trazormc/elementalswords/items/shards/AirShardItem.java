package trazormc.elementalswords.items.shards;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import trazormc.elementalswords.entities.AirBossEntity;
import trazormc.elementalswords.init.ModEntityTypes;

public class AirShardItem extends Item {

	public AirShardItem(Properties properties) {
		super(properties);
	}
		
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		if(!entity.world.isRemote && entity.posY >= 250) {
			AirBossEntity airBoss = new AirBossEntity(ModEntityTypes.AIR_BOSS, entity.world);
			airBoss.setPosition(entity.posX, entity.posY, entity.posZ);
			entity.world.addEntity(airBoss);
			entity.remove();
		}
		return super.onEntityItemUpdate(stack, entity);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("A secret foe flies high in the sky."));
	}

}
