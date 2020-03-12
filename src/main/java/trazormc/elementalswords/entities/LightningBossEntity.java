package trazormc.elementalswords.entities;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
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
	private int lightningTimer = 0;
	private int summonMobsTimer = 300;

	public LightningBossEntity(EntityType<? extends LightningBossEntity> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 100;
		bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5f, false));
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
	public void livingTick() {
		super.livingTick();
		if(!this.world.isRaining()) {
			this.world.setRainStrength(0.5f);
			this.world.setThunderStrength(1.3f);
		} else if(!this.world.isThundering()) {
			this.world.setThunderStrength(1.3f);
		}
		
		if(this.attackingPlayer != null) {		
			if(lightningTimer >= 100 && !this.world.isRemote) {
				LightningBoltEntity lightning = new LightningBoltEntity(this.world, this.attackingPlayer.posX, this.attackingPlayer.posY, this.attackingPlayer.posZ, false);
				lightning.setPosition(this.attackingPlayer.posX, this.attackingPlayer.posY, this.attackingPlayer.posZ);
				if(!this.world.isRemote) {
					this.getServer().func_71218_a(dimension).addLightningBolt(lightning);
				}
				lightningTimer = 0;
			} else {
				lightningTimer++;
			}
			
			if(summonMobsTimer >= 300 && !this.world.isRemote) {   		
	    		if(this.rand.nextInt(2) == 0) {
	    			CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, this.world);
	    			int x = ModUtils.getPos(this.rand, 5, (int)this.attackingPlayer.posX);
	        		int z = ModUtils.getPos(this.rand, 5, (int)this.attackingPlayer.posZ);
	        		int y = ModUtils.calculateGenerationHeight(this.world, x, z) + 1;
	        		creeper.setPosition(x, y, z);	    
	        		this.world.addEntity(creeper);
	        		creeper.setAttackTarget(this.attackingPlayer);
	        		
	        		LightningBoltEntity lightning = new LightningBoltEntity(this.world, creeper.posX, creeper.posY, creeper.posZ, false);
	    			lightning.setPosition(creeper.posX, creeper.posY, creeper.posZ);
	    			this.world.addEntity(lightning);
	    		} 
	    		
	    		for(int i = 0; i <= 4; i++) {
	    			ZombiePigmanEntity pigman = new ZombiePigmanEntity(EntityType.ZOMBIE_PIGMAN, this.world);
	    			int x = ModUtils.getPos(this.rand, 5, (int)this.posX);
	           		int z = ModUtils.getPos(this.rand, 5, (int)this.posZ);
	           		int y = ModUtils.calculateGenerationHeight(this.world, x, z) + 1;
	           		pigman.setPosition(x, y, z);	           		
	           		this.world.addEntity(pigman);  
	           		pigman.setAttackTarget(this.attackingPlayer);
	    		}
	    			
	    		summonMobsTimer = 0;    		
			} else {
				summonMobsTimer++;
			}
		}
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		this.world.setRainStrength(0.0f);
		this.world.setThunderStrength(0.0f);
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
}
