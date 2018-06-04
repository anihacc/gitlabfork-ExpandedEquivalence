package com.zeitheron.expequiv.exp.forestry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import forestry.api.recipes.ICarpenterRecipe;
import forestry.factory.recipes.CarpenterRecipeManager;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class CarpenterEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		Set<ICarpenterRecipe> recipes = null;
		
		// Get a set of all recipes in a carpenter
		Field frecipes;
		try
		{
			frecipes = CarpenterRecipeManager.class.getDeclaredField("recipes");
			frecipes.setAccessible(true);
			recipes = (Set<ICarpenterRecipe>) frecipes.get(null);
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1)
		{
			e1.printStackTrace();
		}
		
		if(recipes != null)
			block2: for(ICarpenterRecipe recipe : recipes)
			{
				boolean handled = false;
				ItemStack recipeOutput = recipe.getBox();
				if(recipeOutput.isEmpty())
					continue;
				NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
				
				handled = true;
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
							ingredientMap.addIngredient(NSSItem.create(stack), stack.getCount());
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
							groupIngredientMap.addIngredient(NSSItem.create(stack), stack.getCount());
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
		return "FCarpenterMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for carpenter recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	public Iterable<CraftingIngredients> getIngredientsFor(ICarpenterRecipe recipe)
	{
		ArrayList<Iterable<ItemStack>> variableInputs = new ArrayList<Iterable<ItemStack>>();
		ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
		
		l: for(NonNullList<ItemStack> recipeItem : recipe.getCraftingGridRecipe().getRawIngredients())
		{
			if(recipeItem.size() == 1)
			{
				fixedInputs.add(recipeItem.get(0).copy());
				continue l;
			}
			if(recipeItem.size() <= 0)
				continue l;
			LinkedList<ItemStack> recipeItemOptions = new LinkedList<ItemStack>();
			for(ItemStack option : recipeItem)
				recipeItemOptions.add(option.copy());
			variableInputs.add(recipeItemOptions);
		}
		
		return Collections.singletonList(new CraftingIngredients(fixedInputs, variableInputs));
	}
}