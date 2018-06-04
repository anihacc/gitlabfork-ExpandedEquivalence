package com.zeitheron.expequiv.exp.ic2;

import java.util.List;
import java.util.function.Function;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import ic2.api.recipe.Recipes;
import ic2.core.recipe.AdvCraftingRecipeManager;
import ic2.core.recipe.AdvRecipe;
import ic2.core.recipe.AdvShapelessRecipe;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ExpansionReg(modid = "ic2")
public class ExpansionIC2 extends Expansion implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	public ExpansionIC2(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(16384, "IdiriumOre");
		addEMCCfg(320, "SteelIngot");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		Function<String, Item> find = reg -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("ic2", reg));
		Item resourceBlock = find.apply("resource");
		Item ingot = find.apply("ingot");
		Item nuclear = find.apply("nuclear");
		Item dust = find.apply("dust");
		Item misc_resource = find.apply("misc_resource");
		addEMC(ingot, 5, "SteelIngot");
		addEMC(misc_resource, 1, "IridiumOre");
		emc.registerCustomEMC(new ItemStack(nuclear, 1, 2), 4096);
		emc.registerCustomEMC(new ItemStack(nuclear, 1, 5), 4096);
		emc.registerCustomEMC(new ItemStack(dust, 1, 0), 160);
		emc.registerCustomEMC(new ItemStack(dust, 1, 1), 32);
		emc.registerCustomEMC(new ItemStack(dust, 1, 4), 128);
		emc.registerCustomEMC(new ItemStack(dust, 1, 5), 8192);
		emc.registerCustomEMC(new ItemStack(dust, 1, 7), 2048);
		emc.registerCustomEMC(new ItemStack(dust, 1, 8), 256);
		emc.registerCustomEMC(new ItemStack(dust, 1, 9), 864);
		emc.registerCustomEMC(new ItemStack(dust, 1, 10), 512);
		emc.registerCustomEMC(new ItemStack(dust, 1, 12), 64);
		emc.registerCustomEMC(new ItemStack(dust, 1, 14), 512);
		emc.registerCustomEMC(new ItemStack(dust, 1, 15), 1);
		emc.registerCustomEMC(new ItemStack(dust, 1, 17), 256);
		emc.registerCustomEMC(new ItemStack(dust, 1, 24), (256 * 2));
		
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(this);
		
		// Map EMC based of IC2's recipes. Oh please!
		mappers.add(new AdvRecipeEMCMapper());
		
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.macerator));
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.blastfurnace));
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.blockcutter));
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.centrifuge));
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.compressor));
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.extractor));
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.metalformerCutting));
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.metalformerExtruding));
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.metalformerRolling));
		mappers.add(new DefaultIC2MachineEMCMapper(Recipes.oreWashing));
	}
	
	private void compact(IMappingCollector<NormalizedSimpleStack, Integer> mapper, ItemStack in, ItemStack out)
	{
		NormalizedSimpleStack output = NSSItem.create(out);
		IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
		im.addIngredient(NSSItem.create(in), in.getCount());
		mapper.addConversion(out.getCount(), output, im.getMap());
	}
	
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
	}
	
	@Override
	public String getName()
	{
		return "IC2CoreMapper";
	}
	
	@Override
	public String getDescription()
	{
		return "Add Conversions for IC2 core items";
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
}