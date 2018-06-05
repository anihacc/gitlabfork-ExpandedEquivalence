package com.zeitheron.expequiv.exp.embers;

import java.util.ArrayList;
import java.util.List;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFluid;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import teamroots.embers.recipe.AlchemyRecipe;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

class StamperEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		for(ItemStampingRecipe r : RecipeRegistry.stampingRecipes)
		{
			IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
			List<ItemStack> inputs = new ArrayList<>();
			ItemStack i = r.getStack();
			if(!i.isEmpty())
				im.addIngredient(NSSItem.create(i), i.getCount());
			if(r.getFluid() != null)
				im.addIngredient(NSSFluid.create(r.getFluid().getFluid()), r.getFluid().amount);
			mapper.addConversion(r.result.getCount(), NSSItem.create(r.result), im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "EBStamperMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for stamper recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}