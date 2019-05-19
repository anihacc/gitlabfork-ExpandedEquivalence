package com.zeitheron.expequiv.exp.actuallyadditions;

import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.LensConversionRecipe;
import net.minecraft.item.ItemStack;

class LaserEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, com.zeitheron.hammercore.cfg.file1132.Configuration cfg)
	{
		for(LensConversionRecipe recipe : ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES)
		{
			ItemStack recipeOutput = recipe.getOutput();
			if(recipeOutput.isEmpty())
				continue;
			emc.map(recipeOutput, recipeOutput.getCount(), FakeItem.create(emc, recipe.getInput(), 1));
		}
	}
}