package com.zeitheron.expequiv.exp.draconicevolution;

import java.util.List;
import java.util.Map;

import com.brandon3055.draconicevolution.DEFeatures;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.api.proxy.ITransmutationProxy;
import moze_intel.projecte.emc.EMCMapper;
import moze_intel.projecte.emc.SimpleGraphMapper;
import moze_intel.projecte.emc.arithmetics.HiddenFractionArithmetic;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.collector.IntToFractionCollector;
import moze_intel.projecte.emc.collector.WildcardSetValueFixCollector;
import moze_intel.projecte.emc.generators.FractionToIntGenerator;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

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
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new FusionEMCMapper());
	}
	
	@Override
	public void registerEMC(IEMCProxy emcp)
	{
		addEMC(DEFeatures.draconiumDust, "DraconiumDust");
		addEMC(DEFeatures.dragonHeart, "DragonHeart");
		addEMC(DEFeatures.chaosShard, "ChaosShard");
	}
}