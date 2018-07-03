package com.zeitheron.expequiv.exp.thaumcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.zeitheron.expequiv.exp.CraftingIngredients;
import com.zeitheron.expequiv.utils.CollectingHelper;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.collector.LongToBigFractionCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.config.Configuration;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.IThaumcraftRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;

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
		Iterator<IRecipe> recipes = CraftingManager.REGISTRY.iterator();
		while(recipes.hasNext())
		{
			IRecipe recipe = recipes.next();
			if(recipe instanceof IArcaneRecipe)
				mapArcaneRecipe(mapper, (IArcaneRecipe) recipe);
		}
		
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
	
	public void mapInfusionRecipe(IMappingCollector<NormalizedSimpleStack, Integer> map, InfusionRecipe recipe)
	{
		LongToBigFractionCollector<NormalizedSimpleStack, ?> mapper = CollectingHelper.getLTBFC(map);
		
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
		
		int pearls = 0;
		ItemStack primPearl = new ItemStack(ItemsTC.primordialPearl);
		for(int i = 0; i < items.size(); ++i)
			if(items.get(i).apply(primPearl))
			{
				items.remove(i);
				++pearls;
			}
		
		CraftingIngredients variation = CraftingIngredients.getIngredientsFor(items);
		IngredientMap ingredientMap = variation.toIngredients(map);
		
		if(ingredientMap == null)
			return;
		
		if(pearls > 0)
		{
			NormalizedSimpleStack fake = NSSFake.create(UUID.randomUUID() + "PrimPearls");
			mapper.setValueBefore(fake, (long) (pearls * tc.getPrimordialPearlCost() / ItemsTC.primordialPearl.getMaxDamage(primPearl)));
			ingredientMap.addIngredient(fake, 1);
		}
		
		AspectList vis = recipe.getAspects();
		for(Aspect a : vis.getAspects())
		{
			NormalizedSimpleStack fake = NSSFake.create(UUID.randomUUID() + a.getName() + "VisX" + vis.getAmount(a));
			mapper.setValueBefore(fake, (long) (tc.getAspectCost(a) * vis.getAmount(a)));
			ingredientMap.addIngredient(fake, 1);
		}
		
		NormalizedSimpleStack fake = NSSFake.create(UUID.randomUUID() + "Instability");
		mapper.setValueBefore(fake, (long) (tc.instabilityMod * recipe.instability));
		ingredientMap.addIngredient(fake, 1);
		
		mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
	}
	
	public void mapCrucibleRecipe(IMappingCollector<NormalizedSimpleStack, Integer> map, CrucibleRecipe recipe)
	{
		LongToBigFractionCollector<NormalizedSimpleStack, ?> mapper = CollectingHelper.getLTBFC(map);
		
		ItemStack recipeOutput = recipe.getRecipeOutput();
		if(recipeOutput.isEmpty())
			return;
		NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
		
		CraftingIngredients variation = CraftingIngredients.getIngredientsFor(Arrays.asList(recipe.getCatalyst()));
		IngredientMap ingredientMap = variation.toIngredients(map);
		
		if(ingredientMap == null)
			return;
		
		AspectList vis = recipe.getAspects();
		for(Aspect a : vis.getAspects())
		{
			NormalizedSimpleStack fake = NSSFake.create(UUID.randomUUID() + a.getName() + "VisX" + vis.getAmount(a));
			mapper.setValueBefore(fake, (long) (tc.getAspectCost(a) * vis.getAmount(a)));
			ingredientMap.addIngredient(fake, 1);
		}
		
		mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
	}
	
	public void mapArcaneRecipe(IMappingCollector<NormalizedSimpleStack, Integer> map, IArcaneRecipe recipe)
	{
		LongToBigFractionCollector<NormalizedSimpleStack, ?> mapper = CollectingHelper.getLTBFC(map);
		
		ItemStack recipeOutput = recipe.getRecipeOutput();
		if(recipeOutput.isEmpty())
			return;
		NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
		
		List<Ingredient> items = new ArrayList<>(recipe.getIngredients());
		
		int pearls = 0;
		ItemStack primPearl = new ItemStack(ItemsTC.primordialPearl);
		for(int i = 0; i < items.size(); ++i)
			if(items.get(i).apply(primPearl))
			{
				items.remove(i);
				++pearls;
			}
		
		CraftingIngredients variation = CraftingIngredients.getIngredientsFor(items);
		IngredientMap ingredientMap = variation.toIngredients(map);
		
		if(ingredientMap == null)
			return;
		
		if(pearls > 0)
		{
			NormalizedSimpleStack fake = NSSFake.create(UUID.randomUUID() + "PrimPearls");
			mapper.setValueBefore(fake, (long) (pearls * tc.getPrimordialPearlCost() / ItemsTC.primordialPearl.getMaxDamage(primPearl)));
			ingredientMap.addIngredient(fake, 1);
		}
		
		AspectList crystals = recipe.getCrystals();
		if(crystals != null)
			for(Aspect a : crystals.getAspects())
			{
				NormalizedSimpleStack fake = NSSFake.create(UUID.randomUUID() + a.getName() + "CrystalX" + crystals.getAmount(a));
				mapper.setValueBefore(fake, (long) (tc.getAspectCost(a) + tc.getVisCrystalCost()) * crystals.getAmount(a));
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