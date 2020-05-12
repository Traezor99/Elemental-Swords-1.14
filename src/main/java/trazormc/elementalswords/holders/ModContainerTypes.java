package trazormc.elementalswords.holders;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.containers.ImbuementTableContainer;

@ObjectHolder(ElementalSwords.MOD_ID)
public class ModContainerTypes {
	public static ContainerType<ImbuementTableContainer> IMBUEMENT_TABLE = null;
}
