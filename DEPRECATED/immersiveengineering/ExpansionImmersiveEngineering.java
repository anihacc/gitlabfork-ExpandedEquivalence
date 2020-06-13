package com.zeitheron.expequiv.exp.immersiveengineering;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import blusunrize.immersiveengineering.common.IEContent;
import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.item.Item;

@ExpansionReg(modid = "immersiveengineering")
public class ExpansionImmersiveEngineering extends Expansion
{
	public ExpansionImmersiveEngineering(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(320, "SteelIngot");
		addEMCCfg(128, "CokeDust");
		addEMCCfg(128 * 8, "HOPGraphiteDust", "HOP Graphite Dust");
		addEMCCfg(12, "TreatedWood");
		addEMCCfg(24, "IndustrialHempFiber");
		addEMCCfg(16, "Slag");
		addEMCCfg(32, "Nitrate");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		addEMC(IEContent.itemMetal, 8, "SteelIngot");
		addEMC(IEContent.itemMaterial, 4, "IndustrialHempFiber");
		addEMC(IEContent.itemMaterial, 7, "Slag");
		addEMC(IEContent.itemMaterial, 17, "CokeDust");
		addEMC(IEContent.itemMaterial, 18, "HOPGraphiteDust");
		addEMC(IEContent.itemMaterial, 19, "HOPGraphiteDust");
		addEMC(IEContent.itemMaterial, 24, "Nitrate");
		addEMC(Item.getItemFromBlock(IEContent.blockTreatedWood), "TreatedWood");
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new BlastFurnaceEMCConverter());
		mappers.add(new CokeEMCConverter());
		mappers.add(new CrusherEMCConverter());
		mappers.add(new EnginnerWorkbenchEMCConverter());
		mappers.add(new KilnEMCMapper());
		mappers.add(new MetalPressEMCConverter());
	}
}