package com.zeitheron.expequiv.exp.botania;

import com.zeitheron.expequiv.utils.CollectingHelper;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.collector.LongToBigFractionCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Configuration;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

class ManaPoolEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> map, Configuration config)
	{
		LongToBigFractionCollector<NormalizedSimpleStack, ?> mapper = CollectingHelper.getLTBFC(map);
		
		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes)
		{
			if(recipe.getInput() instanceof ItemStack)
			{
				ItemStack input = (ItemStack) recipe.getInput();
				ItemStack output = recipe.getOutput();
				
				if(input.isEmpty() || output.isEmpty())
					continue;
				
				NormalizedSimpleStack manaCost = NSSFake.create("mana." + recipe.hashCode());
				mapper.setValueBefore(manaCost, (long) MathHelper.ceil(recipe.getManaToConsume() / 10F));
				
				NormalizedSimpleStack out = NSSItem.create(output);
				NormalizedSimpleStack in = NSSItem.create(input);
				IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
				im.addIngredient(in, input.getCount());
				im.addIngredient(manaCost, 1);
				mapper.addConversion(output.getCount(), out, im.getMap());
			}
		}
	}
	
	@Override
	public String getName()
	{
		return "BTManaMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for mana pool recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}