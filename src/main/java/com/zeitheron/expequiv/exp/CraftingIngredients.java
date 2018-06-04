package com.zeitheron.expequiv.exp;

import net.minecraft.item.ItemStack;

public class CraftingIngredients
{
	public final Iterable<ItemStack> fixedIngredients;
	public final Iterable<Iterable<ItemStack>> multiIngredients;
	
	public CraftingIngredients(Iterable<ItemStack> fixedIngredients, Iterable<Iterable<ItemStack>> multiIngredients)
	{
		this.fixedIngredients = fixedIngredients;
		this.multiIngredients = multiIngredients;
	}
}