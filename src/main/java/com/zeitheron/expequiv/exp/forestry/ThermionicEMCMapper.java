package com.zeitheron.expequiv.exp.forestry;

import java.lang.reflect.Field;
import java.util.Set;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import forestry.api.recipes.IFabricatorRecipe;
import forestry.factory.recipes.FabricatorRecipeManager;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class ThermionicEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		Set<IFabricatorRecipe> recipes = null;
		
		// Get a set of all recipes in a fabricator
		Field frecipes;
		try
		{
			frecipes = FabricatorRecipeManager.class.getDeclaredField("recipes");
			frecipes.setAccessible(true);
			recipes = (Set<IFabricatorRecipe>) frecipes.get(null);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1)
		{
			e1.printStackTrace();
		}
		
		if(recipes != null)
			for(IFabricatorRecipe recipe : recipes)
			{
				boolean handled = false;
				ItemStack recipeOutput = recipe.getRecipeOutput();
				if(recipeOutput.isEmpty())
					continue;
				NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
				CraftingIngredients variation = CraftingIngredients.getIngredientsFor(recipe.getIngredients(), recipe.getPlan());
				IngredientMap ingredientMap = variation.toIngredients(mapper);
				if(ingredientMap == null)
					continue;
				mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
			}
	}
	
	@Override
	public String getName()
	{
		return "FThermionicMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for thermionic fabricator recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}