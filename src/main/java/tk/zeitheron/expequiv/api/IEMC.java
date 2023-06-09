package tk.zeitheron.expequiv.api;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.emc.mappers.APICustomEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tk.zeitheron.expequiv.ExpandedEquivalence;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IEMC
{
	IEMC PE_WRAPPER = (out, ings) ->
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
		if(ingredients.containsKey(null) || ingredients.containsValue(null))
			ExpandedEquivalence.LOG.error("Found a NULL ingredient while adding " + out.toString());
		if(out.getIngredient() != null && out.getCount() > 0 && !ingredients.isEmpty())
			ProjectEAPI.getConversionProxy().addConversion(out.getCount(), out.getIngredient(), ingredients);
	};
	
	void map(CountedIngredient output, CountedIngredient... ingredients);
	
	//
	
	default FakeItem fake(long emc)
	{
		FakeItem fake = new FakeItem();
		register(fake, emc);
		return fake;
	}
	
	default FakeItem fake()
	{
		return new FakeItem();
	}
	
	default void map(ItemStack output, int outCount, Collection<CountedIngredient> ingredients)
	{
		map(output, outCount, ingredients.toArray(new CountedIngredient[0]));
	}
	
	default void map(CountedIngredient ingr, Collection<CountedIngredient> ingredients)
	{
		map(ingr, ingredients.toArray(new CountedIngredient[0]));
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
	
	default void multiMap(List<CountedIngredient> output, Collection<CountedIngredient> ingredients)
	{
		if(output.isEmpty()) return;
		if(output.size() == 1)
		{
			CountedIngredient out = output.get(0);
			map(out, ingredients);
			return;
		}
		
		int total = 0;
		for(CountedIngredient c : ingredients) total += c.getCount();
		
		FakeItem obj = new FakeItem();
		map(obj, total, ingredients);
		
		for(CountedIngredient o : output) map(o, obj.stack(o.getCount()));
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
		APICustomEMCMapper.instance.registerCustomEMC(item.getHolder(), emc);
	}
	
	default void register(ItemStack item, long emc)
	{
		ProjectEAPI.getEMCProxy().registerCustomEMC(item, emc);
	}
}