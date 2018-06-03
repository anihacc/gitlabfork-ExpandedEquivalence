package com.zeitheron.expequiv.exp.botania;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import vazkii.botania.common.block.ModBlocks;

@ExpansionReg(modid = "botania")
public class ExpansionBotania extends Expansion
{
	public ExpansionBotania(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	public void preInit(Configuration configs)
	{
		
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		for(int i = 0; i < 16; ++i)
			emc.registerCustomEMC(new ItemStack(ModBlocks.flower, 1, i), 16);
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new ManaPoolEMCMapper());
		mappers.add(new ElvenTradeEMCMapper());
		mappers.add(new PureDaisyEMCMapper());
		mappers.add(new TerrestrialEMCMapper());
	}
}