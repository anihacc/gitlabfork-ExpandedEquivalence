package com.zeitheron.expequiv.exp.buildcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import buildcraft.api.recipes.AssemblyRecipe;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.lib.recipe.AssemblyRecipeRegistry;
import net.minecraft.item.ItemStack;

class LaserTableEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(AssemblyRecipe recipe : AssemblyRecipeRegistry.REGISTRY.values())
		{
			ItemStack recipeOutput = recipe.getOutputPreviews().stream().findFirst().orElse(ItemStack.EMPTY);
			Set<IngredientStack> ingredients = recipe.getInputsFor(recipeOutput.copy());
			if(recipeOutput.isEmpty())
				continue;
			List<CountedIngredient> im = new ArrayList<>();
			for(IngredientStack is : ingredients)
				im.add(FakeItem.create(emc, is.count, is.ingredient));
			emc.map(recipeOutput, im);
		}
	}
}