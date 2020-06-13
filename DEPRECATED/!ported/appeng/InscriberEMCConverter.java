package com.zeitheron.expequiv.exp.appeng;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;

import appeng.api.AEApi;
import appeng.api.features.IInscriberRecipe;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

class InscriberEMCConverter implements IEMCConverter
{
	@Override
	public void register(IEMC emc, com.zeitheron.hammercore.cfg.file1132.Configuration cfg)
	{
		Function<String, Item> find = reg -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", reg));
		Item material = find.apply("material");
		IntList presses = new IntArrayList();
		presses.add(13);
		presses.add(14);
		presses.add(15);
		presses.add(19);
		
		for(IInscriberRecipe recipe : AEApi.instance().registries().inscriber().getRecipes())
		{
			List<CountedIngredient> im = new ArrayList<>();
			List<ItemStack> inputs = new ArrayList<>();
			inputs.add(recipe.getInputs().get(0));
			inputs.add(recipe.getTopOptional().orElse(ItemStack.EMPTY));
			inputs.add(recipe.getBottomOptional().orElse(ItemStack.EMPTY));
			for(ItemStack in : inputs)
			{
				boolean press = !in.isEmpty() && in.getItem() == material && presses.contains(in.getItemDamage());
				if(!in.isEmpty() && !press)
					im.add(CountedIngredient.create(in, in.getCount()));
			}
			ItemStack out = recipe.getOutput();
			if(out.isEmpty())
				continue;
			emc.map(out, out.getCount(), im);
		}
	}
}