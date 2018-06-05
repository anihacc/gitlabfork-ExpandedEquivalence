package com.zeitheron.expequiv.exp.buildcraft;

import java.util.Set;
import java.util.stream.Collectors;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import buildcraft.api.recipes.AssemblyRecipe;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.lib.recipe.AssemblyRecipeRegistry;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class LaserTableEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(AssemblyRecipe recipe : AssemblyRecipeRegistry.REGISTRY.values())
		{
			ItemStack recipeOutput = recipe.getOutputPreviews().stream().findFirst().orElse(ItemStack.EMPTY);
			Set<IngredientStack> ingredients = recipe.getInputsFor(recipeOutput.copy());
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			CraftingIngredients variation = CraftingIngredients.getIngredientsFor(ingredients.stream().map(s -> s.ingredient).collect(Collectors.toList()));
			IngredientMap ingredientMap = variation.toIngredients(mapper);
			if(ingredientMap == null)
				continue;
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "BCLaserTableMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for laser table recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}