package com.zeitheron.expequiv.exp.enderio;

import java.util.List;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.expequiv.exp.reborncore.RebornCoreEMCMapper;

import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ExpansionReg(modid = "enderio")
public class ExpansionEnderIO extends Expansion
{
	public ExpansionEnderIO(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(32, "GrainsOfInfinity");
		addEMCCfg(32, "ClippingsAndTrimmings");
		addEMCCfg(24, "TwigsAndPrunings");
		addEMCCfg(64 * 1024, "EndermanHead");
		addEMCCfg(20 * 1024, "EnticingCrystal");
		addEMCCfg(19512 + 2048, "EnderCrystal");
		addEMCCfg(19512 + 4096, "PrecientCrystal");
	}
	
	@Override
	public void registerEMC(IEMCProxy emcProxy)
	{
		Item item_material = ForgeRegistries.ITEMS.getValue(new ResourceLocation("enderio", "item_material"));
		
		addEMC(item_material, 16, "EnderCrystal");
		addEMC(item_material, 17, "EnticingCrystal");
		addEMC(item_material, 19, "PrecientCrystal");
		addEMC(item_material, 20, "GrainsOfInfinity");
		addEMC(item_material, 46, "ClippingsAndTrimmings");
		addEMC(item_material, 47, "TwigsAndPrunings");
		
		addEMC(ForgeRegistries.ITEMS.getValue(new ResourceLocation("enderio", "block_enderman_skull")), "EndermanHead");
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new AlloySmelterEMCMapper());
		mappers.add(new SAGEMCMapper());
		mappers.add(new SliceNSpliceEMCMapper());
		mappers.add(new VatEMCMapper());
		
		mappers.add(new RebornCoreEMCMapper());
	}
}