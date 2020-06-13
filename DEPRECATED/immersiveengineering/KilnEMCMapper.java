package com.zeitheron.expequiv.exp.immersiveengineering;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

class KilnEMCMapper implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(AlloyRecipe recipe : AlloyRecipe.recipeList)
		{
			ItemStack recipeOutput = recipe.output;
			if(recipeOutput.isEmpty())
				continue;
			
			List<IngredientStack> inputs = new ArrayList<>();
			if(recipe.input0 != null)
				inputs.add(recipe.input0);
			if(recipe.input1 != null)
				inputs.add(recipe.input1);
			
			List<CountedIngredient> ci = new ArrayList<>();
			for(List<ItemStack> ingredient : inputs.stream().map(is -> is.getStackList()).collect(Collectors.toList()))
				ci.add(FakeItem.create(emc, 1, Ingredient.fromStacks(ingredient.toArray(new ItemStack[ingredient.size()]))));
			emc.map(recipeOutput, ci);
		}
	}
}