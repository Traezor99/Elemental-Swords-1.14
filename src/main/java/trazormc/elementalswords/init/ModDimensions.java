package trazormc.elementalswords.init;

import net.minecraftforge.registries.ObjectHolder;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.world.dimensions.amethyst.AmethystModDimension;
import trazormc.elementalswords.world.dimensions.fire.FireModDimension;

@ObjectHolder(ElementalSwords.MOD_ID)
public class ModDimensions {	
	public static AmethystModDimension AMETHYST_DIMENSION = null;
	public static FireModDimension FIRE_DIMENSION = null;
}
