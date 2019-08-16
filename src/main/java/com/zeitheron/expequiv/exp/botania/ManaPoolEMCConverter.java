package com.zeitheron.expequiv.exp.botania;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

class ManaPoolEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes)
		{
			if(recipe.getInput() instanceof ItemStack)
			{
				ItemStack input = (ItemStack) recipe.getInput();
				ItemStack output = recipe.getOutput();
				if(input.isEmpty() || output.isEmpty())
					continue;
				FakeItem mana = new FakeItem();
				emc.register(mana, (long) MathHelper.ceil(recipe.getManaToConsume() / 10F));
				emc.map(output, CountedIngredient.create(input, input.getCount()), CountedIngredient.create(mana, 1));
			} else if(recipe.getInput() instanceof Ingredient)
			{
				Ingredient input = (Ingredient) recipe.getInput();
				ItemStack output = recipe.getOutput();
				if(input == null || output.isEmpty())
					continue;
				FakeItem mana = new FakeItem();
				emc.register(mana, (long) MathHelper.ceil(recipe.getManaToConsume() / 10F));
				emc.map(output, FakeItem.create(emc, 1, input), CountedIngredient.create(mana, 1));
			} else if(recipe.getInput() instanceof String)
			{
				String input = (String) recipe.getInput();
				ItemStack output = recipe.getOutput();
				if(input.isEmpty() || output.isEmpty())
					continue;
				FakeItem mana = new FakeItem();
				emc.register(mana, (long) MathHelper.ceil(recipe.getManaToConsume() / 10F));
				emc.map(output, CountedIngredient.create(input, 1), CountedIngredient.create(mana, 1));
			}
		}
	}
}