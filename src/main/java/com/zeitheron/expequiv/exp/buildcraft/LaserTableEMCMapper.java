package com.zeitheron.expequiv.exp.buildcraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import buildcraft.api.recipes.AssemblyRecipe;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.lib.recipe.AssemblyRecipeRegistry;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class LaserTableEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		block2: for(AssemblyRecipe recipe : AssemblyRecipeRegistry.REGISTRY.values())
		{
			ItemStack recipeOutput = recipe.getOutputPreviews().stream().findFirst().orElse(ItemStack.EMPTY);
			Set<IngredientStack> ingredients = recipe.getInputsFor(recipeOutput.copy());
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			
			for(CraftingIngredients variation : getIngredientsFor(ingredients))
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
		return "BCLaserTableMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for laser table recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	private static class CraftingIngredients
	{
		public final Iterable<ItemStack> fixedIngredients;
		public final Iterable<Iterable<ItemStack>> multiIngredients;
		
		public CraftingIngredients(Iterable<ItemStack> fixedIngredients, Iterable<Iterable<ItemStack>> multiIngredients)
		{
			this.fixedIngredients = fixedIngredients;
			this.multiIngredients = multiIngredients;
		}
	}
	
	public Iterable<CraftingIngredients> getIngredientsFor(Set<IngredientStack> recipe)
	{
		ArrayList<Iterable<ItemStack>> variableInputs = new ArrayList<Iterable<ItemStack>>();
		ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
		for(IngredientStack recipeItem : recipe)
		{
			ItemStack[] matches = recipeItem.ingredient.getMatchingStacks();
			if(matches.length == 1)
			{
				for(int i = 0; i < recipeItem.count; ++i)
					fixedInputs.add(matches[0].copy());
				continue;
			}
			if(matches.length <= 0)
				continue;
			LinkedList<ItemStack> recipeItemOptions = new LinkedList<ItemStack>();
			for(int i = 0; i < recipeItem.count; ++i)
				for(ItemStack option : matches)
					recipeItemOptions.add(option.copy());
			variableInputs.add(recipeItemOptions);
		}
		return Collections.singletonList(new CraftingIngredients(fixedInputs, variableInputs));
	}
}