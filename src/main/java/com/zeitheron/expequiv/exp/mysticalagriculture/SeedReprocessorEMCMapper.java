package com.zeitheron.expequiv.exp.mysticalagriculture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;
import com.blakebr0.mysticalagriculture.crafting.ReprocessorRecipe;
import com.zeitheron.expequiv.exp.CraftingIngredients;

import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class SeedReprocessorEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		for(ReprocessorRecipe recipe : ReprocessorManager.getRecipes())
		{
			ItemStack recipeOutput = recipe.getOutput();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			
			IngredientMap im = new IngredientMap();
			im.addIngredient(NSSItem.create(recipe.getInput()), recipe.getInput().getCount());
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "MASeedReprocessorMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for seed reprocessor";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}