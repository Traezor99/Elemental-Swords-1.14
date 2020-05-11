package trazormc.elementalswords.util;

import net.minecraft.block.material.Material;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum MaterialSounds {	
	ORGANIC(Material.ORGANIC, SoundEvents.BLOCK_GRAVEL_BREAK),
	TALL_PLANTS(Material.TALL_PLANTS, SoundEvents.BLOCK_GRAVEL_BREAK), //check if dead bush
	PLANTS(Material.PLANTS, SoundEvents.BLOCK_GRAVEL_BREAK),
	SPONGE(Material.SPONGE, SoundEvents.BLOCK_GRAVEL_BREAK),
	REDSTONE_LIGHT(Material.REDSTONE_LIGHT, SoundEvents.BLOCK_GLASS_BREAK),
	GLASS(Material.GLASS, SoundEvents.BLOCK_GLASS_BREAK),
	ICE(Material.ICE, SoundEvents.BLOCK_GLASS_BREAK),
	PACKED_ICE(Material.PACKED_ICE, SoundEvents.BLOCK_GLASS_BREAK),
	PORTAL(Material.PORTAL, SoundEvents.BLOCK_GLASS_BREAK),
	ANVIL(Material.ANVIL, SoundEvents.BLOCK_METAL_BREAK),
	WEB(Material.WEB, SoundEvents.BLOCK_METAL_BREAK),
	ROCK(Material.ROCK, SoundEvents.BLOCK_METAL_BREAK),
	BARRIER(Material.BARRIER, SoundEvents.BLOCK_METAL_BREAK),
	IRON(Material.IRON, SoundEvents.BLOCK_METAL_BREAK),
	CLAY(Material.CLAY, SoundEvents.BLOCK_GRAVEL_BREAK),
	MISCELLANEOUS(Material.MISCELLANEOUS, SoundEvents.BLOCK_METAL_BREAK),
	SAND(Material.SAND, SoundEvents.BLOCK_SAND_BREAK), //Check if gravel
	CACTUS(Material.CACTUS, SoundEvents.BLOCK_SAND_BREAK),
	WOOD(Material.WOOD, SoundEvents.BLOCK_WOOD_BREAK),
	GOURD(Material.GOURD, SoundEvents.BLOCK_WOOD_BREAK),
	SNOW(Material.SNOW, SoundEvents.BLOCK_SNOW_BREAK),
	SNOW_BLOCK(Material.SNOW_BLOCK, SoundEvents.BLOCK_SNOW_BREAK),
	CARPET(Material.CARPET, SoundEvents.BLOCK_SNOW_BREAK),
	WOOL(Material.WOOL, SoundEvents.BLOCK_SNOW_BREAK),
	LEAVES(Material.LEAVES, SoundEvents.BLOCK_GRASS_BREAK),
	EARTH(Material.EARTH, SoundEvents.BLOCK_GRAVEL_BREAK);
	
	
	Material material;
	SoundEvent sound;
	
	private MaterialSounds(Material material, SoundEvent sound) {
		this.material = material;
		this.sound = sound;
	}
	
	public SoundEvent getSound() {
		return sound;
	}
	
	public Material getMaterial() {
		return material;
	}
	
}
