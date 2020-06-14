package com.zeitheron.expequiv.exp.thermalseries.te;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import cofh.thermalexpansion.util.managers.machine.CentrifugeManager;
import cofh.thermalexpansion.util.managers.machine.CentrifugeManager.CentrifugeRecipe;

class CentrifugeEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(CentrifugeRecipe recipe : CentrifugeManager.getRecipeList())
			if(recipe.getOutput().size() == 1)
			{
				FakeItem everything = new FakeItem();
				emc.map(everything, 1, CountedIngredient.create(recipe.getFluid()), CountedIngredient.create(recipe.getInput()));
				int chance = recipe.getChance().get(0);
				int mult = Math.round(100F / chance);
				emc.map(recipe.getOutput().get(0), CountedIngredient.create(everything, mult));
			}
	}
}