package com.zeitheron.expequiv.exp.thermalseries.te;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import cofh.thermalexpansion.util.managers.machine.ExtruderManager;
import cofh.thermalexpansion.util.managers.machine.ExtruderManager.ExtruderRecipe;

class ExtruderEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(ExtruderRecipe recipe : ExtruderManager.getRecipeList(true))
			emc.map(recipe.getOutput(), CountedIngredient.create(recipe.getInputCold()), CountedIngredient.create(recipe.getInputHot()));
		for(ExtruderRecipe recipe : ExtruderManager.getRecipeList(false))
			emc.map(recipe.getOutput(), CountedIngredient.create(recipe.getInputCold()), CountedIngredient.create(recipe.getInputHot()));
	}
}