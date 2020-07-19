package trazormc.elementalswords.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import trazormc.elementalswords.holders.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class BubbleEntity extends DamagingProjectileEntity implements IRendersAsItem {

	public BubbleEntity(World world) {
		super(ModEntityTypes.BUBBLE, world);
	}

	public BubbleEntity(EntityType<? extends BubbleEntity> entityType, World world) {
		super(entityType, world);
	}

	public BubbleEntity(World world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(ModEntityTypes.BUBBLE, shooter, accelX, accelY, accelZ, world);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if(!this.world.isRemote) {
			if(result.getType() == RayTraceResult.Type.ENTITY) {
				Entity entity = ((EntityRayTraceResult)result).getEntity();
				if(this.shootingEntity != null && entity instanceof PlayerEntity) {
					entity.attackEntityFrom(DamageSource.GENERIC, 20.0f);
				}
			}
		}
		this.remove();
	}

	@Override
	public void tick() {
		super.tick();
		if(!this.world.isRemote) {
			if(this.shootingEntity instanceof WaterBossEntity) {
				Entity target = ((WaterBossEntity)this.shootingEntity).getAttackTarget();
				if(target != null && this.isInWaterOrBubbleColumn()) {
					double maxSpeed = 0.3535533906; //sqrt(2) / 4
					double x = target.posX - this.posX;
					double y = target.posY - this.posY;
					double z = target.posZ - this.posZ;
					double mag = Math.sqrt(x * x + z * z);
					if(mag <= 50) {
						double pitch = Math.atan2(y, mag);
						double yaw = Math.atan2(z, x);
						x = maxSpeed * Math.cos(yaw);
						y = maxSpeed * Math.sin(pitch);
						z = maxSpeed * Math.sin(yaw);	
						this.setMotion(x, y, z);
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
		
		this.world.addParticle(this.getParticle(), this.posX, this.posY + 0.25, this.posZ, 0, 0, 0);
		this.world.addParticle(this.getParticle(), this.posX + 0.2, this.posY + 0.25, this.posZ, 0, 0, 0);
		this.world.addParticle(this.getParticle(), this.posX - 0.2, this.posY + 0.25, this.posZ, 0, 0, 0);
		this.world.addParticle(this.getParticle(), this.posX, this.posY + 0.25, this.posZ + 0.2, 0, 0, 0);
		this.world.addParticle(this.getParticle(), this.posX, this.posY + 0.25, this.posZ - 0.2, 0, 0, 0);		
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(!this.world.isRemote) {
			if(source.getDamageType().equalsIgnoreCase("trident")) {
				this.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	protected IParticleData getParticle() {
		return ParticleTypes.BUBBLE;
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
	public ItemStack getItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
