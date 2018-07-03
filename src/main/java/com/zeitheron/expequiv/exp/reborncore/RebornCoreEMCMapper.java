package com.zeitheron.expequiv.exp.reborncore;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;

public class RebornCoreEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		for(IBaseRecipeType recipe : RecipeHandler.recipeList)
			if(recipe.getOutputsSize() == 1)
			{
				ItemStack out = recipe.getOutput(0);
				mapper.addConversion(out.getCount(), NSSItem.create(out), CraftingIngredients.getIngredientsFor(recipe.getInputs()).toIngredients(mapper).getMap());
			}
	}
	
	@Override
	public String getName()
	{
		return "RCMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for base recipes from Reborn Core";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}