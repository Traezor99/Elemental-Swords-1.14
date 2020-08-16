package trazormc.elementalswords.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.ServerBossInfo;
import net.minecraft.world.World;
import trazormc.elementalswords.util.ModUtils;

public class FireBossEntity extends BlazeEntity {
	private final ServerBossInfo bossInfo;

	public FireBossEntity(EntityType<? extends FireBossEntity> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 100;
		bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
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
	public boolean preventDespawn() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();
		bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FireBossEntity.FireballAttackGoal(this));
		this.goalSelector.addGoal(1, new FireBossEntity.SpawnBlazeGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.5f, false));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
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
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 1.74f;
	}

	@Override
	public boolean canPickUpLoot() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.BLOCK_FIRE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLOCK_FIRE_EXTINGUISH;
	}

	private class FireballAttackGoal extends Goal {
		private final FireBossEntity fireBoss;
		private int attackStep;
		private int attackTime;

		private FireballAttackGoal(FireBossEntity fireBoss) {
			this.fireBoss = fireBoss;
		}

		@Override
		public boolean shouldExecute() {
			return this.fireBoss.getAttackTarget() instanceof PlayerEntity;
		}

		@Override
		public void startExecuting() {
			this.attackStep = 0;
		}

		@Override
		public void tick() {
			--this.attackTime;
			Entity target = this.fireBoss.getAttackTarget();
			double distance = this.fireBoss.getDistance(target);

			if(distance < 3.0) {
				if(this.attackTime <= 0) {
					this.attackTime = 10;
					this.fireBoss.attackEntityAsMob(target);
				}         

				this.fireBoss.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, 1.0D);  
			} else if(distance < this.getFollowDistance()) {          	
				double d1 = target.posX - this.fireBoss.posX;
				double d2 = target.getBoundingBox().minY + (double)(target.getHeight() / 2.0F) - (this.fireBoss.posY + (double)(this.fireBoss.getHeight() / 2.0F));
				double d3 = target.posZ - this.fireBoss.posZ;

				if(this.attackTime <= 0) {
					++this.attackStep;

					if(this.attackStep == 1) {
						this.attackTime = 60;                 
					} else if(this.attackStep <= 11) {
						this.attackTime = 5;
					} else {
						this.attackTime = 10;
						this.attackStep = 0;
					}

					if(this.attackStep > 1) {
						float f = MathHelper.sqrt(MathHelper.sqrt(distance)) * 0.5F;
						this.fireBoss.world.playEvent(1018, new BlockPos((int)this.fireBoss.posX, (int)this.fireBoss.posY, (int)this.fireBoss.posZ), 0);

						for(int i = 0; i < 1; ++i)  {
							ChargedFireballEntity chargedfireball = new ChargedFireballEntity(this.fireBoss.world, this.fireBoss, d1 + this.fireBoss.getRNG().nextGaussian() * (double)f, d2, d3 + this.fireBoss.getRNG().nextGaussian() * (double)f);
							chargedfireball.posY = this.fireBoss.posY + (double)(this.fireBoss.getHeight() / 2.0F) + 0.5D;
							this.fireBoss.world.addEntity(chargedfireball);   
						}
					}
				}

				this.fireBoss.getLookController().setLookPositionWithEntity(target, 10.0F, 10.0F);
			} else {
				this.fireBoss.getNavigator().clearPath();
				this.fireBoss.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, 1.0D);
			}

			super.tick();
		}

		private double getFollowDistance() {
			IAttributeInstance iattributeinstance = this.fireBoss.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			return iattributeinstance == null ? 16.0D : iattributeinstance.getValue();
		}
	}
	
	private class SpawnBlazeGoal extends Goal {
		private final FireBossEntity fireBoss;
		private int spawnTime;

		private SpawnBlazeGoal(FireBossEntity fireBoss) {
			this.fireBoss = fireBoss;
		}

		@Override
		public boolean shouldExecute() {
			return this.fireBoss != null && this.fireBoss.isAlive();
		}

		@Override
		public void startExecuting() {
			this.spawnTime = 0;
			for(int i = 0; i < 5; i++) {
				ModUtils.attemptSpawnEntity(this.fireBoss, new BlazeEntity(EntityType.BLAZE, this.fireBoss.world), 10, 5);
			}
		}

		@Override
		public void tick() {
			super.tick();
			if(this.spawnTime >= 200 && !this.fireBoss.world.isRemote) {
				this.spawnTime = 0;
				BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, this.fireBoss.world);
				if(ModUtils.attemptSpawnEntity(this.fireBoss, blaze, 10, 5)) 
					blaze.setAttackTarget(this.fireBoss.getAttackTarget());
				else
					blaze.remove();		
			} else {
				this.spawnTime++;
			}
		}
	}
}