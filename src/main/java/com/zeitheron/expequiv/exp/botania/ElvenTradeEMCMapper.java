package com.zeitheron.expequiv.exp.botania;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;

class ElvenTradeEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes)
		{
			IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
			for(Object o : recipe.getInputs())
				if(o instanceof ItemStack)
				{
					ItemStack is = (ItemStack) o;
					im.addIngredient(NSSItem.create(is), is.getCount());
				}
			ItemStack output = recipe.getOutputs().stream().filter(s -> !s.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
			if(!output.isEmpty())
				mapper.addConversion(output.getCount(), NSSItem.create(output), im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "BTElvenMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for elven trade recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}