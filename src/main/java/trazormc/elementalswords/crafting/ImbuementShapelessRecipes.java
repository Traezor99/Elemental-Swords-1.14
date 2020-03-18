package trazormc.elementalswords.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;
import trazormc.elementalswords.init.ModRecipeSerializers;

public class ImbuementShapelessRecipes implements ICraftingRecipe {	
	private final ResourceLocation id;
	private final String group;
	private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final boolean isSimple;
    
	public ImbuementShapelessRecipes(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> ingredients) {
		this.id = id;
	    this.group = group;
	    this.recipeOutput = output;
	    this.recipeItems = ingredients;
	    this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.IMBUEMENT_SHAPELESS;
	}
	 
	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		RecipeItemHelper helper = new RecipeItemHelper();
		List<ItemStack> inputs = new ArrayList<>();
		int i = 0;
		
		if(inv.getWidth() == 3 && inv.getHeight() == 1) { 
			for(int j = 0; j < inv.getSizeInventory(); ++j) {
				ItemStack stack = inv.getStackInSlot(j);
				if(!stack.isEmpty()) {
					++i;
					if(isSimple) {
						helper.func_221264_a(stack, 1);
					} else {
						inputs.add(stack);
					}
				}
			}
		}
		
		return i == recipeItems.size() && (isSimple ? helper.canCraft(this, (IntList)null) : RecipeMatcher.findMatches(inputs, recipeItems) != null);
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return recipeItems;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		return recipeOutput.copy();
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= this.recipeItems.size();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.recipeOutput;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public String getGroup() {
		return group;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ImbuementShapelessRecipes> {
	      
	      public ImbuementShapelessRecipes read(ResourceLocation recipeId, JsonObject json) {
	         String group = JSONUtils.getString(json, "group", "");
	         NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));	         
	         if (nonnulllist.isEmpty()) {
	             throw new JsonParseException("No ingredients for shapeless recipe");
	          } else if (nonnulllist.size() > 2) {
	             throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 2);
	          } else {
	        	  ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result")); 
	        	  return new ImbuementShapelessRecipes(recipeId, group, result, nonnulllist);	  
	          }
	      }
	      
	      private static NonNullList<Ingredient> readIngredients(JsonArray array) {
	          NonNullList<Ingredient> nonnulllist = NonNullList.create();

	          for(int i = 0; i < array.size(); ++i) {
	             Ingredient ingredient = Ingredient.deserialize(array.get(i));
	             if (!ingredient.hasNoMatchingItems()) {
	                nonnulllist.add(ingredient);
	             }
	          }

	          return nonnulllist;
	       }

	      public ImbuementShapelessRecipes read(ResourceLocation recipeId, PacketBuffer buffer) {
	         String group = buffer.readString(Short.MAX_VALUE);
	         int numIngredients = buffer.readVarInt();
	         NonNullList<Ingredient> ingredients = NonNullList.withSize(numIngredients, Ingredient.EMPTY);

	         for(int j = 0; j < ingredients.size(); ++j) {
	        	 ingredients.set(j, Ingredient.read(buffer));
	         }

	         ItemStack result = buffer.readItemStack();
	         return new ImbuementShapelessRecipes(recipeId, group, result, ingredients);
	      }

	      public void write(PacketBuffer buffer, ImbuementShapelessRecipes recipe) {
	         buffer.writeString(recipe.group);
	         buffer.writeVarInt(recipe.recipeItems.size());

	         for(Ingredient ingredient : recipe.recipeItems) {
	            ingredient.write(buffer);
	         }

	         buffer.writeItemStack(recipe.recipeOutput);
	      }
	 }
}
