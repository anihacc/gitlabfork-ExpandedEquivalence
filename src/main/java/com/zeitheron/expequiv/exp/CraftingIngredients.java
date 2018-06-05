package com.zeitheron.expequiv.exp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

public class CraftingIngredients
{
	public final Iterable<ItemStack> fixedIngredients;
	public final Iterable<Iterable<ItemStack>> multiIngredients;
	
	public CraftingIngredients(Iterable<ItemStack> fixedIngredients, Iterable<Iterable<ItemStack>> multiIngredients)
	{
		this.fixedIngredients = fixedIngredients;
		this.multiIngredients = multiIngredients;
	}
	
	public IngredientMap<NormalizedSimpleStack> toIngredients(IMappingCollector<NormalizedSimpleStack, Integer> mapper)
	{
		IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
		
		for(ItemStack stack : fixedIngredients)
		{
			if(stack.isEmpty())
				continue;
			try
			{
				if(stack.getItemDamage() != OreDictionary.WILDCARD_VALUE && stack.getItem().hasContainerItem(stack))
					im.addIngredient(NSSItem.create(stack.getItem().getContainerItem(stack)), -1);
				im.addIngredient(NSSItem.create(stack), stack.getCount());
			} catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		
		for(Iterable<ItemStack> multiIngredient : multiIngredients)
		{
			NormalizedSimpleStack dummy = NSSFake.create(multiIngredient.toString());
			im.addIngredient(dummy, 1);
			for(ItemStack stack : multiIngredient)
			{
				if(stack.isEmpty())
					continue;
				IngredientMap groupIngredientMap = new IngredientMap();
				if(stack.getItem().hasContainerItem(stack))
					groupIngredientMap.addIngredient(NSSItem.create(stack.getItem().getContainerItem(stack)), -1);
				groupIngredientMap.addIngredient(NSSItem.create(stack), stack.getCount());
				mapper.addConversion(1, dummy, groupIngredientMap.getMap());
			}
		}
		
		return im;
	}
	
	public static NormalizedSimpleStack createFromMultipleItems(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Iterable<ItemStack> multi)
	{
		NormalizedSimpleStack dummy = NSSFake.create(multi.toString());
		for(ItemStack stack : multi)
		{
			if(stack.isEmpty())
				continue;
			IngredientMap groupIngredientMap = new IngredientMap();
			if(stack.getItem().hasContainerItem(stack))
				groupIngredientMap.addIngredient(NSSItem.create(stack.getItem().getContainerItem(stack)), -1);
			groupIngredientMap.addIngredient(NSSItem.create(stack), 1);
			mapper.addConversion(1, dummy, groupIngredientMap.getMap());
		}
		return dummy;
	}
	
	public static CraftingIngredients getIngredientsFor(Iterable<? extends Object> ingredients, ItemStack... fixedIngredients)
	{
		ArrayList<Iterable<ItemStack>> variableInputs = new ArrayList<Iterable<ItemStack>>();
		ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
		if(fixedIngredients != null)
			for(ItemStack f : fixedIngredients)
				fixedInputs.add(f.copy());
		if(ingredients != null)
			for(Object recipeItem : ingredients)
			{
				if(recipeItem instanceof Ingredient || recipeItem instanceof String || recipeItem instanceof ItemStack[] || recipeItem instanceof List)
				{
					ItemStack[] matches = recipeItem instanceof ItemStack[] ? (ItemStack[]) recipeItem : null;
					
					if(recipeItem instanceof Ingredient)
						matches = ((Ingredient) recipeItem).getMatchingStacks();
					if(recipeItem instanceof String)
						matches = OreDictionary.getOres((String) recipeItem).toArray(new ItemStack[0]);
					if(recipeItem instanceof List)
						matches = ((List<ItemStack>) recipeItem).toArray(new ItemStack[0]);
					
					if(matches == null)
						continue;
					if(matches.length == 1)
					{
						fixedInputs.add(matches[0].copy());
						continue;
					}
					if(matches.length <= 0)
						continue;
					LinkedList<ItemStack> recipeItemOptions = new LinkedList<ItemStack>();
					for(ItemStack option : matches)
						recipeItemOptions.add(option.copy());
					variableInputs.add(recipeItemOptions);
				} else if(recipeItem instanceof ItemStack)
					fixedInputs.add((ItemStack) recipeItem);
				else if(recipeItem instanceof Item)
					fixedInputs.add(new ItemStack((Item) recipeItem));
				else if(recipeItem instanceof Block)
					fixedInputs.add(new ItemStack((Block) recipeItem));
			}
		return new CraftingIngredients(fixedInputs, variableInputs);
	}
}