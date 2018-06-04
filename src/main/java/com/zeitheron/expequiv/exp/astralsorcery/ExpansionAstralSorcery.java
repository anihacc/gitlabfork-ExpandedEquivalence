package com.zeitheron.expequiv.exp.astralsorcery;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraftforge.common.config.Configuration;

@ExpansionReg(modid = "astralsorcery")
public class ExpansionAstralSorcery extends Expansion
{
	public ExpansionAstralSorcery(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(256, "Aquamarine");
		addEMCCfg(512, "Starmetal");
	}
	
	@Override
	public void registerEMC(IEMCProxy emcProxy)
	{
		addEMC(ItemsAS.craftingComponent, 0, "Aquamarine");
		addEMC(ItemsAS.craftingComponent, 1, "Starmetal");
		addEMC(ItemsAS.craftingComponent, 2, "Starmetal");
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new StarlightInfusionEMCMapper());
		mappers.add(new AltarEMCMapper());
	}
}