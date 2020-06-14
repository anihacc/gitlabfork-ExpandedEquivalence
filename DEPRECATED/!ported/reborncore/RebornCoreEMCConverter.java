package com.zeitheron.expequiv.exp.reborncore;

import java.util.stream.Collectors;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import net.minecraft.item.ItemStack;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;

class RebornCoreEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(IBaseRecipeType recipe : RecipeHandler.recipeList)
			if(recipe.getOutputsSize() == 1)
			{
				ItemStack out = recipe.getOutput(0);
				emc.map(out, recipe.getInputs().stream().map(i -> CountedIngredient.tryCreate(emc, i)).collect(Collectors.toList()));
			}
	}
}