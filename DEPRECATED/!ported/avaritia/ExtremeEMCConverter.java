package com.zeitheron.expequiv.exp.avaritia;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

class ExtremeEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(IExtremeRecipe recipe : AvaritiaRecipeManager.EXTREME_RECIPES.values())
		{
			ItemStack recipeOutput = recipe.getRecipeOutput();
			if(recipeOutput.isEmpty())
				continue;
			List<CountedIngredient> im = new ArrayList<>();
			for(Ingredient i : recipe.getIngredients())
				im.add(FakeItem.create(emc, 1, i));
			emc.map(recipeOutput.copy().splitStack(1), recipeOutput.getCount(), im);
		}
	}
}