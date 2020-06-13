package com.zeitheron.expequiv.exp.draconicevolution;

import java.util.List;

import com.brandon3055.draconicevolution.DEFeatures;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import moze_intel.projecte.api.proxy.IEMCProxy;

@ExpansionReg(modid = "draconicevolution")
public class ExpansionDraconicEvolution extends Expansion
{
	public ExpansionDraconicEvolution(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(8192, "DraconiumDust");
		addEMCCfg(139264, "DragonHeart");
		addEMCCfg(1024000, "ChaosShard");
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new FusionEMCConverter());
	}
	
	@Override
	public void registerEMC(IEMCProxy emcp)
	{
		addEMC(DEFeatures.draconiumDust, "DraconiumDust");
		addEMC(DEFeatures.dragonHeart, "DragonHeart");
		addEMC(DEFeatures.chaosShard, "ChaosShard");
	}
}