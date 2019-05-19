package com.zeitheron.expequiv.exp.forestry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import forestry.api.recipes.ICarpenterRecipe;
import forestry.factory.recipes.CarpenterRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

class CarpenterEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		Set<ICarpenterRecipe> recipes = null;
		
		// Get a set of all recipes in a carpenter
		Field frecipes;
		try
		{
			frecipes = CarpenterRecipeManager.class.getDeclaredField("recipes");
			frecipes.setAccessible(true);
			recipes = (Set<ICarpenterRecipe>) frecipes.get(null);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1)
		{
			e1.printStackTrace();
		}
		
		if(recipes != null)
			for(ICarpenterRecipe recipe : recipes)
			{
				ItemStack recipeOutput = recipe.getBox();
				if(recipeOutput.isEmpty())
					continue;
				List<CountedIngredient> ci = new ArrayList<>();
				for(NonNullList<ItemStack> inputs : recipe.getCraftingGridRecipe().getRawIngredients())
					ci.add(FakeItem.create(emc, 1, Ingredient.fromStacks(inputs.toArray(new ItemStack[inputs.size()]))));
				emc.map(recipeOutput, ci);
			}
	}
}