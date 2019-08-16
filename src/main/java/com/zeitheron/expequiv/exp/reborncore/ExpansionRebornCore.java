package com.zeitheron.expequiv.exp.reborncore;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

@ExpansionReg(modid = "reborncore")
public class ExpansionRebornCore extends Expansion
{
	public ExpansionRebornCore(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new RebornCoreEMCConverter());
	}
}