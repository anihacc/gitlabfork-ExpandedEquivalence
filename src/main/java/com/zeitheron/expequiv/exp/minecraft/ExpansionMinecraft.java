package com.zeitheron.expequiv.exp.minecraft;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

@ExpansionReg(modid = "minecraft")
public class ExpansionMinecraft extends Expansion
{
	public ExpansionMinecraft(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(1025, "DragonBreath", "Dragon's Breath");
		addEMCCfg(864, "XPBottle", "Bottle o' Enchanting");
		addEMCCfg(65536, "Elytra");
		addEMCCfg(1024 * 50, "TotemOfUndying");
		addEMCCfg(1024 * 48, "WitherSkeletonSkull");
		addEMCCfg(1024 * 192, "DragonHead");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		addEMC(Items.DRAGON_BREATH, "DragonBreath");
		addEMC(Items.EXPERIENCE_BOTTLE, "XPBottle");
		addEMC(Items.ELYTRA, "Elytra");
		addEMC(Items.TOTEM_OF_UNDYING, "TotemOfUndying");
		addEMC(Item.getItemFromBlock(Blocks.SKULL), 1, "WitherSkeletonSkull");
		addEMC(Item.getItemFromBlock(Blocks.SKULL), 5, "DragonHead");
	}
}