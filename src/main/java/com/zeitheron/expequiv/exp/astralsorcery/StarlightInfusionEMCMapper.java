package com.zeitheron.expequiv.exp.astralsorcery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import hellfirepvp.astralsorcery.common.crafting.infusion.AbstractInfusionRecipe;
import hellfirepvp.astralsorcery.common.crafting.infusion.InfusionRecipeRegistry;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.config.Configuration;

class StarlightInfusionEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		Set<AbstractInfusionRecipe> recipes = new HashSet<>();
		recipes.addAll(InfusionRecipeRegistry.recipes);
		recipes.addAll(InfusionRecipeRegistry.mtRecipes);
		
		for(AbstractInfusionRecipe recipe : recipes)
		{
			ItemStack recipeOutput = recipe.getOutputForMatching();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			
			CraftingIngredients variation = CraftingIngredients.getIngredientsFor(recipe.getInput().getApplicableItems());
			
			IngredientMap ingredientMap = variation.toIngredients(mapper);
			
			if(ingredientMap == null)
				continue;
			
			NormalizedSimpleStack additionalEMC = NSSFake.create(recipe.toString());
			mapper.setValueBefore(additionalEMC, (int) (1000 * recipe.getLiquidStarlightConsumptionChance()));
			ingredientMap.addIngredient(additionalEMC, 1);
			
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
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
	
	public Iterable<CraftingIngredients> getIngredientsFor(AbstractInfusionRecipe recipe)
	{
		ArrayList<Iterable<ItemStack>> variableInputs = new ArrayList<Iterable<ItemStack>>();
		ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
		NonNullList<ItemStack> recipeItem = recipe.getInput().getApplicableItems();
		b:
		{
			if(recipeItem.size() == 1)
			{
				fixedInputs.add(recipeItem.get(0).copy());
				break b;
			}
			if(recipeItem.size() <= 0)
				break b;
			LinkedList<ItemStack> recipeItemOptions = new LinkedList<ItemStack>();
			for(ItemStack option : recipeItem)
				recipeItemOptions.add(option.copy());
			variableInputs.add(recipeItemOptions);
		}
		return Collections.singletonList(new CraftingIngredients(fixedInputs, variableInputs));
	}
}