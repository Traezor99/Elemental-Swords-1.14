package trazormc.elementalswords.entities;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.FindWaterGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.ServerBossInfo;
import net.minecraft.world.World;
import trazormc.elementalswords.holders.ModEffects;

public class WaterBossEntity extends MonsterEntity {	
	private final ServerBossInfo bossInfo;
	private boolean swimmingUp;
	protected final SwimmerPathNavigator waterNavigator;
	protected final GroundPathNavigator groundNavigator;

	public WaterBossEntity(EntityType<? extends WaterBossEntity> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 100;	
		this.stepHeight = 1.0F;
		this.moveController = new MoveHelperController(this);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
		this.waterNavigator = new SwimmerPathNavigator(this, worldIn);
		this.groundNavigator = new GroundPathNavigator(this, worldIn);
		bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FindWaterGoal(this));
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5f, false));
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

	public void setSwimmingUp(boolean swimmingUp) {
		this.swimmingUp = swimmingUp;
	}

	protected boolean isCloseToPathTarget() {
		Path path = this.getNavigator().getPath();
		if(path != null) {
			BlockPos blockpos = path.func_224770_k();
			if(blockpos != null) {
				double d0 = this.getDistanceSq((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
				if(d0 < 4.0D) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean doSwimming() {
		if(this.swimmingUp) {
			return true;
		} else {
			LivingEntity livingentity = this.getAttackTarget();
			return livingentity != null && livingentity.isInWater();
		}
	}

	static class MoveHelperController extends MovementController {
		private final WaterBossEntity waterBoss;

		public MoveHelperController(WaterBossEntity waterBoss) {
			super(waterBoss);
			this.waterBoss = waterBoss;
		}

		public void tick() {
			LivingEntity livingentity = this.waterBoss.getAttackTarget();
			if(this.waterBoss.doSwimming() && this.waterBoss.isInWater()) {
				if(livingentity != null && livingentity.posY > this.waterBoss.posY || this.waterBoss.swimmingUp) {
					this.waterBoss.setMotion(this.waterBoss.getMotion().add(0.0D, 0.002D, 0.0D));
				}

				if(this.action != MovementController.Action.MOVE_TO || this.waterBoss.getNavigator().noPath()) {
					this.waterBoss.setAIMoveSpeed(0.0F);
					return;
				}

				double d0 = this.posX - this.waterBoss.posX;
				double d1 = this.posY - this.waterBoss.posY;
				double d2 = this.posZ - this.waterBoss.posZ;
				double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
				d1 = d1 / d3;
				float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
				this.waterBoss.rotationYaw = this.limitAngle(this.waterBoss.rotationYaw, f, 90.0F);
				this.waterBoss.renderYawOffset = this.waterBoss.rotationYaw;
				float f1 = (float)(this.speed * this.waterBoss.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
				float f2 = MathHelper.lerp(0.125F, this.waterBoss.getAIMoveSpeed(), f1);
				this.waterBoss.setAIMoveSpeed(f2);
				this.waterBoss.setMotion(this.waterBoss.getMotion().add((double)f2 * d0 * 0.005D, (double)f2 * d1 * 0.1D, (double)f2 * d2 * 0.005D));
			} else {
				if(!this.waterBoss.onGround) {
					this.waterBoss.setMotion(this.waterBoss.getMotion().add(0.0D, -0.008D, 0.0D));
				}

				super.tick();
			}

		}
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

	static class SwimUpGoal extends Goal {
		private final WaterBossEntity waterBoss;
		private final double swimSpeed;
		private final int targetY;
		private boolean obstructed;

		public SwimUpGoal(WaterBossEntity waterBoss, double swimSpeed, int targetYPos) {
			this.waterBoss = waterBoss;
			this.swimSpeed = swimSpeed;
			this.targetY = targetYPos;
		}

		public boolean shouldExecute() {
			return this.waterBoss.isInWater() && this.waterBoss.posY < (double)(this.targetY - 2);
		}

		public boolean shouldContinueExecuting() {
			return this.shouldExecute() && !this.obstructed;
		}

		public void tick() {
			if(this.waterBoss.posY < (double)(this.targetY - 1) && (this.waterBoss.getNavigator().noPath() || this.waterBoss.isCloseToPathTarget())) {
				Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.waterBoss, 4, 8, new Vec3d(this.waterBoss.posX, (double)(this.targetY - 1), this.waterBoss.posZ));
				if(vec3d == null) {
					this.obstructed = true;
					return;
				}

				this.waterBoss.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.swimSpeed);
			}

		}

		public void startExecuting() {
			this.waterBoss.setSwimmingUp(true);
			this.obstructed = false;
		}

		public void resetTask() {
			this.waterBoss.setSwimmingUp(false);
		}
	}
}
