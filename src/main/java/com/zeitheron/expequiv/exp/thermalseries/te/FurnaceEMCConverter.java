package com.zeitheron.expequiv.exp.thermalseries.te;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import cofh.thermalexpansion.util.managers.machine.FurnaceManager;

import cofh.thermalexpansion.util.managers.machine.FurnaceManager.FurnaceRecipe;
import cofh.thermalfoundation.init.TFFluids;
import net.minecraftforge.fluids.FluidStack;

class FurnaceEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(FurnaceRecipe recipe : FurnaceManager.getRecipeList(false))
			emc.map(recipe.getOutput(), CountedIngredient.create(recipe.getInput()));
		for(FurnaceRecipe recipe : FurnaceManager.getRecipeList(true))
		{
			System.out.println("Pyrolysis for " + recipe.getOutput() + ": " + recipe.getCreosote());
			emc.map(recipe.getOutput(), CountedIngredient.create(recipe.getInput()), CountedIngredient.create(new FluidStack(TFFluids.fluidPyrotheum, recipe.getCreosote())));
		}
	}
}