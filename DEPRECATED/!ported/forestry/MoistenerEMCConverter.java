package com.zeitheron.expequiv.exp.forestry;

import java.lang.reflect.Field;
import java.util.Set;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import forestry.api.recipes.IMoistenerRecipe;
import forestry.factory.recipes.MoistenerRecipeManager;
import net.minecraft.item.ItemStack;

class MoistenerEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		Set<IMoistenerRecipe> recipes = null;
		
		// Get a set of all recipes in a moistener
		Field frecipes;
		try
		{
			frecipes = MoistenerRecipeManager.class.getDeclaredField("recipes");
			frecipes.setAccessible(true);
			recipes = (Set<IMoistenerRecipe>) frecipes.get(null);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1)
		{
			e1.printStackTrace();
		}
		
		if(recipes != null)
			for(IMoistenerRecipe recipe : recipes)
			{
				ItemStack recipeOutput = recipe.getProduct();
				if(recipeOutput.isEmpty())
					continue;
				emc.map(recipeOutput, CountedIngredient.create(recipe.getResource()));
			}
	}
}