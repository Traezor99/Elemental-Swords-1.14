package trazormc.elementalswords.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.FindWaterGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.BossInfo;
import net.minecraft.world.ServerBossInfo;
import net.minecraft.world.World;
import trazormc.elementalswords.holders.ModEffects;

public class WaterBossEntity extends MonsterEntity {	
	private final ServerBossInfo bossInfo;

	public WaterBossEntity(EntityType<? extends WaterBossEntity> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 100;	
		bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FindWaterGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5f, false));
		this.goalSelector.addGoal(4, new WaterBossEntity.ForceDrownTargetGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 7.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));

		this.targetSelector.addGoal(0, new HurtByTargetGoal(this, new Class[] {}));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0d);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25d);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(26.0D);
		this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
	}

	@Override
	public void addTrackingPlayer(ServerPlayerEntity player) {
		super.addTrackingPlayer(player);
		bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(ServerPlayerEntity player) {
		super.removeTrackingPlayer(player);
		bossInfo.removePlayer(player);
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean preventDespawn() {
		return true;
	}

	@Override
	public void livingTick() {
		Entity target = this.getAttackTarget();
		if(!this.world.isRemote && target != null && this.isInWaterOrBubbleColumn()) {
			double maxSpeed = 0.3535533906; //sqrt(2) / 4
			double x = target.posX - this.posX;
			double y = target.posY - this.posY;
			double z = target.posZ - this.posZ;
			double mag = Math.sqrt(x * x + z * z);
			double pitch = Math.atan2(y, mag);
			double yaw = Math.atan2(z, x);
			x = maxSpeed * Math.cos(yaw);
			y = maxSpeed * Math.sin(pitch);
			z = maxSpeed * Math.sin(yaw);	
			this.setMotion(x, y, z);
		}
		super.livingTick();
	}

	@Override
	public void tick() {
		super.tick();
		bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 1.74f;
	}

	@Override
	public boolean canPickUpLoot() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_DROWNED_AMBIENT_WATER;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_DROWNED_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_DROWNED_HURT;
	}

	static class ForceDrownTargetGoal extends Goal {
		private WaterBossEntity waterBoss;
		private int timer = 0;

		public ForceDrownTargetGoal(WaterBossEntity entity) {
			this.waterBoss = entity;
		}

		@Override
		public boolean shouldExecute() {
			return this.waterBoss.getAttackTarget() instanceof PlayerEntity;
		}

		@Override
		public void startExecuting() {
			this.timer = 0;
		}

		@Override
		public void tick() {
			if(this.timer >= 1200) {
				this.timer = 0;
				if(this.waterBoss.rand.nextInt(2) == 0) {
					PlayerEntity player = (PlayerEntity)this.waterBoss.getAttackTarget();
					player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100));
					player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 2));
					player.addPotionEffect(new EffectInstance(ModEffects.FORCED_DROWN, 100, 4));
				}
			} else {
				this.timer++;
			}
			super.tick();
		}
	}
}
