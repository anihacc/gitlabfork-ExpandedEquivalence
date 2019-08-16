package com.zeitheron.expequiv.exp.avaritia;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

class CompressionEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(ICompressorRecipe recipe : AvaritiaRecipeManager.COMPRESSOR_RECIPES.values())
		{
			ItemStack recipeOutput = recipe.getResult();
			if(recipeOutput.isEmpty())
				continue;
			int cost = recipe.getCost() / 2;
			List<CountedIngredient> im = new ArrayList<>();
			for(Ingredient i : recipe.getIngredients())
				im.add(FakeItem.create(emc, cost, i));
			emc.map(recipeOutput, recipeOutput.getCount(), im);
		}
	}
}