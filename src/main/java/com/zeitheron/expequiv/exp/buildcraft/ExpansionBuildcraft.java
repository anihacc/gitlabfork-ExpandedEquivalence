package com.zeitheron.expequiv.exp.buildcraft;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraftforge.common.config.Configuration;

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
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new LaserTableEMCMapper());
	}
}