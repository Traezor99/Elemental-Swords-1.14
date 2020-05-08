package trazormc.elementalswords.items.swords;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import trazormc.elementalswords.entities.AmethystMinerEntity;
import trazormc.elementalswords.init.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class EarthSword extends SwordItem {

	public EarthSword(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(tier, attackDamage, attackSpeed, properties);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		if(!worldIn.isRemote) {
			AmethystMinerEntity miner = new AmethystMinerEntity(ModEntityTypes.AMETHYST_MINER, worldIn);
			double x = playerIn.posX + 5;
			double z = playerIn.posZ + 5;
			double y = ModUtils.calculateGenerationHeight(worldIn, (int)x, (int)z);
			
			miner.setPosition(x, y + 1, z);
			worldIn.addEntity(miner);
		}
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		BlockPos pos = ctx.getPos();
		PlayerEntity player = ctx.getPlayer();
		AxisAlignedBB aoe = new AxisAlignedBB(pos.getX() - 5, pos.getY() - 5, pos.getZ() - 5, pos.getX() + 5, pos.getY() + 5, pos.getZ() + 5);
		List<Entity> entities = ctx.getWorld().getEntitiesWithinAABBExcludingEntity(player, aoe);
		Random rand = new Random();
		ItemStack item;
		
		if(player.getHeldItemMainhand().getItem() == this) {
			item = player.getHeldItemMainhand();
		} else {
			item = player.getHeldItemOffhand();
		}
		
		for (Entity e : entities) {
			double x = e.posX - player.posX; 
			double z = e.posZ - player.posZ; 
			
			if(x > 1) {
				x = 1;
			} else if(x < -1) {
				x = -1;
			}
			
			if(z > 1) {
				z = 1;
			} else if(z < -1) {
				z = -1;
			}
			e.setMotion(x, 1.1, z);
			e.attackEntityFrom(DamageSource.GENERIC, 4);
		}
		
		if(ctx.getWorld().isRemote) {
			for(int i = 0; i < 20; i++) {
				ctx.getWorld().addParticle(ParticleTypes.CRIT, ModUtils.getPos(rand, 5, pos.getX()), pos.getY() + 1, ModUtils.getPos(rand, 5, pos.getZ()), 0.0d, 0.0d, 0.0d);
			}
		}		
		
		ModUtils.playSound(ctx.getWorld(), player, pos);
		if(!ctx.getWorld().isRemote) {
			item.damageItem(1, (ServerPlayerEntity)player, (serverPlayer) -> {
				serverPlayer.sendBreakAnimation(EquipmentSlotType.MAINHAND);
			});
		}
		return super.onItemUse(ctx);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.hitEntity(stack, target, attacker);
		if(target instanceof ZombieEntity && !(target instanceof ZombiePigmanEntity) || target instanceof SkeletonEntity || target instanceof StrayEntity || target instanceof SpiderEntity || target instanceof CreeperEntity || target instanceof WitchEntity || target instanceof GiantEntity) {
			if(attacker instanceof PlayerEntity) {
				target.hurtResistantTime = 0;
				target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity)attacker), 4);
			} else {
				target.attackEntityFrom(DamageSource.causeMobDamage(attacker), 4);
			}
		}
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("Does 4 extra damage to Zombies, Zombie Villagers, Husks, Skeletons, Strays, Spiders, Cave Spiders, Creepers, and Witches."));
	}
}
