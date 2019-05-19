package com.zeitheron.expequiv.exp.astralsorcery;

import java.util.HashSet;
import java.util.Set;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import hellfirepvp.astralsorcery.common.crafting.infusion.AbstractInfusionRecipe;
import hellfirepvp.astralsorcery.common.crafting.infusion.InfusionRecipeRegistry;
import net.minecraft.item.ItemStack;

class StarlightInfusionEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		Set<AbstractInfusionRecipe> recipes = new HashSet<>();
		recipes.addAll(InfusionRecipeRegistry.recipes);
		recipes.addAll(InfusionRecipeRegistry.mtRecipes);
		
		for(AbstractInfusionRecipe recipe : recipes)
		{
			ItemStack recipeOutput = recipe.getOutputForMatching();
			if(recipeOutput.isEmpty())
				continue;
			FakeItem starlight = new FakeItem();
			emc.register(starlight, (long) (1000 * recipe.getLiquidStarlightConsumptionChance()));
			emc.map(recipeOutput, FakeItem.create(emc, 1, recipe.getInput().getRecipeIngredient()), CountedIngredient.create(starlight, 1));
		}
	}
}