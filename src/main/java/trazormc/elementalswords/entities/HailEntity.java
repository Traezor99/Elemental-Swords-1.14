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
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import trazormc.elementalswords.init.ModEntityTypes;

public class HailEntity extends DamagingProjectileEntity {
	private int flightTime = 0;
	
	public HailEntity(World world) {
		super(ModEntityTypes.HAIL, world);
	}

	public HailEntity(EntityType<? extends HailEntity> entityType, World world) {
		super(entityType, world);
	}

	public HailEntity(World world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(ModEntityTypes.HAIL, shooter, accelX, accelY, accelZ, world);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if(!this.world.isRemote) {
			if(result.getType() == RayTraceResult.Type.ENTITY) {
				Entity entity = ((EntityRayTraceResult)result).getEntity();
				if(this.shootingEntity != null && entity instanceof PlayerEntity) {
					entity.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 7.0f);
				}
			}
		}
		this.remove();
	}
	
	@Override
	public void tick() {
		super.tick();
		if(!this.world.isRemote) {
			if(flightTime >= 100)
				this.remove();
			else
				flightTime++;
		}
	}
	
	@Override
	protected IParticleData getParticle() {
		return ParticleTypes.ITEM_SNOWBALL;
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
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
