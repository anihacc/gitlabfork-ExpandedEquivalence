package com.zeitheron.expequiv.exp.actuallyadditions;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import de.ellpeck.actuallyadditions.mod.blocks.InitBlocks;
import de.ellpeck.actuallyadditions.mod.items.InitItems;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

@ExpansionReg(modid = "actuallyadditions")
public class ExpansionActuallyAdditions extends Expansion
{
	public ExpansionActuallyAdditions(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(64, "Canola");
		addEMCCfg(64, "CoffeBeans");
		addEMCCfg(64, "Rice");
		addEMCCfg(480, "BatWings");
		addEMCCfg(863, "SolidifiedXP");
		addEMCCfg(256, "BlackQuartzOre");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		addEMC(InitItems.itemMisc, 13, "Canola");
		addEMC(InitItems.itemCoffeeBean, "CoffeBeans");
		addEMC(InitItems.itemFoods, 16, "Rice");
		addEMC(InitItems.itemMisc, 15, "BatWings");
		addEMC(InitItems.itemSolidifiedExperience, "SolidifiedXP");
		addEMC(Item.getItemFromBlock(InitBlocks.blockMisc), 3, "BlackQuartzOre");
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new LaserEMCMapper());
		mappers.add(new EmpowererEMCMapper());
	}
}