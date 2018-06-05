package com.zeitheron.expequiv.exp.immersiveengineering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class CrusherEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		for(CrusherRecipe recipe : CrusherRecipe.recipeList)
		{
			ItemStack recipeOutput = recipe.output;
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			
			Object recipeItem = recipe.input;
			
			List<ItemStack> match = new ArrayList<>();
			if(recipeItem instanceof ItemStack)
				match.add((ItemStack) recipeItem);
			if(recipeItem instanceof String)
				match.addAll(OreDictionary.getOres(recipeItem + ""));
			if(recipeItem instanceof Ingredient)
				match.addAll(Arrays.asList(((Ingredient) recipeItem).getMatchingStacks()));
			
			CraftingIngredients variation = CraftingIngredients.getIngredientsFor(match);
			IngredientMap ingredientMap = variation.toIngredients(mapper);
			if(ingredientMap == null)
				continue;
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "IECrusherMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for crusher recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}