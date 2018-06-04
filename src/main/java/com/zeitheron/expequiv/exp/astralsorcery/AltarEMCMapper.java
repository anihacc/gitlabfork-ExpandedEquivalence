package com.zeitheron.expequiv.exp.astralsorcery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
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

class AltarEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		Set<AbstractAltarRecipe> recipes = new HashSet<>();
		AltarRecipeRegistry.recipes.values().forEach(recipes::addAll);
		AltarRecipeRegistry.mtRecipes.values().forEach(recipes::addAll);
		
		block2: for(AbstractAltarRecipe recipe : recipes)
		{
			ItemStack recipeOutput = recipe.getOutputForMatching();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			
			for(CraftingIngredients variation : getIngredientsFor(recipe))
			{
				IngredientMap ingredientMap = new IngredientMap();
				
				NormalizedSimpleStack additionalEMC = NSSFake.create(recipe.toString());
				mapper.setValueBefore(additionalEMC, (int) (100 * recipe.getCraftExperience() * recipe.getCraftExperienceMultiplier()));
				ingredientMap.addIngredient(additionalEMC, 1);
				
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
		return "ASAltarMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for Astral Sorcery's altar recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	public Iterable<CraftingIngredients> getIngredientsFor(AbstractAltarRecipe recipe)
	{
		ArrayList<Iterable<ItemStack>> variableInputs = new ArrayList<Iterable<ItemStack>>();
		ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
		
		for(Ingredient recipeItem : recipe.getNativeRecipe().getIngredients())
		{
			ItemStack[] matches = recipeItem.getMatchingStacks();
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
		}
		return Collections.singletonList(new CraftingIngredients(fixedInputs, variableInputs));
	}
}