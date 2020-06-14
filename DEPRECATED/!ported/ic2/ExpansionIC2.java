package com.zeitheron.expequiv.exp.ic2;

import java.util.List;
import java.util.function.Function;

import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import ic2.api.recipe.Recipes;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.IngredientMap;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ExpansionReg(modid = "ic2")
public class ExpansionIC2 extends Expansion
{
	public ExpansionIC2(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(32 * 1024, "IridiumOre");
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
		
		IEMC e = IEMC.PE_WRAPPER;
		e.register(new ItemStack(nuclear, 1, 2), 4096);
		e.register(new ItemStack(nuclear, 1, 5), 4096);
		e.register(new ItemStack(dust, 1, 0), 160);
		e.register(new ItemStack(dust, 1, 1), 32);
		e.register(new ItemStack(dust, 1, 4), 128);
		e.register(new ItemStack(dust, 1, 5), 8192);
		e.register(new ItemStack(dust, 1, 7), 2048);
		e.register(new ItemStack(dust, 1, 8), 256);
		e.register(new ItemStack(dust, 1, 9), 864);
		e.register(new ItemStack(dust, 1, 10), 512);
		e.register(new ItemStack(dust, 1, 12), 64);
		e.register(new ItemStack(dust, 1, 14), 512);
		e.register(new ItemStack(dust, 1, 15), 1);
		e.register(new ItemStack(dust, 1, 17), 256);
		e.register(new ItemStack(dust, 1, 24), (256 * 2));
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		// Map EMC based of IC2's recipes. Oh please!
		mappers.add(new AdvRecipeEMCConverter());
		
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.macerator));
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.blastfurnace));
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.blockcutter));
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.centrifuge));
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.compressor));
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.extractor));
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.metalformerCutting));
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.metalformerExtruding));
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.metalformerRolling));
		mappers.add(new DefaultIC2MachineEMCConverter(Recipes.oreWashing));
	}
	
	private void compact(IMappingCollector<NormalizedSimpleStack, Integer> mapper, ItemStack in, ItemStack out)
	{
		NormalizedSimpleStack output = NSSItem.create(out);
		IngredientMap<NormalizedSimpleStack> im = new IngredientMap<>();
		im.addIngredient(NSSItem.create(in), in.getCount());
		mapper.addConversion(out.getCount(), output, im.getMap());
	}
}