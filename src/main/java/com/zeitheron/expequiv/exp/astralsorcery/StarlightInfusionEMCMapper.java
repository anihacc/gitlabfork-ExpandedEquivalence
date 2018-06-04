package com.zeitheron.expequiv.exp.astralsorcery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

class StarlightInfusionEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		Set<AbstractInfusionRecipe> recipes = new HashSet<>();
		recipes.addAll(InfusionRecipeRegistry.recipes);
		recipes.addAll(InfusionRecipeRegistry.mtRecipes);
		
		block2: for(AbstractInfusionRecipe recipe : recipes)
		{
			ItemStack recipeOutput = recipe.getOutputForMatching();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			
			for(CraftingIngredients variation : getIngredientsFor(recipe))
			{
				IngredientMap ingredientMap = new IngredientMap();
				
				NormalizedSimpleStack additionalEMC = NSSFake.create(recipe.toString());
				mapper.setValueBefore(additionalEMC, (int) (1000 * recipe.getLiquidStarlightConsumptionChance()));
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