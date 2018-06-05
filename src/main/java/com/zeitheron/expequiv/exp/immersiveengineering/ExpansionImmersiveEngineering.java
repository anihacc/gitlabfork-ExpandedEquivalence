package com.zeitheron.expequiv.exp.immersiveengineering;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import blusunrize.immersiveengineering.common.IEContent;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

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
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new KilnEMCMapper());
		mappers.add(new BlastFurnaceEMCMapper());
		mappers.add(new CokeEMCMapper());
		mappers.add(new MetalPressEMCMapper());
		mappers.add(new CrusherEMCMapper());
		mappers.add(new EnginnerWorkbenchEMCMapper());
	}
}