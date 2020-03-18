package trazormc.elementalswords.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModUtils {
	/**
	 * Starts from the world height and finds the top block at the specified x and z coordinates
	 * @param world the current world
	 * @param x the x coordinate
	 * @param z the z coordinate
	 * @return the y height of the top block at the specified coordinates
	 */
	public static int calculateGenerationHeight(World world, int x, int z) {
		int y = world.getHeight();
		boolean foundGround = false;
		
		while(!foundGround && y-- > 0) {
			Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundGround = block != Blocks.AIR;
		}
		
		return y;
	}
	
	/**
	 * Attempts to spawn the given entity in a safe location up to the specified number of times.
	 * @param bossIn the Boss spawning the entity
	 * @param spawnEntity the entity to be spawned
	 * @param attempts the number of attempts allowed to find a safe location.
	 */
	public static void attemptSpawnBossAdd(MonsterEntity bossIn, Entity spawnEntity, int attempts) {
		boolean repeat = false;
		int attemptsTried = 0;
		do {
			int x = ModUtils.getPos(new Random(), 5, (int)bossIn.posX);
			int z = ModUtils.getPos(new Random(), 5, (int)bossIn.posZ);
			int y = calculateMobSpawnHeight(bossIn.world, (int)bossIn.posY, x, z);  
			if(y != 0) {
				spawnEntity.setPosition(x, y, z);
				bossIn.world.addEntity(spawnEntity);
				repeat = false;
			}
			else {
				repeat = true;
				attemptsTried++;
			}
		} while(repeat && attemptsTried < attempts);
	}
	
	/**
	 * Used to find a safe height for spawning entities near a boss. Checks 10 blocks above and below the entity for a safe y height
	 * @param world the world to spawn the entity in
	 * @param yEntity the y height of the boss
	 * @param x the x coordinate to spawn the mob at
	 * @param z the z coordinate to spawn the mob at
	 * @return the safe y height, if none is found 0 is returned. Check that 0 is not returned before spawning.
	 */
	public static int calculateMobSpawnHeight(World world, int yEntity, double x, double z) {
		int y = yEntity + 10;
		boolean isSafe = false;
		
		while(y > yEntity - 10) {
			Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			Block block1 = world.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
			if(isSafe) {
				if(!block.equals(Blocks.AIR)) {
					return y - 1;
				} else {
					y--;
				}
			} else if(block.equals(Blocks.AIR) && block1.equals(Blocks.AIR)) {
				isSafe = true;
				y--;
			} else {
				y--;
			}
		}
		return 0;
	}
	
	/**
	 * Adds the specified potion effect to the player for 10 seconds. Checks that the effect is either not currently active 
	 * or is just about to end before doing so.
	 * @param player the player to receive the potion effect
	 * @param effect the effect to be applied
	 * @param amplifier the strength of the effect
	 */
	public static void effectPlayer(PlayerEntity player, Effect effect, int amplifier) {
		if(!player.isPotionActive(effect) || player.getActivePotionEffect(effect).getDuration() <= 1) {
			player.addPotionEffect(new EffectInstance(effect, 200, amplifier));
		}
	}
	
	/**
	 * Randomly generates a number within the specified radius of the player
	 * @param rand
	 * @param radius the radius for generation
	 * @param xzPos the player's x or z coordinate (method must be called once for each) 
	 * @return the x or z coordinate of the spawnpoint
	 */
	public static int getPos(Random rand, int radius, int xzPos) {	
		if(xzPos >= 0) {
			return rand.nextInt(radius * 2 + 1) + xzPos - radius;
		} else { 
			xzPos = xzPos * -1;
			return (rand.nextInt(radius * 2 + 1) + xzPos - radius) * -1;
		} 	
	}
	
	/**
	 * Checks which block the player clicked and plays the appropriate sound
	 * @param worldIn the current world
	 * @param player the player the sound will be played for
	 * @param pos the position clicked
	 */
	public static void playSound(World worldIn, PlayerEntity player, BlockPos pos) {
		for(MaterialSounds material : MaterialSounds.values()) {
			if(worldIn.getBlockState(pos) == Blocks.GRAVEL.getDefaultState()) {
				repeatSound(SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, worldIn, player, pos);
				break;
			} else if(worldIn.getBlockState(pos).getBlock() == Blocks.DEAD_BUSH) {
				repeatSound(SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, worldIn, player, pos);
				break;
			} else if(material.getMaterial() == worldIn.getBlockState(pos).getMaterial()) {
				repeatSound(material.getSound(), SoundCategory.BLOCKS, worldIn, player, pos);
			}
		}
	}
	
	/**
	 * Plays the specified sound 3 times
	 * @param soundIn
	 * @param categoryIn
	 * @param worldIn
	 * @param player
	 * @param pos
	 */
	private static void repeatSound(SoundEvent soundIn, SoundCategory categoryIn, World worldIn, PlayerEntity player, BlockPos pos) {
		for(int i = 0; i < 3; i++) {
			worldIn.playSound(player, pos, soundIn, categoryIn, 5.0f, 0.8f);
		}
	}
}
