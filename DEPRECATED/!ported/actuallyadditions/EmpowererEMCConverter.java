package com.zeitheron.expequiv.exp.actuallyadditions;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.EmpowererRecipe;
import net.minecraft.item.ItemStack;

class EmpowererEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(EmpowererRecipe recipe : ActuallyAdditionsAPI.EMPOWERER_RECIPES)
		{
			ItemStack recipeOutput = recipe.getOutput();
			if(recipeOutput.isEmpty())
				continue;
			List<CountedIngredient> im = new ArrayList<>();
			
			im.add(FakeItem.create(emc, recipe.getInput(), 1));
			if(recipe.getStandOne() != null)
				im.add(FakeItem.create(emc, recipe.getStandOne(), 1));
			if(recipe.getStandTwo() != null)
				im.add(FakeItem.create(emc, recipe.getStandTwo(), 1));
			if(recipe.getStandThree() != null)
				im.add(FakeItem.create(emc, recipe.getStandThree(), 1));
			if(recipe.getStandFour() != null)
				im.add(FakeItem.create(emc, recipe.getStandFour(), 1));
			
			emc.map(recipe.getOutput(), recipe.getOutput().getCount(), im);
		}
	}
}