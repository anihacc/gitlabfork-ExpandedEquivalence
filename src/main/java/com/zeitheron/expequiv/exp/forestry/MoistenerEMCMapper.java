package com.zeitheron.expequiv.exp.forestry;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import forestry.api.recipes.IMoistenerRecipe;
import forestry.factory.recipes.MoistenerRecipeManager;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class MoistenerEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
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
				boolean handled = false;
				ItemStack recipeOutput = recipe.getProduct();
				if(recipeOutput.isEmpty())
					continue;
				NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
				CraftingIngredients variation = CraftingIngredients.getIngredientsFor(Collections.emptyList(), recipe.getResource());
				IngredientMap ingredientMap = variation.toIngredients(mapper);
				if(ingredientMap == null)
					continue;
				mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
			}
	}
	
	@Override
	public String getName()
	{
		return "FMoistenerMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for moistener recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}