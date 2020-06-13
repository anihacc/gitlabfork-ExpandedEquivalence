package com.zeitheron.expequiv.exp.ic2;

import java.util.stream.Collectors;

import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import ic2.core.recipe.AdvRecipe;
import ic2.core.recipe.AdvShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

class AdvRecipeEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(IRecipe recipe : CraftingManager.REGISTRY)
		{
			if(!(recipe instanceof AdvRecipe) && !(recipe instanceof AdvShapelessRecipe))
				continue;
			ItemStack recipeOutput = recipe.getRecipeOutput();
			if(recipeOutput.isEmpty())
				continue;
			emc.map(recipeOutput, recipe.getIngredients().stream().map(i -> FakeItem.create(emc, 1, i)).collect(Collectors.toList()));
		}
	}
}