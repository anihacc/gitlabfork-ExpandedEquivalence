package com.zeitheron.expequiv.exp.appeng;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.zeitheron.expequiv.utils.EMCUtils;

import appeng.api.AEApi;
import appeng.api.features.IGrinderRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

class GrinderEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		for(IGrinderRecipe recipe : AEApi.instance().registries().grinder().getRecipes())
		{
			ItemStack input = recipe.getInput();
			ItemStack output = recipe.getOutput();
			
			IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
			im.addIngredient(NSSItem.create(input), input.getCount());
			mapper.addConversion(output.getCount(), NSSItem.create(output), im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "AE2GrinderMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for grinder recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}