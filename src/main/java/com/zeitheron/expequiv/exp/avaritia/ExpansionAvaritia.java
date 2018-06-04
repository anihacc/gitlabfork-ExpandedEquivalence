package com.zeitheron.expequiv.exp.avaritia;

import com.zeitheron.expequiv.exp.ExpansionReg;

import morph.avaritia.init.ModItems;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;

@ExpansionReg(modid = "avaritia")
public class ExpansionAvaritia extends Expansion
{
	public ExpansionAvaritia(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(512, "NeutronPile");
	}
	
	@Override
	public void registerEMC(IEMCProxy emcProxy)
	{
		addEMC(ModItems.neutron_pile.getItem(), ModItems.neutron_pile.getItemDamage(), "NeutronPile");
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new CompressionEMCMapper());
		mappers.add(new ExtremeEMCMapper());
	}
}