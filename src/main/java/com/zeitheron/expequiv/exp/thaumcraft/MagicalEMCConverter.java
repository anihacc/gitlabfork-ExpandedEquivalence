package com.zeitheron.expequiv.exp.thaumcraft;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.IThaumcraftRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;

class MagicalEMCConverter implements IEMCConverter
{
	ExpansionThaumcraft tc;
	
	public MagicalEMCConverter(ExpansionThaumcraft tc)
	{
		this.tc = tc;
	}
	
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		for(IThaumcraftRecipe recipe : ThaumcraftApi.getCraftingRecipes().values())
		{
			if(recipe instanceof IArcaneRecipe)
				mapRecipe(emc, (IArcaneRecipe) recipe);
			if(recipe instanceof CrucibleRecipe)
				mapRecipe(emc, (CrucibleRecipe) recipe);
			if(recipe instanceof InfusionRecipe)
				mapRecipe(emc, (InfusionRecipe) recipe);
		}
	}
	
	private void mapRecipe(IEMC emc, IArcaneRecipe recipe)
	{
		ItemStack output = recipe.getRecipeOutput();
		if(output.isEmpty())
			return;
		List<CountedIngredient> im = new ArrayList<>();
		int pearls = 0;
		ItemStack primPearl = new ItemStack(ItemsTC.primordialPearl);
		for(Ingredient i : recipe.getIngredients())
			if(i.apply(primPearl))
				++pearls;
			else
				im.add(FakeItem.create(emc, 1, i));
		FakeItem primordialPearl = new FakeItem();
		emc.register(primordialPearl, tc.getPrimordialPearlCost() / ItemsTC.primordialPearl.getMaxDamage(primPearl));
		im.add(CountedIngredient.create(primordialPearl, pearls));
		AspectList vis = recipe.getCrystals();
		for(Aspect a : vis.getAspects())
		{
			FakeItem aspect = new FakeItem();
			emc.register(aspect, tc.getAspectCost(a) + tc.getVisCrystalCost());
			im.add(CountedIngredient.create(aspect, vis.getAmount(a)));
		}
		emc.map(output, im);
	}
	
	private void mapRecipe(IEMC emc, CrucibleRecipe recipe)
	{
		ItemStack output = recipe.getRecipeOutput();
		if(output.isEmpty())
			return;
		List<CountedIngredient> im = new ArrayList<>();
		int pearls = 0;
		ItemStack primPearl = new ItemStack(ItemsTC.primordialPearl);
		Ingredient i = recipe.getCatalyst();
		if(i.apply(primPearl))
			++pearls;
		else
			im.add(FakeItem.create(emc, 1, i));
		FakeItem primordialPearl = new FakeItem();
		emc.register(primordialPearl, tc.getPrimordialPearlCost() / ItemsTC.primordialPearl.getMaxDamage(primPearl));
		im.add(CountedIngredient.create(primordialPearl, pearls));
		AspectList vis = recipe.getAspects();
		for(Aspect a : vis.getAspects())
		{
			FakeItem aspect = new FakeItem();
			emc.register(aspect, tc.getAspectCost(a));
			im.add(CountedIngredient.create(aspect, vis.getAmount(a)));
		}
		emc.map(output, im);
	}
	
	private void mapRecipe(IEMC emc, InfusionRecipe recipe)
	{
		ItemStack output = ItemStack.EMPTY;
		{
			Object o = recipe.getRecipeOutput();
			if(o instanceof ItemStack)
				output = (ItemStack) o;
			if(o instanceof Item)
				output = new ItemStack((Item) o);
			if(o instanceof Block)
				output = new ItemStack((Block) o);
		}
		if(output.isEmpty())
			return;
		List<CountedIngredient> im = new ArrayList<>();
		int pearls = 0;
		ItemStack primPearl = new ItemStack(ItemsTC.primordialPearl);
		List<Ingredient> items = new ArrayList<>();
		items.add(recipe.sourceInput);
		items.addAll(recipe.getComponents());
		for(Ingredient i : items)
			if(i.apply(primPearl))
				++pearls;
			else
				im.add(FakeItem.create(emc, 1, i));
		FakeItem primordialPearl = new FakeItem();
		emc.register(primordialPearl, tc.getPrimordialPearlCost() / ItemsTC.primordialPearl.getMaxDamage(primPearl));
		im.add(CountedIngredient.create(primordialPearl, pearls));
		AspectList vis = recipe.getAspects();
		for(Aspect a : vis.getAspects())
		{
			FakeItem aspect = new FakeItem();
			emc.register(aspect, tc.getAspectCost(a));
			im.add(CountedIngredient.create(aspect, vis.getAmount(a)));
		}
		
		FakeItem instability = new FakeItem();
		emc.register(instability, (long) (tc.instabilityMod * recipe.instability));
		im.add(CountedIngredient.create(instability, pearls));
		
		emc.map(output, im);
	}
}