package com.zeitheron.expequiv.exp.actuallyadditions;

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
			ItemStack recipeOutput = recipe.output;
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			IngredientMap im = new IngredientMap();
			
			if(!recipe.input.isEmpty())
				im.addIngredient(NSSItem.create(recipe.input), recipe.input.getCount());
			if(!recipe.modifier1.isEmpty())
				im.addIngredient(NSSItem.create(recipe.modifier1), recipe.modifier1.getCount());
			if(!recipe.modifier2.isEmpty())
				im.addIngredient(NSSItem.create(recipe.modifier2), recipe.modifier2.getCount());
			if(!recipe.modifier3.isEmpty())
				im.addIngredient(NSSItem.create(recipe.modifier3), recipe.modifier3.getCount());
			if(!recipe.modifier4.isEmpty())
				im.addIngredient(NSSItem.create(recipe.modifier4), recipe.modifier4.getCount());
			
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