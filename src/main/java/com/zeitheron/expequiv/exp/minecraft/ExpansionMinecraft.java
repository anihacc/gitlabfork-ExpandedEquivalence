package com.zeitheron.expequiv.exp.minecraft;

import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

@ExpansionReg(modid = "minecraft")
public class ExpansionMinecraft extends Expansion
{
	public ExpansionMinecraft(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	public int dragonBreathCost = 1 + 1_024;
	public int expBottleCost = 864;
	public int elytraCost = 65_536;
	public int totemCost = 1024 * 50;
	
	@Override
	public void preInit(Configuration c)
	{
		dragonBreathCost = c.getInt("DragonBreath", "", dragonBreathCost, 0, Integer.MAX_VALUE, "Base cost for Dragon's Breath");
		expBottleCost = c.getInt("XPBottle", "", expBottleCost, 0, Integer.MAX_VALUE, "Base cost for Bottle o' Enchanting");
		elytraCost = c.getInt("Elytra", "", elytraCost, 0, Integer.MAX_VALUE, "Base cost for Elytra");
		totemCost = c.getInt("TotemOfUndying", "", totemCost, 0, Integer.MAX_VALUE, "Base cost for Totem of Undying");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		emc.registerCustomEMC(new ItemStack(Items.DRAGON_BREATH), dragonBreathCost);
		emc.registerCustomEMC(new ItemStack(Items.EXPERIENCE_BOTTLE), expBottleCost);
		emc.registerCustomEMC(new ItemStack(Items.ELYTRA), elytraCost);
		emc.registerCustomEMC(new ItemStack(Items.TOTEM_OF_UNDYING), totemCost);
	}
}