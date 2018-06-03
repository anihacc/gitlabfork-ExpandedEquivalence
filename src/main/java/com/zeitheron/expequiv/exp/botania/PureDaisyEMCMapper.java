package com.zeitheron.expequiv.exp.botania;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NSSOreDictionary;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Configuration;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePureDaisy;

class PureDaisyEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes)
		{
			Object oi = recipe.getInput();
			IBlockState state = recipe.getOutputState();
			ItemStack output = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
			NormalizedSimpleStack input = null;
			if(oi instanceof String)
				input = NSSOreDictionary.create((String) oi);
			if(oi instanceof Block)
				input = NSSItem.create((Block) oi);
			
			IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
			im.addIngredient(input, 1);
			mapper.addConversion(output.getCount(), NSSItem.create(output), im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "BTPureDaisyMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for pure daisy recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}