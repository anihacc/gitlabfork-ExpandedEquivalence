package com.zeitheron.expequiv.exp.forestry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import forestry.api.recipes.IFabricatorRecipe;
import forestry.factory.recipes.FabricatorRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

class ThermionicEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		Set<IFabricatorRecipe> recipes = null;
		
		// Get a set of all recipes in a fabricator
		Field frecipes;
		try
		{
			frecipes = FabricatorRecipeManager.class.getDeclaredField("recipes");
			frecipes.setAccessible(true);
			recipes = (Set<IFabricatorRecipe>) frecipes.get(null);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1)
		{
			e1.printStackTrace();
		}
		
		if(recipes != null)
			for(IFabricatorRecipe recipe : recipes)
			{
				ItemStack recipeOutput = recipe.getRecipeOutput();
				if(recipeOutput.isEmpty())
					continue;
				List<CountedIngredient> ci = new ArrayList<>();
				if(!recipe.getPlan().isEmpty())
					ci.add(CountedIngredient.create(recipe.getPlan()));
				if(recipe.getLiquid() != null)
					ci.add(CountedIngredient.create(recipe.getLiquid()));
				for(NonNullList<ItemStack> inputs : recipe.getIngredients())
					ci.add(FakeItem.create(emc, 1, Ingredient.fromStacks(inputs.toArray(new ItemStack[inputs.size()]))));
				emc.map(recipeOutput, ci);
			}
	}
}