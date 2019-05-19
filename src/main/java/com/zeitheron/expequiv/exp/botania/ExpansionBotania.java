package com.zeitheron.expequiv.exp.botania;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.Item;
import vazkii.botania.common.block.ModBlocks;

@ExpansionReg(modid = "botania")
public class ExpansionBotania extends Expansion
{
	public ExpansionBotania(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(16, "Flower");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		for(int i = 0; i < 16; ++i)
			addEMC(Item.getItemFromBlock(ModBlocks.flower), i, "Flower");
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new ManaPoolEMCConverter());
		mappers.add(new ElvenTradeEMCConverter());
		mappers.add(new PureDaisyEMCConverter());
		mappers.add(new TerrestrialEMCConverter());
	}
}