package trazormc.elementalswords.entities;

import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.BlockPos;
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
		bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FindWaterGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5f, false));
		this.goalSelector.addGoal(4, new WaterBossEntity.ForceDrownTargetGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new WaterBossEntity.MoveGoal(this));
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
		if(this.world.getBlockState(this.getPosition().up(10)).getBlock() != Blocks.WATER)
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
	
	private class MoveGoal extends Goal {
		private WaterBossEntity waterBoss;
		private BlockPos targetPos;
		private int timer = 0;
		
		private MoveGoal(WaterBossEntity entity) {
			this.waterBoss = entity;
			this.targetPos = waterBoss.getPosition();
		}

		@Override
		public boolean shouldExecute() {
			return this.waterBoss.isInWaterOrBubbleColumn() && 
					this.waterBoss.world.getBlockState(this.waterBoss.getPosition().up(10)).getBlock() == Blocks.WATER;
		}
		
		@Override
		public void startExecuting() {
			timer = 0;
			do {
				this.targetPos = getTarget();
			}
			while(this.waterBoss.world.getBlockState(targetPos).getBlock() != Blocks.WATER);				
		}
		
		@Override
		public void tick() {
			if(!this.waterBoss.world.isRemote) { 
				if(timer >= 200 ) {
					if(this.waterBoss.isInWaterOrBubbleColumn() && targetPos.distanceSq(waterBoss.getPosition()) > 1) { //Does weird stuff when close to target
						double maxSpeed = 0.1767766953; //sqrt(2) / 8
						double x = targetPos.getX() - this.waterBoss.posX;
						double y = targetPos.getY() - this.waterBoss.posY;
						double z = targetPos.getZ() - this.waterBoss.posZ;
						double mag = Math.sqrt(x * x + z * z);
						double pitch = Math.atan2(y, mag);
						double yaw = Math.atan2(z, x);
						x = maxSpeed * Math.cos(yaw);
						y = maxSpeed * Math.sin(pitch);
						z = maxSpeed * Math.sin(yaw);			
						this.waterBoss.setMotion(x, y, z);
					} else {
						this.startExecuting();
					}
				} else {
					timer++;
				}
			}
			super.tick();
		}
		
		private BlockPos getTarget() {
			double x = ModUtils.getPos(getRNG(), 15, this.waterBoss.posX);
			double y = ModUtils.getPos(getRNG(), 5, this.waterBoss.posY - 6);
			double z = ModUtils.getPos(getRNG(), 15, this.waterBoss.posZ);
			return new BlockPos(x, y, z);
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
