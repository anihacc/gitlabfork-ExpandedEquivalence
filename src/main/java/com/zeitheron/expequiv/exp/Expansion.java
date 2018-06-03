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
					Constructor<? extends Expansion> exp = c.getConstructor(String.class, Configuration.class, Object[].class);
					exp.setAccessible(true);
					
					File subMod = new File(dir, modid);
					if(!subMod.isDirectory())
						subMod.mkdir();
					File cfgFile = new File(subMod, c.getSimpleName() + ".cfg");
					Configuration cfg = new Configuration(cfgFile);
					Expansion ex = exp.newInstance(modid, cfg, args);
					boolean b = ex.getConfig().getBoolean("Enabled", "", true, "Enable this part of ExpandedEquivalence");
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
	
	public void preInit(@Nullable Configuration configs)
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
	
	public void getMappers(List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers)
	{
		
	}
}