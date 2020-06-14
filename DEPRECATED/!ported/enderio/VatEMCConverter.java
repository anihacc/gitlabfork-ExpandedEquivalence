package com.zeitheron.expequiv.exp.enderio;

import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import crazypants.enderio.base.recipe.IRecipe;
import crazypants.enderio.base.recipe.RecipeOutput;
import crazypants.enderio.base.recipe.vat.VatRecipeManager;
import net.minecraftforge.fluids.FluidStack;

class VatEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(IRecipe r : VatRecipeManager.getInstance().getRecipes())
		{
			RecipeOutput o = r.getOutputs().length == 1 ? r.getOutputs()[0] : null;
			if(o == null || o.getChance() < 1F)
				continue;
			List<CountedIngredient> ci = ExpansionEnderIO.inputsToIngs(emc, r.getInputs());
			for(FluidStack f : r.getInputFluidStacks())
				ci.add(CountedIngredient.create(f));
			emc.map(o.getOutput(), ci);
		}
	}
}