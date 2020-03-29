package trazormc.elementalswords.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import trazormc.elementalswords.init.ModEntityTypes;

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
				if(this.shootingEntity != null) {
					entity.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 30.0f);
				}
			}
		}
		this.remove();
	}

	@Override
	public void tick() {
		super.tick();
		if(this.shootingEntity instanceof AirBossEntity && !this.world.isRemote) {
			Entity target = ((AirBossEntity)this.shootingEntity).getAttackTarget();
			if(target != null) {
				double x = target.posX - this.posX;
				double y = MathHelper.clamp(target.posY - this.posY, -0.5, 0.5);
				double z = target.posZ - this.posZ;
				double mag = adjustSpeed(Math.sqrt(Entity.horizontalMag(new Vec3d(x, 0, z))), Math.sqrt(2) / 2);
				double theta = Math.atan2(z, x);
				x = mag * Math.cos(theta);
				z = mag * Math.sin(theta);	
				this.setMotion(x, y, z);
			}
		}
	}
	
	private double adjustSpeed(double speedIn, double max) {
		if(speedIn >= max) {
			return adjustSpeed(speedIn / 2, max);
		} else {
			return speedIn;
		}
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
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
