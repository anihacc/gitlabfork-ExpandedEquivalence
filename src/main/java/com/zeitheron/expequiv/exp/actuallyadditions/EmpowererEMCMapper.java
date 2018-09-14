package com.zeitheron.expequiv.exp.actuallyadditions;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.EmpowererRecipe;
import de.ellpeck.actuallyadditions.api.recipe.LensConversionRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class EmpowererEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(EmpowererRecipe recipe : ActuallyAdditionsAPI.EMPOWERER_RECIPES)
		{
			ItemStack recipeOutput = recipe.getOutput();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			IngredientMap im = new IngredientMap();
			
			im.addIngredient(CraftingIngredients.createFromMultipleItems(mapper, recipe.getInput().getMatchingStacks()), 1);
			if(recipe.getStandOne() != null)
				im.addIngredient(CraftingIngredients.createFromMultipleItems(mapper, recipe.getStandOne().getMatchingStacks()), 1);
			if(recipe.getStandTwo() != null)
				im.addIngredient(CraftingIngredients.createFromMultipleItems(mapper, recipe.getStandTwo().getMatchingStacks()), 1);
			if(recipe.getStandThree() != null)
				im.addIngredient(CraftingIngredients.createFromMultipleItems(mapper, recipe.getStandThree().getMatchingStacks()), 1);
			if(recipe.getStandFour() != null)
				im.addIngredient(CraftingIngredients.createFromMultipleItems(mapper, recipe.getStandFour().getMatchingStacks()), 1);
			
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "AAEmpowererMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for empowerer recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}