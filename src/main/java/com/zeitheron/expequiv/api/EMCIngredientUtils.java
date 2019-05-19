package com.zeitheron.expequiv.api;

import java.util.Collection;

public class EMCIngredientUtils
{
	public static FakeItem merge(IEMC emc, Collection<CountedIngredient> ingredients)
	{
		return merge(emc, ingredients.toArray(new CountedIngredient[ingredients.size()]));
	}
	
	public static FakeItem merge(IEMC emc, CountedIngredient... ingredients)
	{
		FakeItem fake = new FakeItem();
		CountedIngredient fakeX1 = CountedIngredient.create(fake, 1);
		for(CountedIngredient i : ingredients)
			emc.map(fakeX1, i);
		return fake;
	}
}