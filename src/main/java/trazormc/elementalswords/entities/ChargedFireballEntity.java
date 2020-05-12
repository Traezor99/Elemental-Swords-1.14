package trazormc.elementalswords.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;
import trazormc.elementalswords.holders.ModEntityTypes;

public class ChargedFireballEntity extends AbstractFireballEntity {
	private int flightTime = 0;

	public ChargedFireballEntity(EntityType<? extends ChargedFireballEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public ChargedFireballEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(ModEntityTypes.CHARGED_FIREBALL, shooter, accelX, accelY, accelZ, worldIn);
	}

	public ChargedFireballEntity(World worldIn) {
		super(ModEntityTypes.CHARGED_FIREBALL, worldIn);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if(!this.world.isRemote) {
			if(result.getType() == RayTraceResult.Type.ENTITY) {
				Entity entity = ((EntityRayTraceResult)result).getEntity();
				if(!(entity instanceof FireBossEntity)) {	     
					entity.attackEntityFrom(DamageSource.causeExplosionDamage(shootingEntity), 30.0F);
					this.applyEnchantments(this.shootingEntity, entity);
					entity.setFire(7);            		
				}
			} else {
				BlockPos pos = ((BlockRayTraceResult)result).getPos();  				

				Explosion.Mode explosionMode = ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
				this.world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), 2.0f, true, explosionMode);                                                                                                     	
			}

			this.remove();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if(!this.world.isRemote) {
			if(this.isWet() || flightTime >= 100)
				this.remove();
			else
				flightTime++;
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
