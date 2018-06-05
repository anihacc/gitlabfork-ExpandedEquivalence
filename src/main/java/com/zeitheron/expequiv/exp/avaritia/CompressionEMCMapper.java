package com.zeitheron.expequiv.exp.avaritia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class CompressionEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(ICompressorRecipe recipe : AvaritiaRecipeManager.COMPRESSOR_RECIPES.values())
		{
			boolean handled = false;
			ItemStack recipeOutput = recipe.getResult();
			if(recipeOutput.isEmpty())
				continue;
			int cost = recipe.getCost();
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			handled = true;
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
		return "ARCompression";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for neutronium compressor";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}