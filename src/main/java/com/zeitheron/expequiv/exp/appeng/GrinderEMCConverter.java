package com.zeitheron.expequiv.exp.appeng;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import appeng.api.AEApi;
import appeng.api.features.IGrinderRecipe;
import net.minecraft.item.ItemStack;

class GrinderEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(IGrinderRecipe recipe : AEApi.instance().registries().grinder().getRecipes())
		{
			ItemStack input = recipe.getInput();
			ItemStack output = recipe.getOutput();
			emc.map(output, output.getCount(), CountedIngredient.create(input, input.getCount()));
		}
	}
}