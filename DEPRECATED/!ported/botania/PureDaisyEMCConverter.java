package com.zeitheron.expequiv.exp.botania;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;

class PureDaisyEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes)
		{
			Object oi = recipe.getInput();
			IBlockState state = recipe.getOutputState();
			ItemStack output = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
			CountedIngredient in = CountedIngredient.tryCreate(emc, oi);
			if(in != null && !output.isEmpty())
				emc.map(output, in);
		}
	}
}