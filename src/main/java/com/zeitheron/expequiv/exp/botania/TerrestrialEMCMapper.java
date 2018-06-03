package com.zeitheron.expequiv.exp.botania;

import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSFake;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.obj.OBJModel.Normal;
import net.minecraftforge.common.config.Configuration;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.item.ModItems;

class TerrestrialEMCMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		ItemStack output = new ItemStack(ModItems.manaResource, 1, 4);
		int mana = 600_000;
		
		NormalizedSimpleStack manaCost = NSSFake.create("mana." + output.hashCode());
		mapper.setValueBefore(manaCost, MathHelper.ceil(mana / 10F));
		
		IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
		addInput(new ItemStack(ModItems.manaResource, 1, 0), im);
		addInput(new ItemStack(ModItems.manaResource, 1, 1), im);
		addInput(new ItemStack(ModItems.manaResource, 1, 2), im);
		im.addIngredient(manaCost, 1);
		mapper.addConversion(output.getCount(), NSSItem.create(output), im.getMap());
	}
	
	private void addInput(ItemStack stack, IngredientMap<NormalizedSimpleStack> im)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "BTTerraMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for terrestrial agglomeration plate recipes";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}