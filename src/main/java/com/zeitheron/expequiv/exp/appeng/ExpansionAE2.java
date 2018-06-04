package com.zeitheron.expequiv.exp.appeng;

import java.util.List;
import java.util.function.Function;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.api.proxy.ITransmutationProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
		// HELP ME FIND AE2'S ITEM INSTANCES CLASS! PLZZ!!
		Function<String, Item> find = reg -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", reg));
		
		Item material = find.apply("material");
		emc.registerCustomEMC(new ItemStack(material, 1, 1), 64);
		addEMC(material, 6, "MatterBall");
		emc.registerCustomEMC(new ItemStack(material, 1, 10), 32);
		emc.registerCustomEMC(new ItemStack(material, 1, 11), 128);
		emc.registerCustomEMC(new ItemStack(material, 1, 12), 128);
		addEMC(material, 47, "Singularity");
		addEMC(find.apply("sky_stone_block"), "SkyStone");
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new InscriberEMCMapper());
		
		// Doesn't work for some reason, help! :c
		mappers.add(new GrinderEMCMapper());
	}
}