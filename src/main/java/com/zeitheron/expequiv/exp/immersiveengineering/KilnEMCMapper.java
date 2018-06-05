package com.zeitheron.expequiv.exp.immersiveengineering;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class KilnEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		for(AlloyRecipe recipe : AlloyRecipe.recipeList)
		{
			ItemStack recipeOutput = recipe.output;
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			List<IngredientStack> inputs = new ArrayList<>();
			if(recipe.input0 != null)
				inputs.add(recipe.input0);
			if(recipe.input1 != null)
				inputs.add(recipe.input1);
			CraftingIngredients variation = CraftingIngredients.getIngredientsFor(inputs.stream().map(is -> is.getSizedStackList()).collect(Collectors.toList()));
			IngredientMap ingredientMap = variation.toIngredients(mapper);
			if(ingredientMap == null)
				continue;
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "IEKilnMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for kiln recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}