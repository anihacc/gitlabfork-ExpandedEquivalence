package com.zeitheron.expequiv.exp.mysticalagriculture.mysticalagradditions;

import java.util.List;

import com.blakebr0.mysticalagradditions.items.ModItems;
import com.blakebr0.mysticalagradditions.lib.CropType;
import com.zeitheron.expequiv.api.CountedIngredient;
import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.cfg.file1132.Configuration;

import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ExpansionReg(modid = "mysticalagradditions")
public class ExpansionMysticalAgradditions extends Expansion implements IEMCConverter
{
	public ExpansionMysticalAgradditions(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	protected void addCfgEMC()
	{
		addEMCCfg(2048, "WitheringSoul");
		addEMCCfg(6144, "DragonScale");
	}
	
	@Override
	public void registerEMC(IEMCProxy emc)
	{
		Item stuff = ModItems.itemStuff;
		
		addEMC(stuff, 1, "WitheringSoul");
		addEMC(stuff, 3, "DragonScale");
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		mappers.add(this);
	}
	
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		Item stuff = ModItems.itemStuff;
		
		emc.map(new ItemStack(stuff, 3, 0), CountedIngredient.create(Items.NETHER_STAR, 1));
		emc.map(new ItemStack(stuff, 3, 2), CountedIngredient.create(Blocks.DRAGON_EGG, 1));
		
		if(CropType.Type.NETHER_STAR.isEnabled())
			emc.map(new ItemStack(CropType.Type.NETHER_STAR.getCrop(), 9), CountedIngredient.create(new ItemStack(stuff, 1, 0)));
		
		if(CropType.Type.DRAGON_EGG.isEnabled())
			emc.map(new ItemStack(CropType.Type.DRAGON_EGG.getCrop(), 9), CountedIngredient.create(new ItemStack(stuff, 1, 2)));
		
		if(CropType.Type.AWAKENED_DRACONIUM.isEnabled())
		{
			Item deNugget = ForgeRegistries.ITEMS.getValue(new ResourceLocation("draconicevolution", "nugget"));
			emc.map(new ItemStack(CropType.Type.AWAKENED_DRACONIUM.getCrop(), 9), CountedIngredient.create(new ItemStack(deNugget, 3, 1)));
		}
	}
}