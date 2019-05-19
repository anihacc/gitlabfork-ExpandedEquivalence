package com.zeitheron.expequiv.exp.thermalseries.te;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

@ExpansionReg(modid = "thermalexpansion")
public class ExpansionThermalExpansion extends Expansion
{
	public ExpansionThermalExpansion(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new BrewerEMCConverter());
		mappers.add(new CentrifugeEMCConverter());
		mappers.add(new ChargerEMCConverter());
		mappers.add(new CompactorEMCConverter());
		mappers.add(new CrucibleEMCConverter());
		mappers.add(new EnchanterEMCConverter());
		mappers.add(new ExtruderEMCConverter());
		mappers.add(new FurnaceEMCConverter());
		mappers.add(new InsolatorEMCConverter());
		mappers.add(new PrecipitatorEMCConverter());
		mappers.add(new PulverizerEMCConverter());
		mappers.add(new RefineryEMCConverter());
		mappers.add(new SawmillEMCConverter());
		mappers.add(new SmelterEMCConverter());
		mappers.add(new TransposerEMCConverter());
	}
}