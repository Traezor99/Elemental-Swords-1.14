package trazormc.elementalswords.items.swords;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trazormc.elementalswords.entities.AmethystMinerEntity;
import trazormc.elementalswords.init.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class AirSword extends SwordItem {
	private final int reach = 21;

	public AirSword(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(tier, attackDamage, attackSpeed, properties);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		if(!worldIn.isRemote) {
			AmethystMinerEntity miner = new AmethystMinerEntity(ModEntityTypes.ENTITY_AMETHYST_MINER, worldIn);
			double x = playerIn.posX + 5;
			double z = playerIn.posZ + 5;
			double y = ModUtils.calculateGenerationHeight(worldIn, (int)x, (int)z);
			
			miner.setPosition(x, y +1, z);
			worldIn.addEntity(miner);
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("Does 4 extra damage to Endermen, Shulkers, Endermites, Phantoms, and the EnderDragon."));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {		
		ItemStack item = playerIn.getHeldItem(handIn);
		Entity entity = entityLookedAt(reach, playerIn, worldIn);
		worldIn.playSound(playerIn, new BlockPos(playerIn.posX, playerIn.posY, playerIn.posZ), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 5.0f, 0.8f);
		if(!worldIn.isRemote) {
			if(entity != null && entity != playerIn && playerIn.getDistance(entity) <= reach) {
				entity.setVelocity(0, 3, 0);
			}
		} 
		item.attemptDamageItem(1, new Random(), null);	
		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, item);
	}
	
	/**
	 * Searches one block at a time in the direction the player is looking. Stops at after the amount of blocks specified. 
	 * Returns the first entity found. Spawns a cloud particle for each block checked. Could be used for gun shooting with slight 
	 * modifications
	 * @param reach the max range to search
	 * @param player the player using the sword
	 * @param worldIn the current world
	 * @return the first entity found, null if there are no entities in range
	 */
	private static Entity entityLookedAt(double reach, PlayerEntity player, World worldIn) {
		Vec3d vec = player.getLookVec();
		
		for(int i = 1; i < reach; i++) {
			AxisAlignedBB aabb = new AxisAlignedBB(player.posX + vec.x * i + 0.5d, player.posY + vec.y * i + 2, player.posZ + vec.z * i + 0.5d, player.posX + vec.x * i - 0.5d, player.posY + vec.y * i + 1, player.posZ + vec.z * i - 0.5d);
			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(player, aabb);
			if(!list.isEmpty()) {
				return list.get(0);
			}
			worldIn.addParticle(ParticleTypes.CLOUD, player.posX + vec.x * i, player.posY + vec.y * i + 1, player.posZ + vec.z * i, 0.0d, 0.0d, 0.0d);
		}
		return null;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.hitEntity(stack, target, attacker);
		if(target instanceof EndermanEntity || target instanceof EnderDragonEntity || target instanceof ShulkerEntity || target instanceof EndermiteEntity || target instanceof PhantomEntity) {
			if(attacker instanceof PlayerEntity) {
				target.hurtResistantTime = 0;
				target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity)attacker), 4);
			} else {
				target.attackEntityFrom(DamageSource.causeMobDamage(attacker), 4);
			}
		}
		return true;
	}
}
