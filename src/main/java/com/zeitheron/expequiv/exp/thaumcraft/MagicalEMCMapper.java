package com.zeitheron.expequiv.exp.thaumcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zeitheron.expequiv.exp.CraftingIngredients;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.config.Configuration;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.IThaumcraftRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

class MagicalEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	ExpansionThaumcraft tc;
	
	public MagicalEMCMapper(ExpansionThaumcraft tc)
	{
		this.tc = tc;
	}
	
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration arg1)
	{
		for(IThaumcraftRecipe recipe : ThaumcraftApi.getCraftingRecipes().values())
		{
			if(recipe instanceof IArcaneRecipe)
				mapArcaneRecipe(mapper, (IArcaneRecipe) recipe);
			if(recipe instanceof CrucibleRecipe)
				mapCrucibleRecipe(mapper, (CrucibleRecipe) recipe);
			if(recipe instanceof InfusionRecipe)
				mapInfusionRecipe(mapper, (InfusionRecipe) recipe);
		}
	}
	
	public void mapInfusionRecipe(IMappingCollector<NormalizedSimpleStack, Integer> mapper, InfusionRecipe recipe)
	{
		ItemStack recipeOutput = ItemStack.EMPTY;
		{
			Object o = recipe.getRecipeOutput();
			if(o instanceof ItemStack)
				recipeOutput = (ItemStack) o;
			if(o instanceof Item)
				recipeOutput = new ItemStack((Item) o);
			if(o instanceof Block)
				recipeOutput = new ItemStack((Block) o);
		}
		if(recipeOutput.isEmpty())
			return;
		NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
		
		List<Ingredient> items = new ArrayList<>();
		items.add(recipe.sourceInput);
		items.addAll(recipe.getComponents());
		CraftingIngredients variation = CraftingIngredients.getIngredientsFor(items);
		IngredientMap ingredientMap = variation.toIngredients(mapper);
		
		if(ingredientMap == null)
			return;
		
		AspectList vis = recipe.getAspects();
		for(Aspect a : vis.getAspects())
		{
			NormalizedSimpleStack fake = NSSFake.create(a.getName() + "Vis");
			mapper.setValueBefore(fake, tc.defAspectCosts.get(a) * vis.getAmount(a));
			ingredientMap.addIngredient(fake, 1);
		}
		
		NormalizedSimpleStack fake = NSSFake.create(recipe.toString() + "Instability");
		mapper.setValueBefore(fake, tc.instabilityMod * recipe.instability);
		ingredientMap.addIngredient(fake, 1);
		
		mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
	}
	
	public void mapCrucibleRecipe(IMappingCollector<NormalizedSimpleStack, Integer> mapper, CrucibleRecipe recipe)
	{
		ItemStack recipeOutput = recipe.getRecipeOutput();
		if(recipeOutput.isEmpty())
			return;
		NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
		
		CraftingIngredients variation = CraftingIngredients.getIngredientsFor(Arrays.asList(recipe.getCatalyst()));
		IngredientMap ingredientMap = variation.toIngredients(mapper);
		
		if(ingredientMap == null)
			return;
		
		AspectList vis = recipe.getAspects();
		for(Aspect a : vis.getAspects())
		{
			NormalizedSimpleStack fake = NSSFake.create(a.getName() + "Vis");
			mapper.setValueBefore(fake, tc.defAspectCosts.get(a) * vis.getAmount(a));
			ingredientMap.addIngredient(fake, 1);
		}
		
		mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
	}
	
	public void mapArcaneRecipe(IMappingCollector<NormalizedSimpleStack, Integer> mapper, IArcaneRecipe recipe)
	{
		ItemStack recipeOutput = recipe.getRecipeOutput();
		if(recipeOutput.isEmpty())
			return;
		NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
		
		CraftingIngredients variation = CraftingIngredients.getIngredientsFor(recipe.getIngredients());
		IngredientMap ingredientMap = variation.toIngredients(mapper);
		
		if(ingredientMap == null)
			return;
		
		AspectList crystals = recipe.getCrystals();
		for(Aspect a : crystals.getAspects())
		{
			NormalizedSimpleStack fake = NSSFake.create(a.getName() + "Crystal");
			mapper.setValueBefore(fake, tc.defAspectCosts.get(a) * crystals.getAmount(a) + tc.getVisCrystalCost());
			ingredientMap.addIngredient(fake, 1);
		}
		
		mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
	}
	
	@Override
	public String getName()
	{
		return "TCMagicalMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for arcane, crucible and infusion recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}