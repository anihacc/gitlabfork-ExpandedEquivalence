package com.zeitheron.expequiv.exp.basemetals;

import java.util.List;
import java.util.function.Function;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ExpansionReg(modid = "basemetals")
public class ExpansionBaseMetals extends Expansion
{
	public ExpansionBaseMetals(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(2048, "AdamantineIngot");
		addEMCCfg(1024, "AntimonyIngot");
		addEMCCfg(1024, "AquariumIngot");
		addEMCCfg(1024, "BismuthIngot");
		addEMCCfg((480 + 128 * 3) / 4, "BrassIngot");
		addEMCCfg(256, "ColdIronIngot");
		addEMCCfg((1024 + 128 * 3) / 4, "CupronickelIngot");
		addEMCCfg(256, "LiquidMercury");
		addEMCCfg(1024, "MithrilIngot");
		addEMCCfg(512, "PewterIngot");
		addEMCCfg(2048, "StarSteelIngot");
		addEMCCfg(320, "SteelIngot");
		addEMCCfg(480, "ZincIngot");
	}
	
	@Override
	public void registerEMC(IEMCProxy emcProxy)
	{
		Function<String, Item> find = str -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("basemetals", str));

		addEMC(find.apply("adamantine_ingot"), "AdamantineIngot");
		addEMC(find.apply("antimony_ingot"), "AntimonyIngot");
		addEMC(find.apply("aquarium_ingot"), "AquariumIngot");
		addEMC(find.apply("bismuth_ingot"), "BismuthIngot");
		addEMC(find.apply("brass_ingot"), "BrassIngot");
		addEMC(find.apply("coldiron_ingot"), "ColdIronIngot");
		addEMC(find.apply("cupronickel_ingot"), "CupronickelIngot");
		addEMC(find.apply("mercury_ingot"), "LiquidMercury");
		addEMC(find.apply("mithril_ingot"), "MithrilIngot");
		addEMC(find.apply("pewter_ingot"), "PewterIngot");
		addEMC(find.apply("starsteel_ingot"), "StarSteelIngot");
		addEMC(find.apply("steel_ingot"), "SteelIngot");
		addEMC(find.apply("zinc_ingot"), "ZincIngot");
	}
}