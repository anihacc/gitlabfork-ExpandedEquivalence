package com.zeitheron.expequiv.exp.ic2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import moze_intel.projecte.PECore;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import moze_intel.projecte.gameObjs.customRecipes.RecipeShapedKleinStar;
import moze_intel.projecte.gameObjs.customRecipes.RecipeShapelessHidden;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.logging.log4j.Logger;

import ic2.core.recipe.AdvRecipe;
import ic2.core.recipe.AdvShapelessRecipe;

class AdvRecipeEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	private final Set<Class> canNotMap = new HashSet<Class>();
	private final Map<Class, Integer> recipeCount = new HashMap<Class, Integer>();
	
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		this.recipeCount.clear();
		this.canNotMap.clear();
		block2: for(IRecipe recipe : CraftingManager.REGISTRY)
		{
			if(!(recipe instanceof AdvRecipe) && !(recipe instanceof AdvShapelessRecipe))
				continue;
			boolean handled = false;
			ItemStack recipeOutput = recipe.getRecipeOutput();
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
						ingredientMap.addIngredient(NSSItem.create(stack), 1);
					} catch(Exception e)
					{
						PECore.LOGGER.fatal("Exception in CraftingMapper when parsing Recipe Ingredients: RecipeType: {}, Ingredient: {}", (Object) recipe.getClass().getName(), (Object) stack.toString());
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
		return "AdvCraftingMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for Adv Crafting Recipes gathered from net.minecraft.item.crafting.CraftingManager";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	public Iterable<CraftingIngredients> getIngredientsFor(IRecipe recipe)
	{
		ArrayList<Iterable<ItemStack>> variableInputs = new ArrayList<Iterable<ItemStack>>();
		ArrayList<ItemStack> fixedInputs = new ArrayList<ItemStack>();
		for(Ingredient recipeItem : recipe.getIngredients())
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
			{
				recipeItemOptions.add(option.copy());
			}
			variableInputs.add(recipeItemOptions);
		}
		return Collections.singletonList(new CraftingIngredients(fixedInputs, variableInputs));
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
}