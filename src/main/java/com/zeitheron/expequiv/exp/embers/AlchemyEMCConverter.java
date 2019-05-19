package com.zeitheron.expequiv.exp.embers;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import net.minecraft.item.ItemStack;
import teamroots.embers.recipe.AlchemyRecipe;
import teamroots.embers.recipe.RecipeRegistry;

class AlchemyEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(AlchemyRecipe r : RecipeRegistry.alchemyRecipes)
		{
			List<CountedIngredient> im = new ArrayList<>();
			im.add(CountedIngredient.create(r.centerInput));
			for(ItemStack in : r.inputs)
				im.add(CountedIngredient.create(in));
			emc.map(r.result, im);
		}
	}
}