package com.zeitheron.expequiv.exp.draconicevolution;

import java.util.ArrayList;
import java.util.List;

import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe;
import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class FusionEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(IFusionRecipe recipe : FusionRecipeAPI.getRecipes())
		{
			ItemStack recipeOutput = recipe.getRecipeOutput(recipe.getRecipeCatalyst());
			if(recipeOutput.isEmpty())
				continue;
			List<CountedIngredient> im = new ArrayList<>();
			im.add(CountedIngredient.create(recipe.getRecipeCatalyst()));
			for(Object ingredient : recipe.getRecipeIngredients())
				if(ingredient instanceof Item)
					im.add(CountedIngredient.create((Item) ingredient, 1));
				else if(ingredient instanceof Block)
					im.add(CountedIngredient.create((Block) ingredient, 1));
				else if(ingredient instanceof ItemStack)
					im.add(CountedIngredient.create((ItemStack) ingredient));
				else if(ingredient instanceof String)
					im.add(CountedIngredient.create((String) ingredient, 1));
			emc.map(recipeOutput, im);
		}
	}
}