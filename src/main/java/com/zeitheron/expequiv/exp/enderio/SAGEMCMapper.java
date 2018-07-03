package com.zeitheron.expequiv.exp.enderio;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import crazypants.enderio.base.recipe.IRecipe;
import crazypants.enderio.base.recipe.RecipeOutput;
import crazypants.enderio.base.recipe.sagmill.SagMillRecipeManager;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraftforge.common.config.Configuration;

class SAGEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		for(IRecipe r : SagMillRecipeManager.getInstance().getRecipes())
		{
			RecipeOutput o = r.getOutputs().length == 1 ? r.getOutputs()[0] : null;
			if(o == null || o.getChance() < 1F)
				continue;
			IngredientMap<NormalizedSimpleStack> im = CraftingIngredients.getIngredientsFor(r.getInputStackAlternatives()).toIngredients(mapper);
			mapper.addConversion(o.getOutput().getCount(), NSSItem.create(o.getOutput()), im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "EIOSAGMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for SAG Mill";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}