package com.zeitheron.expequiv.exp.ic2;

import java.util.Collection;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class DefaultIC2MachineEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	final IBasicMachineRecipeManager registry;
	
	public DefaultIC2MachineEMCMapper(IBasicMachineRecipeManager registry)
	{
		this.registry = registry;
	}
	
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe : registry.getRecipes())
			if(recipe.getOutput().size() == 1)
			{
				boolean handled = false;
				ItemStack recipeOutput = recipe.getOutput().stream().findFirst().orElse(ItemStack.EMPTY);
				if(recipeOutput.isEmpty())
					continue;
				NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
				
				CraftingIngredients variation = CraftingIngredients.getIngredientsFor(recipe.getInput().getInputs());
				
				IngredientMap ingredientMap = variation.toIngredients(mapper);
				if(ingredientMap == null)
					continue;
				mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
			}
	}
	
	@Override
	public String getName()
	{
		return "IC2MachineMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for machine recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}