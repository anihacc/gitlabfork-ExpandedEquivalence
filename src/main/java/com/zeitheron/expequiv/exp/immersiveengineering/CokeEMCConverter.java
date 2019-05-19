package com.zeitheron.expequiv.exp.immersiveengineering;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import net.minecraft.item.ItemStack;

class CokeEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(CokeOvenRecipe recipe : CokeOvenRecipe.recipeList)
		{
			ItemStack recipeOutput = recipe.output;
			if(recipeOutput.isEmpty())
				continue;
			emc.map(recipeOutput, CountedIngredient.tryCreate(emc, recipe.input));
		}
	}
}