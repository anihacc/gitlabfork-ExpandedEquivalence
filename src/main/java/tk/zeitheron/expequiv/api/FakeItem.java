package tk.zeitheron.expequiv.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import net.minecraft.item.crafting.Ingredient;

public class FakeItem
{
	final Object holder = new Object();
	
	public FakeItem()
	{
	}
	
	public Object getHolder()
	{
		return holder;
	}
	
	public CountedIngredient stack(int amount)
	{
		return CountedIngredient.create(this, amount);
	}
	
	public static FakeItem merge(IEMC emc, CountedIngredient... ingredients)
	{
		return EMCIngredientUtils.merge(emc, ingredients);
	}
	
	public static FakeItem merge(IEMC emc, Collection<CountedIngredient> ingredients)
	{
		return EMCIngredientUtils.merge(emc, ingredients);
	}
	
	public static CountedIngredient merge(IEMC emc, int count, CountedIngredient... ingredients)
	{
		return CountedIngredient.create(merge(emc, ingredients), count);
	}
	
	public static CountedIngredient merge(IEMC emc, int count, Collection<CountedIngredient> ingredients)
	{
		return CountedIngredient.create(merge(emc, ingredients), count);
	}
	
	public static FakeItem create(IEMC emc, Ingredient ingredient)
	{
		return merge(emc, Arrays.stream(ingredient.getMatchingStacks()).map(CountedIngredient::create).collect(Collectors.toList()));
	}
	
	public static CountedIngredient create(IEMC emc, int count, Ingredient ingredient)
	{
		return CountedIngredient.create(create(emc, ingredient), count);
	}
	
	public static CountedIngredient create(IEMC emc, Ingredient ingredient, int count)
	{
		return CountedIngredient.create(create(emc, ingredient), count);
	}
}