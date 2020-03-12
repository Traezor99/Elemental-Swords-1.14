package trazormc.elementalswords.entities;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraftforge.fml.network.NetworkHooks;
import trazormc.elementalswords.init.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class ChargedFireballEntity extends AbstractFireballEntity {
	private int flightTime = 0;

	public ChargedFireballEntity(EntityType<? extends ChargedFireballEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public ChargedFireballEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(ModEntityTypes.ENTITY_CHARGED_FIREBALL, shooter, accelX, accelY, accelZ, worldIn);
	}

	public ChargedFireballEntity(World worldIn) {
		super(ModEntityTypes.ENTITY_CHARGED_FIREBALL, worldIn);
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
				Random rand = new Random();
				
				//Maybe make it a overkill explosion?
				this.world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 0.75f, Explosion.Mode.BREAK);

				BlockState state = Blocks.FIRE.getDefaultState();

				this.world.setBlockState(new BlockPos(pos.getX(), ModUtils.calculateGenerationHeight(world, pos.getX(), pos.getZ()) + 1, pos.getZ()), state);

				/*switch(rand.nextInt(2)) {      		
				case 0:        		
					this.world.setBlockState(new BlockPos(pos.getX() - 1, ModUtils.calculateGenerationHeight(this.world, pos.getX() - 1, pos.getZ()) + 1, pos.getZ()), state);
					this.world.setBlockState(new BlockPos(pos.getX(), ModUtils.calculateGenerationHeight(this.world, pos.getX(), pos.getZ() - 1) + 1, pos.getZ() - 1), state);
					this.world.setBlockState(new BlockPos(pos.getX() - 1, ModUtils.calculateGenerationHeight(this.world, pos.getX() - 1, pos.getZ() - 1) + 1, pos.getZ() - 1), state);
					break;
				case 1:
					this.world.setBlockState(new BlockPos(pos.getX() + 1, ModUtils.calculateGenerationHeight(this.world, pos.getX() + 1, pos.getZ()) + 1, pos.getZ()), state);
					this.world.setBlockState(new BlockPos(pos.getX(), ModUtils.calculateGenerationHeight(this.world, pos.getX(), pos.getZ() + 1) + 1, pos.getZ() + 1), state);
					this.world.setBlockState(new BlockPos(pos.getX() + 1, ModUtils.calculateGenerationHeight(this.world, pos.getX() + 1, pos.getZ() + 1) + 1, pos.getZ() + 1), state);
					break;
				} */                                                                                                     	
			}

			this.remove();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if(!this.world.isRemote) {
			if(flightTime < 100) 
				flightTime++;
			else
				this.remove();
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
