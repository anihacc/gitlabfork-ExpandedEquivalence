package com.zeitheron.expequiv.exp.compactmachines3;

import java.util.ArrayList;
import java.util.Collections;

import org.dave.compactmachines3.miniaturization.MultiblockRecipe;
import org.dave.compactmachines3.miniaturization.MultiblockRecipes;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class MiniaturizationEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(MultiblockRecipe recipe : MultiblockRecipes.getRecipes())
		{
			ItemStack recipeOutput = recipe.getTargetStack();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			
			ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
			fixedInputs.add(recipe.getCatalystStack().copy());
			for(ItemStack is : recipe.getRequiredItemStacks())
				fixedInputs.add(is.copy());
			
			CraftingIngredients variation = new CraftingIngredients(fixedInputs, new ArrayList<>());
			IngredientMap ingredientMap = variation.toIngredients(mapper);
			if(ingredientMap == null)
				continue;
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "ASStarlightMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for starlight infusion recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}