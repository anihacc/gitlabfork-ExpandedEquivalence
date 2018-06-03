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
public class AE2Expansion extends Expansion
{
	public AE2Expansion(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	public void preInit(Configuration c)
	{
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		// HELP ME FIND AE2'S ITEM INSTANCES CLASS! PLZZ!!
		Function<String, Item> find = reg -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", reg));
		
		Item material = find.apply("material");
		emc.registerCustomEMC(new ItemStack(material, 1, 1), 64);
		emc.registerCustomEMC(new ItemStack(material, 1, 6), 256);
		emc.registerCustomEMC(new ItemStack(material, 1, 10), 32);
		emc.registerCustomEMC(new ItemStack(material, 1, 11), 128);
		emc.registerCustomEMC(new ItemStack(material, 1, 12), 128);
		emc.registerCustomEMC(new ItemStack(material, 1, 47), 256_000);
		emc.registerCustomEMC(new ItemStack(find.apply("sky_stone_block")), 64);
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new InscriberEMCMapper());
		
		// Doesn't work for some reason, help! :c
		mappers.add(new GrinderEMCMapper());
	}
}