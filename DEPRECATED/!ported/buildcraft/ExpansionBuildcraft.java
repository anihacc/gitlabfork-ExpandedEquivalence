package com.zeitheron.expequiv.exp.buildcraft;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import moze_intel.projecte.api.proxy.IEMCProxy;

@ExpansionReg(modid = "buildcraftcore")
public class ExpansionBuildcraft extends Expansion
{
	public ExpansionBuildcraft(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new LaserTableEMCConverter());
	}
}