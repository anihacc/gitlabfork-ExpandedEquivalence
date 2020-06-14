package com.zeitheron.expequiv.exp.thermalseries.te;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager.PulverizerRecipe;

class PulverizerEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(PulverizerRecipe recipe : PulverizerManager.getRecipeList())
			emc.map(recipe.getPrimaryOutput(), CountedIngredient.create(recipe.getInput()));
	}
}