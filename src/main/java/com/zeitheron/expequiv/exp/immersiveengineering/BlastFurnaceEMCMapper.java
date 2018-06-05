package com.zeitheron.expequiv.exp.immersiveengineering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class BlastFurnaceEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration cfg)
	{
		block2: for(BlastFurnaceRecipe recipe : BlastFurnaceRecipe.recipeList)
		{
			ItemStack recipeOutput = recipe.output;
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			
			for(CraftingIngredients variation : getIngredientsFor(recipe))
			{
				IngredientMap ingredientMap = new IngredientMap();
				
				for(ItemStack stack : variation.fixedIngredients)
				{
					if(stack.isEmpty())
						continue;
					try
					{
						if(stack.getItemDamage() != OreDictionary.WILDCARD_VALUE && stack.getItem().hasContainerItem(stack))
							ingredientMap.addIngredient(NSSItem.create(stack.getItem().getContainerItem(stack)), -1);
						ingredientMap.addIngredient(NSSItem.create(stack), 1);
					} catch(Exception e)
					{
						e.printStackTrace();
						continue block2;
					}
				}
				
				for(Iterable<ItemStack> multiIngredient : variation.multiIngredients)
				{
					NormalizedSimpleStack dummy = NSSFake.create(multiIngredient.toString());
					ingredientMap.addIngredient(dummy, 1);
					for(ItemStack stack : multiIngredient)
					{
						if(stack.isEmpty())
							continue;
						IngredientMap groupIngredientMap = new IngredientMap();
						if(stack.getItem().hasContainerItem(stack))
							groupIngredientMap.addIngredient(NSSItem.create(stack.getItem().getContainerItem(stack)), -1);
						groupIngredientMap.addIngredient(NSSItem.create(stack), 1);
						mapper.addConversion(1, dummy, groupIngredientMap.getMap());
					}
				}
				
				mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
			}
			
		}
	}
	
	@Override
	public String getName()
	{
		return "IEBlastFurnaceMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for blast furnace recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	public Iterable<CraftingIngredients> getIngredientsFor(BlastFurnaceRecipe recipe)
	{
		ArrayList<Iterable<ItemStack>> variableInputs = new ArrayList<Iterable<ItemStack>>();
		ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
		Object recipeItem = recipe.input;
		
		List<ItemStack> match = new ArrayList<>();
		if(recipeItem instanceof ItemStack)
			match.add((ItemStack) recipeItem);
		if(recipeItem instanceof String)
			match.addAll(OreDictionary.getOres(recipeItem + ""));
		if(recipeItem instanceof Ingredient)
			match.addAll(Arrays.asList(((Ingredient) recipeItem).getMatchingStacks()));
		
		c:
		{
			ItemStack[] matches = match.toArray(new ItemStack[0]);
			if(matches.length == 1)
			{
				fixedInputs.add(matches[0].copy());
				break c;
			}
			if(matches.length <= 0)
				break c;
			LinkedList<ItemStack> recipeItemOptions = new LinkedList<ItemStack>();
			for(ItemStack option : matches)
				recipeItemOptions.add(option.copy());
			variableInputs.add(recipeItemOptions);
		}
		
		return Collections.singletonList(new CraftingIngredients(fixedInputs, variableInputs));
	}
}