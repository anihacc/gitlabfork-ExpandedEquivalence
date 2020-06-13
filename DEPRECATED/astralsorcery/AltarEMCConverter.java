package com.zeitheron.expequiv.exp.astralsorcery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

class AltarEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		Set<AbstractAltarRecipe> recipes = new HashSet<>();
		AltarRecipeRegistry.recipes.values().forEach(recipes::addAll);
		AltarRecipeRegistry.mtRecipes.values().forEach(recipes::addAll);
		
		for(AbstractAltarRecipe recipe : recipes)
		{
			ItemStack recipeOutput = recipe.getOutputForMatching();
			if(recipeOutput.isEmpty())
				continue;
			List<CountedIngredient> im = new ArrayList<>();
			for(Ingredient i : recipe.getNativeRecipe().getIngredients())
				im.add(FakeItem.create(emc, 1, i));
			FakeItem xp = new FakeItem();
			emc.register(xp, (long) (100 * recipe.getCraftExperience() * recipe.getCraftExperienceMultiplier()));
			im.add(CountedIngredient.create(xp, 1));
			emc.map(recipeOutput, im);
		}
	}
}