package com.zeitheron.expequiv.exp.botania;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;

class ElvenTradeEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes)
		{
			List<CountedIngredient> im = new ArrayList<>();
			for(Object o : recipe.getInputs())
				if(o instanceof ItemStack)
					im.add(CountedIngredient.create((ItemStack) o, ((ItemStack) o).getCount()));
				else if(o instanceof Ingredient)
					im.add(FakeItem.create(emc, 1, (Ingredient) o));
				else if(o instanceof String)
					im.add(CountedIngredient.create(o.toString(), 1));
			ItemStack output = recipe.getOutputs().stream().filter(s -> !s.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
			if(!output.isEmpty())
				emc.map(output, im);
		}
	}
}