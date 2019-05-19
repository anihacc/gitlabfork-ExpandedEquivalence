package com.zeitheron.expequiv.exp.avaritia;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import morph.avaritia.init.ModItems;
import moze_intel.projecte.api.proxy.IEMCProxy;

@ExpansionReg(modid = "avaritia")
public class ExpansionAvaritia extends Expansion
{
	public ExpansionAvaritia(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(512, "NeutronPile");
	}
	
	@Override
	public void registerEMC(IEMCProxy emcProxy)
	{
		addEMC(ModItems.neutron_pile.getItem(), ModItems.neutron_pile.getItemDamage(), "NeutronPile");
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new CompressionEMCConverter());
		mappers.add(new ExtremeEMCConverter());
	}
}