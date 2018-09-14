package com.zeitheron.expequiv.exp.actuallyadditions;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.LensConversionRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class LaserEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(LensConversionRecipe recipe : ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES)
		{
			ItemStack recipeOutput = recipe.getOutput();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			IngredientMap im = new IngredientMap();
			if(recipe.getInput() != null)
				im.addIngredient(CraftingIngredients.createFromMultipleItems(mapper, recipe.getInput().getMatchingStacks()), 1);
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "AALaserMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for laser recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}