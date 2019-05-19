package com.zeitheron.expequiv.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.impl.EMCProxyImpl;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IEMC
{
	public static IEMC PE_WRAPPER = (out, ings) ->
	{
		if(out == null)
			return;
		Map<Object, Integer> ingredients = new HashMap<>();
		for(CountedIngredient ci : ings)
			if(ci != null && ci.getIngredient() != null && ci.getCount() > 0)
				if(ingredients.containsKey(ci.getIngredient()))
					ingredients.put(ci.getIngredient(), ingredients.get(ci.getIngredient()) + ci.getCount());
				else
					ingredients.put(ci.getIngredient(), ci.getCount());
		if(out.getIngredient() != null && out.getCount() > 0 && !ingredients.isEmpty())
			ProjectEAPI.getConversionProxy().addConversion(out.getCount(), out.getIngredient(), ingredients);
	};
	
	void map(CountedIngredient output, CountedIngredient... ingredients);
	
	//
	
	default void map(ItemStack output, int outCount, Collection<CountedIngredient> ingredients)
	{
		map(output, outCount, ingredients.toArray(new CountedIngredient[ingredients.size()]));
	}
	
	default void map(ItemStack output, int outCount, CountedIngredient... ingredients)
	{
		map(CountedIngredient.create(output, outCount), ingredients);
	}
	
	default void map(ItemStack output, Collection<CountedIngredient> ingredients)
	{
		map(output, output.getCount(), ingredients);
	}
	
	default void map(ItemStack output, CountedIngredient... ingredients)
	{
		map(output, output.getCount(), ingredients);
	}
	
	//
	
	default void map(FluidStack output, int outCount, Collection<CountedIngredient> ingredients)
	{
		map(output, outCount, ingredients.toArray(new CountedIngredient[ingredients.size()]));
	}
	
	default void map(FluidStack output, int outCount, CountedIngredient... ingredients)
	{
		map(CountedIngredient.create(output, outCount), ingredients);
	}
	
	default void map(FluidStack output, Collection<CountedIngredient> ingredients)
	{
		map(output, output.amount, ingredients);
	}
	
	default void map(FluidStack output, CountedIngredient... ingredients)
	{
		map(output, output.amount, ingredients);
	}
	
	//
	
	default void map(FakeItem output, int outCount, Collection<CountedIngredient> ingredients)
	{
		map(output, outCount, ingredients.toArray(new CountedIngredient[ingredients.size()]));
	}
	
	default void map(FakeItem output, int outCount, CountedIngredient... ingredients)
	{
		map(CountedIngredient.create(output, outCount), ingredients);
	}
	
	//
	
	default void register(FakeItem item, long emc)
	{
		ProjectEAPI.getEMCProxy().registerCustomEMC(item.getHolder(), emc);
	}
	
	default void register(ItemStack item, long emc)
	{
		ProjectEAPI.getEMCProxy().registerCustomEMC(item, emc);
	}
}