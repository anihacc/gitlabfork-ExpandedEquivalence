package com.zeitheron.expequiv.exp.immersiveengineering;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

class EnginnerWorkbenchEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(BlueprintCraftingRecipe recipe : BlueprintCraftingRecipe.recipeList.values())
		{
			ItemStack recipeOutput = recipe.output;
			if(recipeOutput.isEmpty())
				continue;
			List<CountedIngredient> ci = new ArrayList<>();
			for(List<ItemStack> ingredient : recipe.getItemInputs().stream().map(is -> is.getStackList()).collect(Collectors.toList()))
				ci.add(FakeItem.create(emc, 1, Ingredient.fromStacks(ingredient.toArray(new ItemStack[ingredient.size()]))));
			emc.map(recipeOutput, ci);
		}
	}
}