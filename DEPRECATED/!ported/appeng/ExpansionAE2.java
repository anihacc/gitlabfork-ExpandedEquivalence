package com.zeitheron.expequiv.exp.appeng;

import java.util.List;

import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import appeng.api.AEApi;
import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.item.ItemStack;

@ExpansionReg(modid = "appliedenergistics2")
public class ExpansionAE2 extends Expansion
{
	public ExpansionAE2(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(64, "SkyStone");
		addEMCCfg(256, "MatterBall");
		addEMCCfg(256_000, "Singularity");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		IEMC e = IEMC.PE_WRAPPER;
		
		e.register(AEApi.instance().definitions().materials().certusQuartzCrystalCharged().maybeStack(1).orElse(ItemStack.EMPTY), 64);
		addEMC(AEApi.instance().definitions().materials().matterBall().maybeItem().orElse(null), 6, "MatterBall");
		e.register(AEApi.instance().definitions().materials().purifiedCertusQuartzCrystal().maybeStack(1).orElse(ItemStack.EMPTY), 32);
		e.register(AEApi.instance().definitions().materials().purifiedNetherQuartzCrystal().maybeStack(1).orElse(ItemStack.EMPTY), 128);
		e.register(AEApi.instance().definitions().materials().purifiedFluixCrystal().maybeStack(1).orElse(ItemStack.EMPTY), 128);
		addEMC(AEApi.instance().definitions().materials().singularity().maybeItem().orElse(null), 47, "Singularity");
		addEMC(AEApi.instance().definitions().blocks().skyStoneBlock().maybeItem().orElse(null), "SkyStone");
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new InscriberEMCConverter());
		mappers.add(new GrinderEMCConverter());
	}
}