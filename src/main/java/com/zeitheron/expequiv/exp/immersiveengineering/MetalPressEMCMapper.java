package com.zeitheron.expequiv.exp.immersiveengineering;

import java.util.stream.Collectors;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class MetalPressEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		for(MetalPressRecipe recipe : MetalPressRecipe.recipeList.values())
		{
			ItemStack recipeOutput = recipe.output;
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			CraftingIngredients variation = CraftingIngredients.getIngredientsFor(recipe.getItemInputs().stream().map(is -> is.getSizedStackList()).collect(Collectors.toList()));
			IngredientMap ingredientMap = variation.toIngredients(mapper);
			if(ingredientMap == null)
				continue;
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "IEMetalPressMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for metal press recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}