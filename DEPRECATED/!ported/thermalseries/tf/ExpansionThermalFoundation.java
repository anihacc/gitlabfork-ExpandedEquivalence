package com.zeitheron.expequiv.exp.thermalseries.tf;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import cofh.thermalfoundation.init.TFItems;
import moze_intel.projecte.api.proxy.IEMCProxy;

@ExpansionReg(modid = "thermalfoundation")
public class ExpansionThermalFoundation extends Expansion
{
	public ExpansionThermalFoundation(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(1280, "EnderiumIngot");
		addEMCCfg(16, "Slag");
		addEMCCfg(128, "RichSlag");
		addEMCCfg(512, "Cinnabar");
		addEMCCfg(256, "Bitumen");
		addEMCCfg(1536, "BlizzRod");
		addEMCCfg(1536, "BlitzRod");
		addEMCCfg(1536, "BasalzRod");
		addEMCCfg(1536 / 4, "BlizzPowder");
		addEMCCfg(1536 / 4, "BlitzPowder");
		addEMCCfg(1536 / 4, "BasalzPowder");
	}
	
	@Override
	public void registerEMC(IEMCProxy emcProxy)
	{
		addEMC(TFItems.itemMaterial, 167, "EnderiumIngot");
		addEMC(TFItems.itemMaterial, 864, "Slag");
		addEMC(TFItems.itemMaterial, 865, "RichSlag");
		addEMC(TFItems.itemMaterial, 866, "Cinnabar");
		addEMC(TFItems.itemMaterial, 892, "Bitumen");
		addEMC(TFItems.itemMaterial, 2048, "BlizzRod");
		addEMC(TFItems.itemMaterial, 2050, "BlitzRod");
		addEMC(TFItems.itemMaterial, 2052, "BasalzRod");
		addEMC(TFItems.itemMaterial, 2049, "BlizzPowder");
		addEMC(TFItems.itemMaterial, 2051, "BlitzPowder");
		addEMC(TFItems.itemMaterial, 2053, "BasalzPowder");
	}
}