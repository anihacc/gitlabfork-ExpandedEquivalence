package com.zeitheron.expequiv.exp.avaritia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
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

class ExtremeEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	private final List<IRecipeMapper> recipeMappers = Arrays.asList(new AXRecipeMapper());
	private final Set<Class> canNotMap = new HashSet<Class>();
	private final Map<Class, Integer> recipeCount = new HashMap<Class, Integer>();
	
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		this.recipeCount.clear();
		this.canNotMap.clear();
		block2: for(IExtremeRecipe recipe : AvaritiaRecipeManager.EXTREME_RECIPES.values())
		{
			boolean handled = false;
			ItemStack recipeOutput = recipe.getRecipeOutput();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			for(IRecipeMapper recipeMapper : this.recipeMappers)
			{
				handled = true;
				for(CraftingIngredients variation : recipeMapper.getIngredientsFor(recipe))
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
			if(!handled)
				continue;
			int count = 0;
			if(this.recipeCount.containsKey(recipe.getClass()))
				count = this.recipeCount.get(recipe.getClass());
			this.recipeCount.put(recipe.getClass(), ++count);
		}
	}
	
	@Override
	public String getName()
	{
		return "ARExtremeMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for extreme recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	private static class AXRecipeMapper implements IRecipeMapper
	{
		private AXRecipeMapper()
		{
		}
		
		@Override
		public String getName()
		{
			return "AXRecipeMapper";
		}
		
		@Override
		public String getDescription()
		{
			return "Maps `IExtremeRecipe` crafting recipes";
		}
		
		@Override
		public boolean canHandle(IExtremeRecipe recipe)
		{
			return true;
		}
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
	
	public static interface IRecipeMapper
	{
		public String getName();
		
		public String getDescription();
		
		public boolean canHandle(IExtremeRecipe var1);
		
		default public Iterable<CraftingIngredients> getIngredientsFor(IExtremeRecipe recipe)
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
					recipeItemOptions.add(option.copy());
				variableInputs.add(recipeItemOptions);
			}
			return Collections.singletonList(new CraftingIngredients(fixedInputs, variableInputs));
		}
	}
}