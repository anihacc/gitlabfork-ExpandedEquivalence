package com.zeitheron.expequiv.utils;

import java.util.List;

import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

public class EMCUtils
{
	public static int getEMC(Object obj)
	{
		if(obj instanceof Ingredient)
			return getEMC(((Ingredient) obj).getMatchingStacks());
		if(obj instanceof ItemStack)
			return ProjectEAPI.getEMCProxy().getValue((ItemStack) obj);
		if(obj instanceof Item)
			return ProjectEAPI.getEMCProxy().getValue((Item) obj);
		if(obj instanceof Block)
			return ProjectEAPI.getEMCProxy().getValue((Block) obj);
		if(obj instanceof List && !((List) obj).isEmpty())
		{
			int least = 0;
			int cemc;
			for(Object o : (List) obj)
				if((cemc = getEMC(o)) > 0 && (cemc < least || least == 0))
					least = cemc;
			return least;
		}
		if(obj instanceof ItemStack[])
		{
			int least = 0;
			int cemc;
			for(ItemStack stack : (ItemStack[]) obj)
				if((cemc = getEMC(stack)) > 0 && (cemc < least || least == 0))
					least = cemc;
			return least;
		}
		if(obj instanceof String)
			return getEMC(OreDictionary.getOres((String) obj));
		return 0;
	}
}