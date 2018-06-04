package com.zeitheron.expequiv.exp.compactmachines3;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraftforge.common.config.Configuration;

@ExpansionReg(modid = "compactmachines3")
public class ExpansionCompactMachines extends Expansion
{
	public ExpansionCompactMachines(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new MiniaturizationEMCMapper());
	}
}