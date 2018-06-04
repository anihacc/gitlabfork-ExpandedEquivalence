package com.zeitheron.expequiv.exp.compactmachines3;

import java.util.ArrayList;
import java.util.Collections;

import org.dave.compactmachines3.miniaturization.MultiblockRecipe;
import org.dave.compactmachines3.miniaturization.MultiblockRecipes;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class MiniaturizationEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		block2: for(MultiblockRecipe recipe : MultiblockRecipes.getRecipes())
		{
			ItemStack recipeOutput = recipe.getTargetStack();
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
		return "ASStarlightMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for starlight infusion recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	public Iterable<CraftingIngredients> getIngredientsFor(MultiblockRecipe recipe)
	{
		ArrayList<Iterable<ItemStack>> variableInputs = new ArrayList<Iterable<ItemStack>>();
		ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
		fixedInputs.add(recipe.getCatalystStack().copy());
		for(ItemStack is : recipe.getRequiredItemStacks())
			fixedInputs.add(is.copy());
		return Collections.singletonList(new CraftingIngredients(fixedInputs, variableInputs));
	}
}