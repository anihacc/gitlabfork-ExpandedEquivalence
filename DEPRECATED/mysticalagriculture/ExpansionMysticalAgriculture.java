package com.zeitheron.expequiv.exp.mysticalagriculture;

import java.util.List;

import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ExpansionReg(modid = "mysticalagriculture")
public class ExpansionMysticalAgriculture extends Expansion
{
	public ExpansionMysticalAgriculture(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(8, "InferiumEssence");
		addEMCCfg(8 * 4, "PrudentiumEssence");
		addEMCCfg(8 * 16, "IntermediumEssence");
		addEMCCfg(8 * 64, "SuperiumEssence");
		addEMCCfg(8 * 256, "SupremiumEssence");
		addEMCCfg(64, "ProsperityShard");
		addEMCCfg(96, "FertilizedEssence");
		addEMCCfg(1024, "MobChunk");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		Item itemCrafting = ForgeRegistries.ITEMS.getValue(new ResourceLocation("mysticalagriculture", "crafting"));
		
		addEMC(itemCrafting, 0, "InferiumEssence");
		addEMC(itemCrafting, 1, "PrudentiumEssence");
		addEMC(itemCrafting, 2, "IntermediumEssence");
		addEMC(itemCrafting, 3, "SuperiumEssence");
		addEMC(itemCrafting, 4, "SupremiumEssence");
		addEMC(itemCrafting, 5, "ProsperityShard");
		
		addEMC(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mysticalagriculture", "fertilized_essence")), "FertilizedEssence");
		addEMC(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mysticalagriculture", "chunk")), "MobChunk");
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new SeedReprocessorEMCConverter());
	}
}