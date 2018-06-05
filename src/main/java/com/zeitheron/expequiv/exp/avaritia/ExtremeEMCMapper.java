package com.zeitheron.expequiv.exp.avaritia;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class ExtremeEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(IExtremeRecipe recipe : AvaritiaRecipeManager.EXTREME_RECIPES.values())
		{
			boolean handled = false;
			ItemStack recipeOutput = recipe.getRecipeOutput();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			CraftingIngredients variation = CraftingIngredients.getIngredientsFor(recipe.getIngredients());
			IngredientMap ingredientMap = variation.toIngredients(mapper);
			if(ingredientMap == null)
				continue;
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "ARExtremeMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for extreme recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}