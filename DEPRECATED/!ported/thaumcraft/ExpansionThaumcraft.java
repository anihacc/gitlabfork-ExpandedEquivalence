package com.zeitheron.expequiv.exp.thaumcraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.proxy.IConversionProxy;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.api.proxy.ITransmutationProxy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;

@ExpansionReg(modid = "thaumcraft")
public class ExpansionThaumcraft extends Expansion
{
	public ExpansionThaumcraft(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	private final Map<Aspect, Integer> defAspectCosts = new HashMap<>();
	public int instabilityMod;
	
	@Override
	protected void preInit(Configuration configs)
	{
		for(Aspect a : Aspect.aspects.values())
			defAspectCosts.put(a, configs.getCategory("AspectEMC").getIntEntry(a.getName(), 64, 0, Integer.MAX_VALUE).setDescription("Default cost of " + a.getName() + ".").getValue());
		instabilityMod = configs.getCategory("Additional").getIntEntry("Instability", 2048, 0, Integer.MAX_VALUE).setDescription("Additional EMC penalty per instability point.").getValue();
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(128, "Shimmerleaf");
		addEMCCfg(256, "Quicksilver");
		addEMCCfg(768, "Cinderpearl");
		addEMCCfg(512, "Amber");
		addEMCCfg(256, "ZombieBrains");
		addEMCCfg(131_072, "PrimordialPearl");
		addEMCCfg(4096, "RareEarths");
		addEMCCfg(128, "SalisMundus");
		addEMCCfg(32, "VisCrystal");
		addEMCCfg(16, "AncientStone");
		addEMCCfg(32, "EldritchStone");
		addEMCCfg(256 + 5 * 64, "AlchemicalBrassIngot");
	}
	
	@Override
	public void postInit(IEMCProxy emcProxy, ITransmutationProxy transmuteProxy)
	{
		super.postInit(emcProxy, transmuteProxy);
		
		IConversionProxy conv = ProjectEAPI.getConversionProxy();
		
		conv.addConversion(1, BlocksTC.researchTable, ImmutableMap.of(BlocksTC.tableWood, 1));
		conv.addConversion(1, ItemsTC.thaumonomicon, ImmutableMap.of(Blocks.BOOKSHELF, 1, ItemsTC.salisMundus, 1));
		conv.addConversion(1, BlocksTC.crucible, ImmutableMap.of(Items.CAULDRON, 1, ItemsTC.salisMundus, 1));
		conv.addConversion(1, BlocksTC.arcaneWorkbench, ImmutableMap.of(BlocksTC.tableWood, 1, ItemsTC.salisMundus, 1));
		conv.addConversion(1, BlocksTC.condenserlatticeDirty, ImmutableMap.of(BlocksTC.condenserlattice, 1));
	}
	
	public long getVisCrystalCost()
	{
		return getCfgEmc("VisCrystal").getValue();
	}
	
	public long getPrimordialPearlCost()
	{
		return getCfgEmc("PrimordialPearl").getValue();
	}
	
	public int getAspectCost(Aspect a)
	{
		Integer i = defAspectCosts.getOrDefault(a, 64);
		return i == null ? 64 : i.intValue();
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		// for(Aspect asp : Aspect.aspects.values())
		// emc.registerCustomEMC(ThaumcraftApiHelper.makeCrystal(asp),
		// getVisCrystalCost() + getAspectCost(asp));
		addEMC(ItemsTC.nuggets, 10, "RareEarths");
		addEMC(ItemsTC.ingots, 2, "AlchemicalBrassIngot");
		addEMC(ItemsTC.salisMundus, "SalisMundus");
		addEMC(ItemsTC.quicksilver, "Quicksilver");
		addEMC(ItemsTC.amber, "Amber");
		addEMC(ItemsTC.brain, "ZombieBrains");
		addEMC(ItemsTC.primordialPearl, "PrimordialPearl");
		addEMC(Item.getItemFromBlock(BlocksTC.cinderpearl), "Cinderpearl");
		addEMC(Item.getItemFromBlock(BlocksTC.shimmerleaf), "Shimmerleaf");
		addEMC(Item.getItemFromBlock(BlocksTC.stoneAncient), "AncientStone");
		addEMC(Item.getItemFromBlock(BlocksTC.stoneEldritchTile), "EldritchStone");
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new MagicalEMCConverter(this));
	}
}