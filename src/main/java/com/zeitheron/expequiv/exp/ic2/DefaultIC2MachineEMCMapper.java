package com.zeitheron.expequiv.exp.ic2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
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

class DefaultIC2MachineEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	final IBasicMachineRecipeManager registry;
	
	public DefaultIC2MachineEMCMapper(IBasicMachineRecipeManager registry)
	{
		this.registry = registry;
	}
	
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		block2: for(MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe : registry.getRecipes())
			if(recipe.getOutput().size() == 1)
			{
				boolean handled = false;
				ItemStack recipeOutput = recipe.getOutput().stream().findFirst().orElse(ItemStack.EMPTY);
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
		return "IC2MachineMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for machine recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	public Iterable<CraftingIngredients> getIngredientsFor(MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe)
	{
		ArrayList<Iterable<ItemStack>> variableInputs = new ArrayList<Iterable<ItemStack>>();
		ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
		Ingredient recipeItem = recipe.getInput().getIngredient();
		
		l:
		{
			if(recipeItem instanceof Ingredient)
			{
				ItemStack[] matches = recipeItem.getMatchingStacks();
				if(matches.length == 1)
				{
					fixedInputs.add(matches[0].copy());
					break l;
				}
				if(matches.length <= 0)
					break l;
				LinkedList<ItemStack> recipeItemOptions = new LinkedList<ItemStack>();
				for(ItemStack option : matches)
					recipeItemOptions.add(option.copy());
				variableInputs.add(recipeItemOptions);
			}
		}
		
		return Collections.singletonList(new CraftingIngredients(fixedInputs, variableInputs));
	}
}