package trazormc.elementalswords.entities;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.BossInfo;
import net.minecraft.world.ServerBossInfo;
import net.minecraft.world.World;
import trazormc.elementalswords.util.ModUtils;

public class LightningBossEntity extends MonsterEntity {
	private final ServerBossInfo bossInfo;
	
	public LightningBossEntity(EntityType<? extends LightningBossEntity> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 100;
		bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5f, false));
		this.goalSelector.addGoal(1, new LightningStrikeGoal(this));
		this.goalSelector.addGoal(1, new SummonMobsGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 7.0f));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));

		this.targetSelector.addGoal(0, new HurtByTargetGoal(this, new Class[] {}));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(25.0);
		this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0);
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
	public boolean isImmuneToExplosions() {
		return true;
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
		return SoundEvents.BLOCK_FIRE_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ENDER_DRAGON_HURT;
	}

	static class LightningStrikeGoal extends Goal {
		private final LightningBossEntity lightningBoss;
		private int lightningTimer = 0;

		public LightningStrikeGoal(LightningBossEntity entity) {
			this.lightningBoss = entity;
		}

		@Override
		public boolean shouldExecute() {
			return this.lightningBoss.getAttackTarget() instanceof PlayerEntity;
		}

		@Override
		public void startExecuting() {
			lightningTimer = 0;
		}

		@Override
		public void tick() {
			if(lightningTimer >= 100 && !this.lightningBoss.world.isRemote && this.lightningBoss.getAttackTarget() instanceof PlayerEntity) {
				LightningBoltEntity lightning = new LightningBoltEntity(this.lightningBoss.world, this.lightningBoss.getAttackTarget().posX, 
						this.lightningBoss.getAttackTarget().posY, this.lightningBoss.getAttackTarget().posZ, false);
				lightning.setPosition(this.lightningBoss.getAttackTarget().posX, this.lightningBoss.getAttackTarget().posY, this.lightningBoss.getAttackTarget().posZ);
				if(!this.lightningBoss.world.isRemote) {
					this.lightningBoss.getServer().func_71218_a(this.lightningBoss.dimension).addLightningBolt(lightning);
				}
				lightningTimer = 0;
			} else {
				lightningTimer++;
			}
			super.tick();
		}
	}

	static class SummonMobsGoal extends Goal {
		private final LightningBossEntity lightningBoss;
		private int summonMobsTimer = 300;

		public SummonMobsGoal(LightningBossEntity entity) {
			this.lightningBoss = entity;
		}

		@Override
		public boolean shouldExecute() {
			return this.lightningBoss != null && this.lightningBoss.isAlive();
		}

		@Override
		public void startExecuting() {
			summonMobsTimer = 300;
		}

		@Override
		public void tick() {
			if(summonMobsTimer >= 300 && !this.lightningBoss.world.isRemote) {   		
				if(this.lightningBoss.rand.nextInt(2) == 0) {
					CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, this.lightningBoss.world);
					ModUtils.attemptSpawnBossAdd(this.lightningBoss, creeper, 10);
					creeper.setAttackTarget(this.lightningBoss.getAttackTarget());

					LightningBoltEntity lightning = new LightningBoltEntity(this.lightningBoss.world, creeper.posX, creeper.posY, creeper.posZ, false);
					lightning.setPosition(creeper.posX, creeper.posY, creeper.posZ);
					this.lightningBoss.world.addEntity(lightning);
				} 

				for(int i = 0; i <= 4; i++) {
					ZombiePigmanEntity pigman = new ZombiePigmanEntity(EntityType.ZOMBIE_PIGMAN, this.lightningBoss.world);
					ModUtils.attemptSpawnBossAdd(this.lightningBoss, pigman, 10);  
					pigman.setAttackTarget(this.lightningBoss.getAttackTarget());
				}

				summonMobsTimer = 0;    		
			} else {
				summonMobsTimer++;
			}
			super.tick();
		}
	}
}
