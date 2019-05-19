package com.zeitheron.expequiv.exp.reborncore.techreborn;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ExpansionReg(modid = "techreborn")
public class ExpansionTechReborn extends Expansion
{
	public ExpansionTechReborn(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(32, "Sap");
		addEMCCfg(3968, "IridiumNugget");
	}
	
	@Override
	public void registerEMC(IEMCProxy emcProxy)
	{
		Item part = ForgeRegistries.ITEMS.getValue(new ResourceLocation("techreborn", "part"));
		Item nuggets = ForgeRegistries.ITEMS.getValue(new ResourceLocation("techreborn", "nuggets"));
		
		addEMC(part, 31, "Sap");
		addEMC(nuggets, 7, "IridiumNugget");
	}
}