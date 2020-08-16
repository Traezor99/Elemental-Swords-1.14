package trazormc.elementalswords.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.LookController;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.ServerBossInfo;
import net.minecraft.world.World;
import trazormc.elementalswords.holders.ModEffects;
import trazormc.elementalswords.util.ModUtils;

public class WaterBossEntity extends MonsterEntity {	
	private final ServerBossInfo bossInfo;

	public WaterBossEntity(EntityType<? extends WaterBossEntity> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 100;	
		this.lookController = new LookController(this);
		bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FindWaterGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5f, false));
		this.goalSelector.addGoal(2, new WaterBossEntity.ShootBubbles(this));
		this.goalSelector.addGoal(3, new WaterBossEntity.ForceDrownTargetGoal(this));
		this.goalSelector.addGoal(4, new WaterBossEntity.MoveGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
		//this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 7.0F));
		//this.goalSelector.addGoal(9, new LookRandomlyGoal(this));

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
		if(this.world.getSeaLevel() - this.posY <= 10)
			this.setMotion(0, -0.25, 0);
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

	private class ShootBubbles extends Goal {
		private WaterBossEntity waterBoss;
		private int timer = 0;

		private ShootBubbles(WaterBossEntity entity) {
			this.waterBoss = entity;
		}		

		@Override
		public boolean shouldExecute() {
			return this.waterBoss.getAttackTarget() instanceof PlayerEntity;
		}

		@Override
		public void startExecuting() {
			timer = 0;
		}

		@Override
		public void tick() {
			if(!this.waterBoss.world.isRemote) {
				if(timer >= 200) {
					Entity target = this.waterBoss.getAttackTarget();
					timer = 0;
					double x = target.posX - this.waterBoss.posX;
					double y = target.posY - this.waterBoss.posY;
					double z = target.posZ - this.waterBoss.posZ;	
					BubbleEntity bubble = new BubbleEntity(this.waterBoss.world, this.waterBoss, x, y, z);
					bubble.posX = this.waterBoss.posX + MathHelper.clamp(x, -0.3, 0.3);
					bubble.posY = this.waterBoss.posY + (double)(this.waterBoss.getHeight() / 2.0F) + 0.5D;
					bubble.posZ = this.waterBoss.posZ + MathHelper.clamp(z, -0.3, 0.3);
					this.waterBoss.world.addEntity(bubble);
				} else {
					timer++;
				}
			}

			super.tick();
		}

	}

	private class MoveGoal extends Goal {
		private WaterBossEntity waterBoss;
		private BlockPos targetPos;
		private int timer = 0;
		private boolean hasTarget = false;

		private MoveGoal(WaterBossEntity entity) {
			this.waterBoss = entity;
			this.targetPos = waterBoss.getPosition();
		}

		@Override
		public boolean shouldExecute() {
			return this.waterBoss.isInWaterOrBubbleColumn() && this.waterBoss.world.getSeaLevel() - this.waterBoss.posY >= 11 && 
					this.waterBoss.getAttackTarget() instanceof PlayerEntity;
		}

		@Override
		public void startExecuting() {
			timer = 0;				
		}

		@Override
		public void tick() {
			if(!this.waterBoss.world.isRemote) { 
				if(timer >= 200) {
					if(!hasTarget)
						targetPos = getTarget();				
					if(this.waterBoss.isInWaterOrBubbleColumn() && !reachedTargetPos()) {
						double maxSpeed = 0.1767766953; //sqrt(2) / 8
						double x = targetPos.getX() - this.waterBoss.posX;
						double y = targetPos.getY() - this.waterBoss.posY;
						double z = targetPos.getZ() - this.waterBoss.posZ;
						double mag = Math.sqrt(x * x + z * z);
						double pitch = Math.atan2(y, mag);
						double yaw = Math.atan2(z, x);
						//this.waterBoss.rotationYaw = (float)(yaw * (180 / Math.PI)) - 90; //Weird rotation when in survival
						x = maxSpeed * Math.cos(yaw);
						y = maxSpeed * Math.sin(pitch);
						z = maxSpeed * Math.sin(yaw);			
						this.waterBoss.setMotion(x, y, z);
					} else {
						timer = 0;
						hasTarget = false;
					}
				} else {
					timer++;
					this.waterBoss.getLookController().setLookPositionWithEntity(this.waterBoss.getAttackTarget(), 90.0f, 90.0f);
					this.waterBoss.addVelocity(0, 0.005, 0); //Keeps it from sinking
				}
			}
			super.tick();
		}

		private boolean reachedTargetPos() {
			BlockPos current = this.waterBoss.getPosition();
			return Math.abs(targetPos.getX() - current.getX()) <= 1 && Math.abs(targetPos.getY() - current.getY()) <= 1
					&& Math.abs(targetPos.getZ() - current.getZ()) <= 1;
		}

		private BlockPos getTarget() {
			BlockPos pos;
			do {
				double x = ModUtils.getPos(getRNG(), 15, this.waterBoss.posX);
				double y = this.waterBoss.world.getSeaLevel() - this.waterBoss.posY >= 16 ? 
						ModUtils.getPos(getRNG(), 5, this.waterBoss.posY) : ModUtils.getPos(getRNG(), 5, this.waterBoss.posY - 6);
						double z = ModUtils.getPos(getRNG(), 15, this.waterBoss.posZ);
						pos = new BlockPos(x, y, z);
			}
			while(this.waterBoss.world.getBlockState(pos).getBlock() != Blocks.WATER);
			hasTarget = true;
			return pos;
		}	
	}

	private class ForceDrownTargetGoal extends Goal {
		private WaterBossEntity waterBoss;
		private int timer = 0;

		private ForceDrownTargetGoal(WaterBossEntity entity) {
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
				PlayerEntity player = (PlayerEntity)this.waterBoss.getAttackTarget();
				player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100));
				player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 2));
				player.addPotionEffect(new EffectInstance(ModEffects.FORCED_DROWN, 100, 2));
			} else {
				this.timer++;
			}
			super.tick();
		}
	}
}
