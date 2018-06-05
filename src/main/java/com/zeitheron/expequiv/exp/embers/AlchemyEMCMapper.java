package com.zeitheron.expequiv.exp.embers;

import java.util.ArrayList;
import java.util.List;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import teamroots.embers.recipe.AlchemyRecipe;
import teamroots.embers.recipe.RecipeRegistry;

class AlchemyEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{

	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		for(AlchemyRecipe r : RecipeRegistry.alchemyRecipes)
		{
			IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
			List<ItemStack> inputs = new ArrayList<>();
			inputs.add(r.centerInput);
			inputs.addAll(r.inputs);
			for(ItemStack i : inputs)
				if(!i.isEmpty())
					im.addIngredient(NSSItem.create(i), i.getCount());
			mapper.addConversion(r.result.getCount(), NSSItem.create(r.result), im.getMap());
		}
	}

	@Override
	public String getName()
	{
		return "EBAlchemyMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for alchemy recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}