package trazormc.elementalswords.entities;

import java.util.List;

import net.minecraft.entity.Entity;
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
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
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
		this.goalSelector.addGoal(1, new LightningBossEntity.LightningStrikeGoal(this));
		this.goalSelector.addGoal(2, new LightningBossEntity.SpawnCreeperGoal(this));
		this.goalSelector.addGoal(2, new LightningBossEntity.AngryGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 7.0f));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));

		this.targetSelector.addGoal(0, new HurtByTargetGoal(this, new Class[] {}));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(26.0);
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
		if(!this.world.isRemote) 
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
	
	private class AngryGoal extends Goal {
		private final LightningBossEntity lightningBoss;
		private int angerDuration = 0; 
		private int step = 0; 
		private int timer = 0; 

		private AngryGoal(LightningBossEntity entity) {
			this.lightningBoss = entity;
		}
		
		@Override
		public boolean shouldExecute() {
			return this.lightningBoss.getAttackTarget() instanceof PlayerEntity;
		}
		
		@Override
		public void startExecuting() {
			angerDuration = 0;
			timer = 0;
			step = 0;
		}
		
		@Override
		public void tick() {
			if(timer >= 1000) {
				if(angerDuration <= 300) {
					angerDuration++;
					
					if(step >= 7) {
						step = 0;
						AxisAlignedBB aoe = new AxisAlignedBB(this.lightningBoss.posX - 10, this.lightningBoss.posY - 5, this.lightningBoss.posX - 10, this.lightningBoss.posX + 10, this.lightningBoss.posY + 5, this.lightningBoss.posZ + 10);
						List<Entity> entities = this.lightningBoss.world.getEntitiesWithinAABBExcludingEntity(this.lightningBoss, aoe);
		
						for(Entity e : entities) {
							double x = e.posX - this.lightningBoss.posX; 
							double z = e.posZ - this.lightningBoss.posZ;
							float distance = (float) Math.sqrt(x * x + z * z);
							if(Math.abs(distance) <= 0.1)
								distance = 0.1f;
							e.attackEntityFrom(DamageSource.GENERIC, MathHelper.clamp(4 / (distance / 2), 2, 10));
							e.hurtResistantTime = 0;
						}
					} else {
						step++;
					}
				} else {
					angerDuration = 0;
					timer = 0;
					step = 0;
				}
			} else {
				timer++;
			}
			
			super.tick();
		}
		
	}

	private class LightningStrikeGoal extends Goal {
		private final LightningBossEntity lightningBoss;
		private int lightningTimer = 0;

		private LightningStrikeGoal(LightningBossEntity entity) {
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
			Entity target = this.lightningBoss.getAttackTarget();
			if(lightningTimer >= 100 && !this.lightningBoss.world.isRemote) {
				LightningBoltEntity lightning = new LightningBoltEntity(this.lightningBoss.world, target.posX, target.posY, target.posZ, false);
				lightning.setPosition(target.posX, target.posY, target.posZ);
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

	private class SpawnCreeperGoal extends Goal {
		private final LightningBossEntity lightningBoss;
		private int summonMobsTimer = 300;

		private SpawnCreeperGoal(LightningBossEntity entity) {
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
				summonMobsTimer = 0;
				CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, this.lightningBoss.world);
				if(ModUtils.attemptSpawnEntity(this.lightningBoss, creeper, 10, 5)) {
					LightningBoltEntity lightning = new LightningBoltEntity(this.lightningBoss.world, creeper.posX, creeper.posY, creeper.posZ, true);
					lightning.setPosition(creeper.posX, creeper.posY, creeper.posZ);
					this.lightningBoss.world.addEntity(lightning);    
				} else {
					creeper.remove();
				}
			} else {
				summonMobsTimer++;
			}
			super.tick();
		}
	}
}
