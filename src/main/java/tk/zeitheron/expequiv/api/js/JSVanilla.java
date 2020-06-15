package tk.zeitheron.expequiv.api.js;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import tk.zeitheron.expequiv.api.CountedIngredient;
import tk.zeitheron.expequiv.api.IEMC;

import java.util.ArrayList;
import java.util.List;

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
	
	public static void handleIRecipe(IEMC emc, IRecipe recipe)
	{
		ItemStack output = recipe.getRecipeOutput();
		if(output.isEmpty()) return;
		List<CountedIngredient> im = new ArrayList<>();
		recipe.getIngredients().forEach(ingredient ->
		{
			if(!JSIngredient.isEmpty(ingredient))
			{
				CountedIngredient ci = CountedIngredient.tryCreate(emc, ingredient);
				if(im != null) im.add(ci);
			}
		});
		emc.map(output, im);
	}
}