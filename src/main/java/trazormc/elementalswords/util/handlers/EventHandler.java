package trazormc.elementalswords.util.handlers;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.TickEvent;
import trazormc.elementalswords.entities.AirBossEntity;
import trazormc.elementalswords.entities.FireBossEntity;
import trazormc.elementalswords.entities.LightningBossEntity;
import trazormc.elementalswords.init.ModItems;

@EventBusSubscriber
public class EventHandler {

	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event) {
		boolean fly = (event.player.inventory.armorInventory.get(3).getItem() == ModItems.AIR_HELMET 
				&& event.player.inventory.armorInventory.get(2).getItem() == ModItems.AIR_CHESTPLATE
				&& event.player.inventory.armorInventory.get(1).getItem() == ModItems.AIR_LEGGINGS
				&& event.player.inventory.armorInventory.get(0).getItem() == ModItems.AIR_BOOTS); 

		if(fly || event.player.isCreative() || event.player.isSpectator()) {
			event.player.abilities.allowFlying = true;
		} else {
			event.player.abilities.allowFlying = false;
			event.player.abilities.isFlying = false;
		}
	}

	@SubscribeEvent
	public void onLightningStrike(EntityStruckByLightningEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			if(player.inventory.armorInventory.get(3).getItem() == ModItems.LIGHTNING_HELMET 
					&& player.inventory.armorInventory.get(2).getItem() == ModItems.LIGHTNING_CHESTPLATE
					&& player.inventory.armorInventory.get(1).getItem() == ModItems.LIGHTNING_LEGGINGS
					&& player.inventory.armorInventory.get(0).getItem() == ModItems.LIGHTNING_BOOTS) {
				event.setCanceled(true);
			} 
		} else if(event.getEntity() instanceof LightningBossEntity) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void cancelFallDamage(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof AirBossEntity) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onEntityHit(LivingAttackEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof FireBossEntity) {
			AxisAlignedBB aoe = new AxisAlignedBB(entity.posX - 40, entity.posY - 40, entity.posZ - 40, entity.posX + 40, entity.posY + 40, entity.posZ + 40);
			List<Entity> entities = entity.getEntityWorld().getEntitiesWithinAABBExcludingEntity(entity, aoe);
			for(Entity e : entities) {
				if(e instanceof BlazeEntity) {
					event.setCanceled(true);
					break;
				}
			}
		}
	}
}
