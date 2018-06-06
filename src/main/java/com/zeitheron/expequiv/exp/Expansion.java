package com.zeitheron.expequiv.exp;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.zeitheron.expequiv.ExpandedEquivalence;

import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.api.proxy.ITransmutationProxy;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import moze_intel.projecte.impl.EMCProxyImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public abstract class Expansion
{
	public static final Map<String, List<Class<? extends Expansion>>> EXPANSIONS = new HashMap<>();
	
	public final String modid;
	public final Object[] args;
	private final Configuration config;
	
	public Expansion(String modid, Configuration config, Object[] args)
	{
		this.config = config;
		this.args = args;
		this.modid = modid;
	}
	
	protected final Map<String, ConfigEMCValue> cfgEmc = new HashMap<>();
	private List<ConfigEMCValue> values;
	
	/**
	 * Call {@link #addEMCCfg(int, String, String)} at this method to define new
	 * config entries for different items
	 */
	protected void addCfgEMC()
	{
	}
	
	public ConfigEMCValue getCfgEmc(String id)
	{
		return cfgEmc.get(id);
	}
	
	protected void addEMCCfg(int base, String id)
	{
		addEMCCfg(base, id, "$");
	}
	
	protected void addEMCCfg(int base, String id, String name)
	{
		if(values != null)
			values.add(new ConfigEMCValue(base, id, name != null ? (name.equals("$") ? splitName(id) : name) : id));
	}
	
	protected int getCfgEMC(int base, String id, String name)
	{
		return config.getInt(id, "EMC", base, 0, Integer.MAX_VALUE, "Base cost for " + name + ". Set to 0 to disable.");
	}
	
	private static String splitName(String str)
	{
		StringBuilder sb = new StringBuilder();
		for(char c : str.toCharArray())
		{
			if(Character.toUpperCase(c) == c && Character.toLowerCase(c) != c && sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ')
				sb.append(' ');
			sb.append(c);
		}
		return sb.toString();
	}
	
	public Configuration getConfig()
	{
		return config;
	}
	
	public static void registerExpansion(String modid, Class<? extends Expansion> cl)
	{
		List<Class<? extends Expansion>> exps = EXPANSIONS.get(modid);
		if(exps == null || !(exps instanceof ArrayList))
			EXPANSIONS.put(modid, exps = new ArrayList<>());
		if(!exps.contains(cl))
			exps.add(cl);
	}
	
	public static List<Expansion> createExpansionList(File dir, Object... args)
	{
		List<Expansion> exps = new ArrayList<>();
		List<String> loadedMods = EXPANSIONS.keySet().stream().filter(Loader::isModLoaded).collect(Collectors.toList());
		for(String modid : loadedMods)
		{
			List<Class<? extends Expansion>> classes = EXPANSIONS.get(modid);
			for(Class<? extends Expansion> c : classes)
				try
				{
					ExpandedEquivalence.LOG.info("Creating expansion " + c.getName() + " for mod " + modid + " (" + Loader.instance().getIndexedModList().get(modid).getName() + ")");
					
					Constructor<? extends Expansion> exp = c.getConstructor(String.class, Configuration.class, Object[].class);
					exp.setAccessible(true);
					
					File subMod = new File(dir, modid);
					if(!subMod.isDirectory())
						subMod.mkdir();
					File cfgFile = new File(subMod, c.getSimpleName() + ".cfg");
					Configuration cfg = new Configuration(cfgFile);
					Expansion ex = exp.newInstance(modid, cfg, args);
					boolean b = ex.getConfig().getBoolean("Enabled", "Base", true, "Enable this part of ExpandedEquivalence");
					if(cfg.hasChanged())
						cfg.save();
					if(b)
						exps.add(ex);
				} catch(Throwable err)
				{
					ExpandedEquivalence.LOG.error("Failed to create new expansion instance for class " + c.getName(), err);
				}
		}
		return exps;
	}
	
	public final void preInit$(Configuration configs)
	{
		List<ConfigEMCValue> emcs = new ArrayList<>();
		values = emcs;
		addCfgEMC();
		values = null;
		emcs.forEach(c -> cfgEmc.put(c.id, c));
		cfgEmc.values().forEach(c -> c.value = getCfgEMC(c.base, c.id, c.name));
		
		preInit(configs);
	}
	
	protected void preInit(Configuration configs)
	{
		
	}
	
	public void init()
	{
		
	}
	
	public void postInit(IEMCProxy emcProxy, ITransmutationProxy transmutateProxy)
	{
		registerEMC(emcProxy);
	}
	
	public void registerEMC(IEMCProxy emcProxy)
	{
		
	}
	
	protected boolean addEMC(Item item, String id)
	{
		return addEMC(item, 0, id);
	}
	
	protected boolean addEMC(Item item, int meta, String id)
	{
		boolean add;
		if(add = cfgEmc.containsKey(id))
			EMCProxyImpl.instance.registerCustomEMC(new ItemStack(item, 1, meta), cfgEmc.get(id).getValue());
		return add;
	}
	
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		
	}
	
	public static class ConfigEMCValue
	{
		final int base;
		final String id, name;
		
		private int value;
		
		ConfigEMCValue(int base, String id, String name)
		{
			this.base = base;
			this.id = id;
			this.name = name;
			value = base;
		}
		
		public int getValue()
		{
			return value;
		}
	}
}