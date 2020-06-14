package tk.zeitheron.expequiv.api.js;

import com.zeitheron.hammercore.cfg.file1132.Configuration;
import moze_intel.projecte.impl.EMCProxyImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JSConfigs
{
	public final JSExpansion js;
	public final Configuration config;
	
	public JSConfigs(JSExpansion js, Configuration config)
	{
		this.js = js;
		this.config = config;
	}
	
	public void registerEMC(Item item, int metadata, long emc)
	{
		EMCProxyImpl.instance.registerCustomEMC(new ItemStack(item, 1, metadata), emc);
	}
	
	public void registerEMC(Item item, long emc)
	{
		EMCProxyImpl.instance.registerCustomEMC(new ItemStack(item), emc);
	}
	
	public void addEMC(Item item, String configKey, long emc)
	{
		addEMC(item, 0, configKey, emc);
	}
	
	public void addEMC(Item item, int metadata, String configKey, long emc)
	{
		if(item == null) return;
		emc = js.getCfgEMC(emc, configKey);
		if(emc > 0)
			EMCProxyImpl.instance.registerCustomEMC(new ItemStack(item, 1, metadata), emc);
	}
	
	public void addEMC(Item item, String configKey, String displayName, long emc)
	{
		addEMC(item, 0, configKey, displayName, emc);
	}
	
	public void addEMC(Item item, int metadata, String configKey, String displayName, long emc)
	{
		if(item == null) return;
		emc = js.getCfgEMC(emc, configKey, displayName);
		if(emc > 0)
			EMCProxyImpl.instance.registerCustomEMC(new ItemStack(item, 1, metadata), emc);
	}
	
	public void addEMC(ItemStack stack, String configKey, long emc)
	{
		if(stack.isEmpty()) return;
		emc = js.getCfgEMC(emc, configKey);
		if(emc > 0)
			EMCProxyImpl.instance.registerCustomEMC(stack, emc);
	}
	
	public void addEMC(ItemStack stack, String configKey, String displayName, long emc)
	{
		if(stack.isEmpty()) return;
		emc = js.getCfgEMC(emc, configKey, displayName);
		if(emc > 0)
			EMCProxyImpl.instance.registerCustomEMC(stack, emc);
	}
	
	public long getEMC(long base, String configKey)
	{
		return js.getCfgEMC(base, configKey);
	}
}