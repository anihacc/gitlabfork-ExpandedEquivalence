package com.zeitheron.expequiv.exp.enderio;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.FakeItem;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import crazypants.enderio.base.recipe.IManyToOneRecipe;
import crazypants.enderio.base.recipe.IRecipeInput;
import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
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
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(new AlloySmelterEMCConverter());
		mappers.add(new SAGEMCConverter());
		mappers.add(new SliceNSpliceEMCConverter());
		mappers.add(new VatEMCConverter());
	}
	
	static List<CountedIngredient> inputsToIngs(IEMC emc, IRecipeInput... inputs)
	{
		List<CountedIngredient> ci = new ArrayList<>();
		for(IRecipeInput in : inputs)
		{
			if(in.getFluidInput() != null)
				ci.add(CountedIngredient.create(in.getFluidInput()));
			if(in.getEquivelentInputs() != null && in.getEquivelentInputs().length > 0)
				ci.add(FakeItem.create(emc, 1, Ingredient.fromStacks(in.getEquivelentInputs())));
			else if(!in.getInput().isEmpty())
				ci.add(CountedIngredient.create(in.getInput()));
		}
		return ci;
	}
	
	static void handleMtO(IEMC emc, IManyToOneRecipe r)
	{
		emc.map(r.getOutput(), inputsToIngs(emc, r.getInputs()));
	}
}