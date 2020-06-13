package com.zeitheron.expequiv.exp.botania;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import vazkii.botania.common.item.ModItems;

class TerrestrialEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		List<CountedIngredient> im = new ArrayList<>();
		ItemStack output = new ItemStack(ModItems.manaResource, 1, 4);
		int mana = 600_000;
		FakeItem manaItem = new FakeItem();
		emc.register(manaItem, (long) MathHelper.ceil(mana / 10F));
		im.add(CountedIngredient.create(manaItem, 1));
		im.add(CountedIngredient.create(new ItemStack(ModItems.manaResource, 1, 0), 1));
		im.add(CountedIngredient.create(new ItemStack(ModItems.manaResource, 1, 1), 1));
		im.add(CountedIngredient.create(new ItemStack(ModItems.manaResource, 1, 2), 1));
		emc.map(output, im);
	}
}