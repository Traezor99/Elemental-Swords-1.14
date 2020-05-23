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
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.BossInfo;
import net.minecraft.world.ServerBossInfo;
import net.minecraft.world.World;
import trazormc.elementalswords.util.ModUtils;

public class EarthBossEntity extends MonsterEntity {
	private final ServerBossInfo bossInfo;

	public EarthBossEntity(EntityType<? extends EarthBossEntity> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 100;
		bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);	
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5f, false));
		this.goalSelector.addGoal(1, new EarthBossEntity.PoundGoal(this));
		this.goalSelector.addGoal(2, new EarthBossEntity.SpawnMobsGoal(this));
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
		return SoundEvents.AMBIENT_CAVE;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLOCK_STONE_BREAK;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.BLOCK_STONE_HIT;
	}

	private class PoundGoal extends Goal {
		private final EarthBossEntity earthBoss;
		private int poundTimer = 0;

		private PoundGoal(EarthBossEntity entity) {
			this.earthBoss = entity;
		}

		@Override
		public boolean shouldExecute() {
			return this.earthBoss.getAttackTarget() instanceof PlayerEntity;
		}

		@Override
		public void startExecuting() {
			poundTimer = 0;
		}

		@Override
		public void tick() {
			if(poundTimer >= 300) {
				AxisAlignedBB aoe = new AxisAlignedBB(this.earthBoss.posX - 10, this.earthBoss.posY - 5, this.earthBoss.posX - 10, 
						this.earthBoss.posX + 10, this.earthBoss.posY + 5, this.earthBoss.posZ + 10);
				List<Entity> entities = this.earthBoss.world.getEntitiesWithinAABBExcludingEntity(this.earthBoss, aoe);		
				for(Entity e : entities) {
					double theta = Math.atan2(e.posZ - this.earthBoss.posZ, e.posX - this.earthBoss.posX);
					
					if(e instanceof PlayerEntity) {
						e.setMotion(Math.cos(theta), 1.3, Math.sin(theta));
						e.attackEntityFrom(DamageSource.causeMobDamage(this.earthBoss), 8);
					} else {
						e.setMotion(Math.cos(theta), 1, Math.sin(theta));
						e.attackEntityFrom(DamageSource.causeMobDamage(this.earthBoss), 4);
					}
				}

				if(this.earthBoss.world.isRemote) {
					for(int i = 0; i < 20; i++) {
						this.earthBoss.world.addParticle(ParticleTypes.CRIT, ModUtils.getPos(this.earthBoss.rand, 5, (int)this.earthBoss.posX), this.earthBoss.posY + 1, ModUtils.getPos(this.earthBoss.rand, 5, (int)this.earthBoss.posZ), 0.0d, 0.0d, 0.0d);
					}
				}

				ModUtils.playSound(this.earthBoss.world, (PlayerEntity)this.earthBoss.getAttackTarget(), this.earthBoss.getPosition());	
				ModUtils.effectPlayer((PlayerEntity)this.earthBoss.getAttackTarget(), Effects.SLOWNESS, 100, 2);
				poundTimer = 0;
			} else {
				poundTimer++;
			}

			super.tick();
		}
	}

	private class SpawnMobsGoal extends Goal {
		private final EarthBossEntity earthBoss;
		private int spawnTimer = 0;

		private SpawnMobsGoal(EarthBossEntity entity) {
			this.earthBoss = entity;
		}

		@Override
		public boolean shouldExecute() {
			return this.earthBoss != null && this.earthBoss.isAlive();
		}

		@Override
		public void startExecuting() {
			spawnTimer = 0;
		}

		@Override
		public void tick() {
			if(spawnTimer >= 200) {
				for(int i = 0; i < 10; i++) {
					switch(this.earthBoss.rand.nextInt(2)) {
					case 0:
						ZombieEntity zombie = new ZombieEntity(this.earthBoss.world);
						ModUtils.attemptSpawnEntity(this.earthBoss, zombie, 7, 5);
						zombie.setAttackTarget(this.earthBoss.getAttackTarget());
						break;
					case 1:
						SkeletonEntity skeleton = new SkeletonEntity(EntityType.SKELETON, this.earthBoss.world);
						ModUtils.attemptSpawnEntity(this.earthBoss, skeleton, 7, 5);
						skeleton.setAttackTarget(this.earthBoss.getAttackTarget());
						break;
					}
				}

				spawnTimer = 0;
			} else {
				spawnTimer++;
			}

			super.tick();
		}
	}
}