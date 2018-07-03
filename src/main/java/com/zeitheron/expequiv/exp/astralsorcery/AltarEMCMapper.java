package com.zeitheron.expequiv.exp.astralsorcery;

import java.util.HashSet;
import java.util.Set;

import com.zeitheron.expequiv.exp.CraftingIngredients;
import com.zeitheron.expequiv.utils.CollectingHelper;

import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.collector.LongToBigFractionCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

class AltarEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> map, Configuration config)
	{
		LongToBigFractionCollector<NormalizedSimpleStack, ?> mapper = CollectingHelper.getLTBFC(map);
		
		Set<AbstractAltarRecipe> recipes = new HashSet<>();
		AltarRecipeRegistry.recipes.values().forEach(recipes::addAll);
		AltarRecipeRegistry.mtRecipes.values().forEach(recipes::addAll);
		
		for(AbstractAltarRecipe recipe : recipes)
		{
			ItemStack recipeOutput = recipe.getOutputForMatching();
			if(recipeOutput.isEmpty())
				continue;
			NormalizedSimpleStack recipeOutputNorm = NSSItem.create(recipeOutput);
			
			CraftingIngredients variation = CraftingIngredients.getIngredientsFor(recipe.getNativeRecipe().getIngredients());
			
			IngredientMap ingredientMap = variation.toIngredients(map);
			
			if(ingredientMap == null)
				continue;
			
			NormalizedSimpleStack additionalEMC = NSSFake.create(recipe.toString());
			mapper.setValueBefore(additionalEMC, (long) (100 * recipe.getCraftExperience() * recipe.getCraftExperienceMultiplier()));
			ingredientMap.addIngredient(additionalEMC, 1);
			
			mapper.addConversion(recipeOutput.getCount(), recipeOutputNorm, ingredientMap.getMap());
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
}