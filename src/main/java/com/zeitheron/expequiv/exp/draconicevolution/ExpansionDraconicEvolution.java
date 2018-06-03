package com.zeitheron.expequiv.exp.draconicevolution;

import java.util.List;
import java.util.Map;

import com.brandon3055.draconicevolution.DEFeatures;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.api.proxy.ITransmutationProxy;
import moze_intel.projecte.emc.EMCMapper;
import moze_intel.projecte.emc.SimpleGraphMapper;
import moze_intel.projecte.emc.arithmetics.HiddenFractionArithmetic;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.collector.IntToFractionCollector;
import moze_intel.projecte.emc.collector.WildcardSetValueFixCollector;
import moze_intel.projecte.emc.generators.FractionToIntGenerator;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

@ExpansionReg(modid = "draconicevolution")
public class ExpansionDraconicEvolution extends Expansion
{
	public ExpansionDraconicEvolution(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	public int draconiumDustCost = 8_192;
	public int dragonHeartCost = 139_264;
	public int chaosShardCost = 1_024_000;
	
	@Override
	public void preInit(Configuration c)
	{
		draconiumDustCost = c.getInt("DraconiumDust", "EMC", draconiumDustCost, 0, Integer.MAX_VALUE, "Base cost for Draconium Dust");
		dragonHeartCost = c.getInt("DragonHeart", "EMC", dragonHeartCost, 0, Integer.MAX_VALUE, "Base cost for Dragon Heart");
		chaosShardCost = c.getInt("ChaosShard", "EMC", chaosShardCost, 0, Integer.MAX_VALUE, "Base cost for Chaos Shard");
	}
	
	@Override
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		mappers.add(new FusionEMCMapper());
	}
	
	@Override
	public void registerEMC(IEMCProxy emcp)
	{
		emcp.registerCustomEMC(new ItemStack(DEFeatures.draconiumDust), draconiumDustCost);
		emcp.registerCustomEMC(new ItemStack(DEFeatures.dragonHeart), dragonHeartCost);
		emcp.registerCustomEMC(new ItemStack(DEFeatures.chaosShard), chaosShardCost);
		
		// for(IFusionRecipe rec : FusionRecipeAPI.getRecipes())
		// if(!(rec instanceof FusionUpgradeRecipe) && !(rec instanceof
		// ToolUpgradeRecipe))
		// {
		// int emc = EMCUtils.getEMC(rec.getRecipeCatalyst());
		// int cemc = 0;
		// for(Object o : rec.getRecipeIngredients())
		// if((cemc = EMCUtils.getEMC(o)) > 0)
		// emc += cemc;
		// else
		// continue recipe;
		// ItemStack output = rec.getRecipeOutput(ItemStack.EMPTY);
		// if(!InterItemStack.isStackNull(output) && emc > 0 &&
		// emcp.getValue(output) < 1)
		// {
		// mappingCollector.addConversion(arg0, arg1, arg2);
		// emcp.registerCustomEMC(output, emc);
		// mod = true;
		// }
		// }
	}
}