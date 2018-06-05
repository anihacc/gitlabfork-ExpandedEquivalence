package com.zeitheron.expequiv.exp.draconicevolution;

import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe;
import com.zeitheron.expequiv.exp.CraftingIngredients;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class FusionEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(IFusionRecipe recipe : FusionRecipeAPI.getRecipes())
		{
			boolean handled = false;
			ItemStack recipeOutput = recipe.getRecipeOutput(ItemStack.EMPTY);
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			CraftingIngredients variation = CraftingIngredients.getIngredientsFor(recipe.getRecipeIngredients(), recipe.getRecipeCatalyst());
			IngredientMap ingredientMap = variation.toIngredients(mapper);
			if(ingredientMap == null) continue;
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "DEFusionMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for fusion recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}