package com.zeitheron.expequiv.exp.avaritia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

class CompressionEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(ICompressorRecipe recipe : AvaritiaRecipeManager.COMPRESSOR_RECIPES.values())
		{
			boolean handled = false;
			ItemStack recipeOutput = recipe.getResult();
			if(recipeOutput.isEmpty())
				continue;
			int cost = recipe.getCost() / 2;
			handled = true;
			
			List<CountedIngredient> im = new ArrayList<>();
			for(Ingredient i : recipe.getIngredients())
				im.add(FakeItem.create(emc, cost, i));
			emc.map(recipeOutput, recipeOutput.getCount(), im);
		}
	}
}