package com.zeitheron.expequiv.exp.thermalseries.te;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import cofh.thermalexpansion.util.managers.machine.BrewerManager;
import cofh.thermalexpansion.util.managers.machine.BrewerManager.BrewerRecipe;

class BrewerEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(BrewerRecipe recipe : BrewerManager.getRecipeList())
			emc.map(recipe.getOutputFluid(), CountedIngredient.create(recipe.getInput()), CountedIngredient.create(recipe.getInputFluid()));
	}
}