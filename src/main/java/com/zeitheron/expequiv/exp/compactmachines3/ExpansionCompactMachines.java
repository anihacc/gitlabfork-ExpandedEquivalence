package com.zeitheron.expequiv.exp.compactmachines3;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

@ExpansionReg(modid = "compactmachines3")
public class ExpansionCompactMachines extends Expansion
{
	public ExpansionCompactMachines(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new MiniaturizationEMCConverter());
	}
}