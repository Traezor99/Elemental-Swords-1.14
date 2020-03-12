package trazormc.elementalswords.world.dimensions.amethyst;

import java.util.function.BiFunction;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import trazormc.elementalswords.ElementalSwords;

public class AmethystModDimension extends ModDimension {
	
	public AmethystModDimension(String name) {
		this.setRegistryName(ElementalSwords.MOD_ID, name);
	}

	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
		return AmethystDimension::new;
	}
	
}
