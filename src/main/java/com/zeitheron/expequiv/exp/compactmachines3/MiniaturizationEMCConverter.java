package com.zeitheron.expequiv.exp.compactmachines3;

import java.util.ArrayList;
import java.util.List;

import org.dave.compactmachines3.miniaturization.MultiblockRecipe;
import org.dave.compactmachines3.miniaturization.MultiblockRecipes;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import net.minecraft.item.ItemStack;

class MiniaturizationEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(MultiblockRecipe recipe : MultiblockRecipes.getRecipes())
		{
			ItemStack recipeOutput = recipe.getTargetStack();
			if(recipeOutput.isEmpty())
				continue;
			List<CountedIngredient> im = new ArrayList<>();
			im.add(CountedIngredient.create(recipe.getCatalystStack()));
			for(ItemStack is : recipe.getRequiredItemStacks())
				im.add(CountedIngredient.create(is));
			emc.map(recipeOutput, im);
		}
	}
}