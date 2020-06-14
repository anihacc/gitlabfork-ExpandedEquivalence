package tk.zeitheron.expequiv.api.js;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class JSVanilla
{
	public static Iterable<IRecipe> getCraftingRecipes()
	{
		return CraftingManager.REGISTRY;
	}
	
	public static ItemStack getRecipeOutput(IRecipe recipe)
	{
		return recipe.getRecipeOutput();
	}
	
	public static NonNullList<Ingredient> getIngredients(IRecipe recipe)
	{
		return recipe.getIngredients();
	}
}