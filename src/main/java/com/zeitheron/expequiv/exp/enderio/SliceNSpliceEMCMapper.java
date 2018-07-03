package com.zeitheron.expequiv.exp.enderio;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import crazypants.enderio.base.recipe.IManyToOneRecipe;
import crazypants.enderio.base.recipe.slicensplice.SliceAndSpliceRecipeManager;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraftforge.common.config.Configuration;

class SliceNSpliceEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		for(IManyToOneRecipe r : SliceAndSpliceRecipeManager.getInstance().getRecipes())
		{
			IngredientMap<NormalizedSimpleStack> im = CraftingIngredients.getIngredientsFor(r.getInputStackAlternatives()).toIngredients(mapper);
			mapper.addConversion(r.getOutput().getCount(), NSSItem.create(r.getOutput()), im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "EIOSNSMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for Slice'N'Splice";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}