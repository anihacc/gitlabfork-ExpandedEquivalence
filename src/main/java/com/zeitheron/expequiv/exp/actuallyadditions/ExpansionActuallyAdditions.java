package com.zeitheron.expequiv.exp.actuallyadditions;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import de.ellpeck.actuallyadditions.mod.blocks.InitBlocks;
import de.ellpeck.actuallyadditions.mod.items.InitItems;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

@ExpansionReg(modid = "actuallyadditions")
public class ExpansionActuallyAdditions extends Expansion
{
	public ExpansionActuallyAdditions(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	public int canolaCost = 64;
	public int coffeCost = 64;
	public int riceCost = 64;
	public int batWingsCost = 480;
	public int solidXPCost = 863;
	public int blackQuartzOreCost = 256;
	
	@Override
	public void preInit(Configuration c)
	{
		
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		emc.registerCustomEMC(new ItemStack(InitBlocks.blockMisc, 1, 3), blackQuartzOreCost);
		emc.registerCustomEMC(new ItemStack(InitItems.itemFoods, 1, 16), riceCost);
		emc.registerCustomEMC(new ItemStack(InitItems.itemMisc, 1, 15), batWingsCost);
		emc.registerCustomEMC(new ItemStack(InitItems.itemMisc, 1, 13), canolaCost);
		emc.registerCustomEMC(new ItemStack(InitItems.itemCoffeeBean), coffeCost);
		emc.registerCustomEMC(new ItemStack(InitItems.itemSolidifiedExperience), solidXPCost);
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new LaserEMCMapper());
		mappers.add(new EmpowererEMCMapper());
	}
}