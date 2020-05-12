package trazormc.elementalswords.holders;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;
import trazormc.elementalswords.crafting.ImbuementShapelessRecipes;
import trazormc.elementalswords.ElementalSwords;

@ObjectHolder(ElementalSwords.MOD_ID)
public class ModRecipeSerializers {	
	public static IRecipeSerializer<ImbuementShapelessRecipes> IMBUEMENT_SHAPELESS = null;
}
