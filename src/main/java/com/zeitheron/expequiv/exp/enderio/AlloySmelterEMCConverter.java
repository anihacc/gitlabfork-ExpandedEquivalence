package com.zeitheron.expequiv.exp.enderio;

import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import crazypants.enderio.base.recipe.IManyToOneRecipe;
import crazypants.enderio.base.recipe.alloysmelter.AlloyRecipeManager;

class AlloySmelterEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(IManyToOneRecipe r : AlloyRecipeManager.getInstance().getRecipes())
			ExpansionEnderIO.handleMtO(emc, r);
	}
}