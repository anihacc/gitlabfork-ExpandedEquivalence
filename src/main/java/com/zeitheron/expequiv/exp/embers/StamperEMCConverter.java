package com.zeitheron.expequiv.exp.embers;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

class StamperEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(ItemStampingRecipe r : RecipeRegistry.stampingRecipes)
		{
			List<CountedIngredient> im = new ArrayList<>();
			if(!r.getStack().isEmpty())
				im.add(CountedIngredient.create(r.getStack()));
			if(r.getFluid() != null)
				im.add(CountedIngredient.create(r.getFluid()));
			emc.map(r.result, im);
		}
	}
}