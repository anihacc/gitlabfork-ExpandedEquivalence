package com.zeitheron.expequiv.exp.thermalseries.te;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import cofh.thermalexpansion.util.managers.machine.TransposerManager;
import cofh.thermalexpansion.util.managers.machine.TransposerManager.TransposerRecipe;

class TransposerEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(TransposerRecipe recipe : TransposerManager.getFillRecipeList())
			emc.map(recipe.getOutput(), CountedIngredient.create(recipe.getInput()), CountedIngredient.create(recipe.getFluid()));
		for(TransposerRecipe recipe : TransposerManager.getExtractRecipeList())
			emc.map(recipe.getFluid(), CountedIngredient.create(recipe.getInput()), CountedIngredient.create(recipe.getOutput(), -1));
	}
}