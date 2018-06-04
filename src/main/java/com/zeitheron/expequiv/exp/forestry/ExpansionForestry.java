package com.zeitheron.expequiv.exp.forestry;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import forestry.apiculture.ModuleApiculture;
import forestry.arboriculture.ModuleArboriculture;
import forestry.core.ModuleCore;
import forestry.cultivation.ModuleCultivation;
import forestry.food.ModuleFood;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

@ExpansionReg(modid = "forestry")
public class ExpansionForestry extends Expansion
{
	public ExpansionForestry(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(24, "Fruits", "all fruits");
		addEMCCfg(64, "Beeswax");
		addEMCCfg(128, "HoneyDrop");
		addEMCCfg(64, "Propolis");
		addEMCCfg(256, "StickyPropolis");
		addEMCCfg(40, "PulsaingPropolis");
		addEMCCfg(512, "SilkyPropolis");
		addEMCCfg(32, "PollenCluster");
		addEMCCfg(512, "CrystallinePollenCluster");
		addEMCCfg(16_384, "ProvenGrafter");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		for(int i = 0; i < 7; ++i)
			addEMC(ModuleCore.items.fruits, i, "Fruits");
		addEMC(ModuleCore.items.beeswax, "Beeswax");
		addEMC(ModuleApiculture.getItems().honeyDrop, "HoneyDrop");
		addEMC(ModuleApiculture.getItems().propolis, 0, "Propolis");
		addEMC(ModuleApiculture.getItems().propolis, 1, "StickyPropolis");
		addEMC(ModuleApiculture.getItems().propolis, 2, "PulsaingPropolis");
		addEMC(ModuleApiculture.getItems().propolis, 3, "SilkyPropolis");
		addEMC(ModuleApiculture.getItems().pollenCluster, 0, "PollenCluster");
		addEMC(ModuleApiculture.getItems().pollenCluster, 1, "CrystallinePollenCluster");
		addEMC(ModuleArboriculture.getItems().grafterProven, "ProvenGrafter");
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new CarpenterEMCMapper());
		mappers.add(new ThermionicEMCMapper());
		mappers.add(new MoistenerEMCMapper());
	}
}