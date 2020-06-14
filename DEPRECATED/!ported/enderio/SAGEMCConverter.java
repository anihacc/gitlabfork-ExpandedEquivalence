package com.zeitheron.expequiv.exp.enderio;

import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import crazypants.enderio.base.recipe.IRecipe;
import crazypants.enderio.base.recipe.RecipeOutput;
import crazypants.enderio.base.recipe.sagmill.SagMillRecipeManager;

class SAGEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(IRecipe r : SagMillRecipeManager.getInstance().getRecipes())
		{
			RecipeOutput o = r.getOutputs().length == 1 ? r.getOutputs()[0] : null;
			if(o == null || o.getChance() < 1F)
				continue;
			emc.map(o.getOutput(), ExpansionEnderIO.inputsToIngs(emc, r.getInputs()));
		}
	}
}