package trazormc.elementalswords.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import trazormc.elementalswords.holders.ModEntityTypes;

public class WindSeekerEntity extends DamagingProjectileEntity {

	public WindSeekerEntity(World world) {
		super(ModEntityTypes.WIND_SEEKER, world);
	}

	public WindSeekerEntity(EntityType<? extends WindSeekerEntity> entityType, World world) {
		super(entityType, world);
	}

	public WindSeekerEntity(World world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(ModEntityTypes.WIND_SEEKER, shooter, accelX, accelY, accelZ, world);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if(!this.world.isRemote) {
			if(result.getType() == RayTraceResult.Type.ENTITY) {
				Entity entity = ((EntityRayTraceResult)result).getEntity();
				if(this.shootingEntity != null && entity instanceof PlayerEntity) {
					entity.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 20.0f);
				}
			}
		}
		this.remove();
	}

	@Override
	public void tick() {
		super.tick();
		if(!this.world.isRemote) {
			if(this.shootingEntity instanceof AirBossEntity) {
				Entity target = ((AirBossEntity)this.shootingEntity).getAttackTarget();
				if(target != null) {
					double maxSpeed = 0.70710678; //sqrt(2) / 2
					double x = target.posX - this.posX;
					double y = target.posY - this.posY;
					double z = target.posZ - this.posZ;
					double mag = Math.sqrt(x * x + z * z);
					if(mag <= 100) {
						double pitch = Math.atan2(y, mag);
						double yaw = Math.atan2(z, x);
						if(mag <= 0.333 && Math.abs(y) >= 2) { //Helps a bit, but isn't the right solution for the weird rotation when directly above or below
							this.setMotion(0, maxSpeed * Math.sin(pitch), 0);
						} else {
							x = maxSpeed * Math.cos(yaw);
							y = maxSpeed * Math.sin(pitch);
							z = maxSpeed * Math.sin(yaw);	
							this.setMotion(x, y, z);
						}
					} else {
						this.remove();
					}
				} else {
					this.remove();
				}
			} else {
				this.remove();
			}
		}
	}
	
	@Override
	protected IParticleData getParticle() {
		return ParticleTypes.CLOUD;
	}

	@Override
	protected boolean isFireballFiery() {
		return false;
	}

	@Override
	public boolean isBurning() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(!this.world.isRemote) {
			if(source.getDamageType().equalsIgnoreCase("arrow")) {
				this.remove();
				return true;
			} else if(source.getTrueSource() != null){
				Vec3d vec3d = source.getTrueSource().getLookVec();
				this.setMotion(vec3d);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
