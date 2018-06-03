package com.zeitheron.expequiv.exp.appeng;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.endie.lib.fast.lists.IntArrayList;
import com.endie.lib.fast.lists.IntList;

import appeng.api.AEApi;
import appeng.api.features.IInscriberRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

class InscriberEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		Function<String, Item> find = reg -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", reg));
		Item material = find.apply("material");
		IntList presses = new IntArrayList();
		presses.addInt(13);
		presses.addInt(14);
		presses.addInt(15);
		presses.addInt(19);
		
		for(IInscriberRecipe recipe : AEApi.instance().registries().inscriber().getRecipes())
		{
			IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
			List<ItemStack> inputs = new ArrayList<>();
			inputs.add(recipe.getInputs().get(0));
			inputs.add(recipe.getTopOptional().orElse(ItemStack.EMPTY));
			inputs.add(recipe.getBottomOptional().orElse(ItemStack.EMPTY));
			for(ItemStack in : inputs)
			{
				boolean press = !in.isEmpty() && in.getItem() == material && presses.contains(in.getItemDamage());
				if(!in.isEmpty() && !press)
					im.addIngredient(NSSItem.create(in), in.getCount());
			}
			ItemStack out = recipe.getOutput();
			if(out.isEmpty())
				continue;
			mapper.addConversion(out.getCount(), NSSItem.create(out), im.getMap());
		}
	}
	
	@Override
	public String getName()
	{
		return "AE2InscriberMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for inscriber recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}