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
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.BossInfo;
import net.minecraft.world.ServerBossInfo;
import net.minecraft.world.World;
import trazormc.elementalswords.util.ModUtils;

public class AirBossEntity extends MonsterEntity {
	private final ServerBossInfo bossInfo;

	public AirBossEntity(EntityType<? extends AirBossEntity> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 100;
		bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5f, false));
		this.goalSelector.addGoal(1, new WhirlWindGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
		this.goalSelector.addGoal(6, new SpawnPhantomsGoal(this));
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
	public void applyEntityCollision(Entity entityIn) {
		if(entityIn instanceof PlayerEntity) {
			double x = entityIn.posX - this.posX; 
			double z = entityIn.posZ - this.posZ;
			
			entityIn.setSprinting(false);
			entityIn.setVelocity(x * 5, 10, z * 5);
			entityIn.attackEntityFrom(DamageSource.GENERIC, 8);
		} else {
			super.applyEntityCollision(entityIn);
		}
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
	public void fall(float distance, float damageMultiplier) {}

	@Override
	public boolean canPickUpLoot() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ITEM_ELYTRA_FLYING;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_GENERIC_EXPLODE;
	}
	
	//Probably change sounds?
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_PUFFER_FISH_HURT;
	}
	
	static class SpawnPhantomsGoal extends Goal {
		private final AirBossEntity airBoss;
		private int phantomSpawnTimer = 100;
		
		public SpawnPhantomsGoal(AirBossEntity entity) {
			this.airBoss = entity;
		}

		@Override
		public boolean shouldExecute() {
			return this.airBoss != null && this.airBoss.isAlive();
		}
		
		@Override
		public void startExecuting() {
			phantomSpawnTimer = 100;
		}
		
		@Override
		public void tick() {
			if(phantomSpawnTimer >= 200) {
				for(int i = 0; i < 5; i++) {
					PhantomEntity phantom = new PhantomEntity(EntityType.PHANTOM, this.airBoss.world);
					int x = ModUtils.getPos(this.airBoss.rand, 10, (int)this.airBoss.posX);
					int z = ModUtils.getPos(this.airBoss.rand, 10, (int)this.airBoss.posZ);
					int y = ModUtils.calculateGenerationHeight(this.airBoss.world, x, z);
					phantom.setPosition(x, y, z);
					this.airBoss.world.addEntity(phantom);
					phantom.setAttackTarget(this.airBoss.attackingPlayer);
				}

				phantomSpawnTimer = 0;
			} else {
				phantomSpawnTimer++;
			}
			super.tick();
		}
	}
	
	static class WhirlWindGoal extends Goal {
		private final AirBossEntity airBoss;
		private int whirlWindTimer = 0;
		
		public WhirlWindGoal(AirBossEntity entity) {
			this.airBoss = entity;
		}

		@Override
		public boolean shouldExecute() {
			return this.airBoss.getAttackTarget() instanceof PlayerEntity;
		}
		
		@Override
		public void startExecuting() {
			whirlWindTimer = 0;
		}
		
		@Override
		public void tick() {
			if(whirlWindTimer >= 100) {//Set to 300, or make better change
				AxisAlignedBB aoe = new AxisAlignedBB(this.airBoss.posX - 20, this.airBoss.posY - 5, this.airBoss.posZ - 20, this.airBoss.posX + 20, 
						this.airBoss.posY + 5, this.airBoss.posZ + 20);
				List<Entity> entities = this.airBoss.world.getEntitiesWithinAABBExcludingEntity(this.airBoss, aoe);	
				for(Entity e : entities) {
					double x = (this.airBoss.posX - e.posX) * 2; 
					double y = this.airBoss.posY - e.posY;
					double z = (this.airBoss.posZ - e.posZ) * 2;
					//Something does not work when player is straight in the x or z direction from the entity
					/*if(Math.abs(x) < 1)
						x = 2;
					else if(Math.abs(z) < 1)
						z = 2;*/

					if(e instanceof PlayerEntity) {
						e.addVelocity(x, y, z); 
						e.attackEntityFrom(DamageSource.GENERIC, 8);
					} else {
						e.addVelocity(x, y, z);
						e.attackEntityFrom(DamageSource.GENERIC, 4);
					}
				}

				if(this.airBoss.world.isRemote) {
					for(int i = 0; i < 20; i++) {
						this.airBoss.world.addParticle(ParticleTypes.EXPLOSION, ModUtils.getPos(this.airBoss.rand, 5, (int)this.airBoss.posX), 
								this.airBoss.posY + 1, ModUtils.getPos(this.airBoss.rand, 5, (int)this.airBoss.posZ), 0.0d, 0.0d, 0.0d);
					}
				}

				whirlWindTimer = 0;
			} else {
				whirlWindTimer++;
			}
			super.tick();
		}
	}
}