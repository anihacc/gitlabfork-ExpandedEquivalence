package com.zeitheron.expequiv.exp.ic2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import ic2.core.recipe.AdvRecipe;
import ic2.core.recipe.AdvShapelessRecipe;
import moze_intel.projecte.PECore;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class AdvRecipeEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(IRecipe recipe : CraftingManager.REGISTRY)
		{
			if(!(recipe instanceof AdvRecipe) && !(recipe instanceof AdvShapelessRecipe))
				continue;
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
		return "AdvCraftingMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for Adv Crafting Recipes gathered from net.minecraft.item.crafting.CraftingManager";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}