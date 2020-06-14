package com.zeitheron.expequiv.exp.embers;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import moze_intel.projecte.api.proxy.IEMCProxy;
import teamroots.embers.RegistryManager;

@ExpansionReg(modid = "embers")
public class ExpansionEmbers extends Expansion
{
	public ExpansionEmbers(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(120, "EmberShard");
		addEMCCfg(1024 * 3, "DawnstoneIngot");
		addEMCCfg(32, "AlchemicalWaste");
		addEMCCfg(32, "AshDust");
		addEMCCfg(32, "ArchaicBrick");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		addEMC(RegistryManager.dust_ash, "AshDust");
		addEMC(RegistryManager.shard_ember, "EmberShard");
		addEMC(RegistryManager.ingot_dawnstone, "DawnstoneIngot");
		addEMC(RegistryManager.alchemic_waste, "AlchemicalWaste");
		addEMC(RegistryManager.archaic_brick, "ArchaicBrick");
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new AlchemyEMCConverter());
		mappers.add(new StamperEMCConverter());
	}
}