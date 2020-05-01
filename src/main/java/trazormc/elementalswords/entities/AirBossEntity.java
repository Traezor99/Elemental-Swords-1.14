package trazormc.elementalswords.entities;

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
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.ServerBossInfo;
import net.minecraft.world.World;
import trazormc.elementalswords.util.ModUtils;

public class AirBossEntity extends MonsterEntity {
	private final ServerBossInfo bossInfo;
	private boolean shouldMove = false;

	public AirBossEntity(EntityType<? extends AirBossEntity> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 100;
		if(!this.world.isRemote)
			bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
		else
			bossInfo = null;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5f, false));
		this.goalSelector.addGoal(1, new ShootWindSeekerGoal(this));
		this.goalSelector.addGoal(2, new ShootHailGoal(this));
		this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 100));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));

		this.targetSelector.addGoal(0, new HurtByTargetGoal(this, new Class[] {}));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0);
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
			entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 8);
		} else {
			super.applyEntityCollision(entityIn);
		}
	}

	@Override
	public void addTrackingPlayer(ServerPlayerEntity player) {
		super.addTrackingPlayer(player);
		if(!this.world.isRemote)
			bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(ServerPlayerEntity player) {
		super.removeTrackingPlayer(player);
		if(!this.world.isRemote)
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
		if(!this.world.isRemote)
			bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 1.74f;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	public void livingTick() {
		if(!this.world.isRemote) {
			Entity target = this.getAttackTarget();
			if(target != null) {
				double x = target.posX - this.posX;
				double y = target.posY - this.posY;
				double z = target.posZ - this.posZ;
				double mag = Math.sqrt(x * x + z * z);
				double theta = Math.atan2(z, x);
				shouldMove = mag >= 25 || (shouldMove && mag >= 15);
				this.rotationYaw = (float)(theta * (180 / Math.PI)) - 90;

				if(Math.abs(y) <= 5 || ((int)this.posY == 150 && y < 0)) {
					y = 0;
				} else if((int)this.posY < 150) {
					y = Math.abs(y);
				} 

				y = MathHelper.clamp(y, -0.25, 0.25);

				if(shouldMove){
					mag = 0.35355339; //sqrt(2) / 4
					x = mag * Math.cos(theta);
					z = mag * Math.sin(theta);
					this.setMotion(x, y, z);
				} else {
					this.setMotion(0, y, 0);
				}
			} else {
				this.setMotion(0, 0, 0);
			}
		}

		super.livingTick();
	}

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

	static class ShootWindSeekerGoal extends Goal {
		private final AirBossEntity airBoss;
		private int shootTimer = 0;

		public ShootWindSeekerGoal(AirBossEntity entity) {
			this.airBoss = entity;
		}

		@Override
		public boolean shouldExecute() {
			return this.airBoss.getAttackTarget() instanceof PlayerEntity;
		}

		@Override
		public void startExecuting() {
			shootTimer = 0;
		}

		@Override
		public void tick() {
			if(shootTimer >= 200 && !this.airBoss.world.isRemote) {
				Entity target = this.airBoss.getAttackTarget();
				shootTimer = 0;
				double x = target.posX - this.airBoss.posX;
				double y = target.posY - this.airBoss.posY;
				double z = target.posZ - this.airBoss.posZ;	
				WindSeekerEntity windSeeker = new WindSeekerEntity(this.airBoss.world, this.airBoss, x, y, z);
				windSeeker.posY = this.airBoss.posY + (double)(this.airBoss.getHeight() / 2.0F) + 0.5D;
				this.airBoss.world.addEntity(windSeeker);
			} else {
				shootTimer++;
			}
			super.tick();
		}
	}

	static class ShootHailGoal extends Goal {
		private AirBossEntity airBoss;
		private int timer = 0;
		private int attackStep = 0;

		public ShootHailGoal(AirBossEntity entity) {
			this.airBoss = entity;
		}

		@Override
		public boolean shouldExecute() {
			return this.airBoss.isAlive() && this.airBoss.getAttackTarget() instanceof PlayerEntity;
		}

		@Override
		public void startExecuting() {
			timer = 0;
			attackStep = 0;
		}

		@Override
		public void tick() {
			if(!this.airBoss.world.isRemote) {
				if(timer >= 60) {
					if(attackStep < 10) {
						attackStep++;
						timer = 55;
						Entity target = this.airBoss.getAttackTarget();
						double x = target.posX - this.airBoss.posX;
						double y = target.posY - this.airBoss.posY;
						double z = target.posZ - this.airBoss.posZ;
						double yaw = Math.atan2(z, x);
						double pitch = Math.atan2(y, Math.sqrt(x * x + z * z));
						HailEntity hail = new HailEntity(this.airBoss.world, this.airBoss, x, y, z);
						hail.posX = this.airBoss.posX + MathHelper.clamp(x, -0.3, 0.3);
						hail.posY = this.airBoss.posY + (this.airBoss.getHeight() / 2) + 0.5;
						hail.posZ = this.airBoss.posZ + MathHelper.clamp(z, -0.3, 0.3);
						this.airBoss.world.addEntity(hail);
						hail.setMotion(0.707 * Math.cos(yaw), 0.707 * Math.sin(pitch), 0.707 * Math.sin(yaw));
					} else {
						timer = 0;
						attackStep = 0;
					}	
				} else {
					timer++;
				}
			} 

			super.tick();
		}
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
					phantom.setPosition(x, this.airBoss.posY, z);
					this.airBoss.world.addEntity(phantom);
					phantom.setAttackTarget(this.airBoss.getAttackTarget());
				}

				phantomSpawnTimer = 0;
			} else {
				phantomSpawnTimer++;
			}
			super.tick();
		}
	}
}