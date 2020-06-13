package com.zeitheron.expequiv.exp.astralsorcery;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import moze_intel.projecte.api.proxy.IEMCProxy;

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
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new StarlightInfusionEMCConverter());
		mappers.add(new AltarEMCConverter());
	}
}