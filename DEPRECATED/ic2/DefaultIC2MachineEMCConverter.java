package com.zeitheron.expequiv.exp.ic2;

import java.util.Collection;

import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import net.minecraft.item.ItemStack;

class DefaultIC2MachineEMCConverter implements IEMCConverter
{
	final IBasicMachineRecipeManager registry;
	
	public DefaultIC2MachineEMCConverter(IBasicMachineRecipeManager registry)
	{
		this.registry = registry;
	}
	
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe : registry.getRecipes())
			if(recipe.getOutput().size() == 1)
			{
				ItemStack recipeOutput = recipe.getOutput().stream().findFirst().orElse(ItemStack.EMPTY);
				if(recipeOutput.isEmpty())
					continue;
				emc.map(recipeOutput, FakeItem.create(emc, recipe.getInput().getAmount(), recipe.getInput().getIngredient()));
			}
	}
}