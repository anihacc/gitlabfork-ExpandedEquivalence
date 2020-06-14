package tk.zeitheron.expequiv.exp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.zeitheron.expequiv.ExpandedEquivalence;
import tk.zeitheron.expequiv.InfoEE;
import tk.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.api.proxy.ITransmutationProxy;
import moze_intel.projecte.impl.EMCProxyImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Expansion
{
	public static final Map<String, List<IExpansionFactory>> EXPANSIONS = new HashMap<>();
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
	 * Call {@link #addEMCCfg(long, String, String)} at this method to define new
	 * config entries for different items
	 */
	protected void addCfgEMC()
	{
	}
	
	public ConfigEMCValue getCfgEmc(String id)
	{
		return cfgEmc.get(id);
	}
	
	protected void addEMCCfg(long base, String id)
	{
		addEMCCfg(base, id, "$");
	}
	
	protected void addEMCCfg(long base, String id, String name)
	{
		if(values != null)
			values.add(new ConfigEMCValue(base, id, name != null ? (name.equals("$") ? splitName(id) : name) : id));
	}
	
	protected long getCfgEMC(long base, String id)
	{
		return getCfgEMC(base, id, splitName(id));
	}
	
	protected long getCfgEMC(long base, String id, String name)
	{
		return config.getCategory("EMC").getLongEntry(id, base, 0, Long.MAX_VALUE).setDescription("Base cost for " + name + ". Set to 0 to disable.").getValue();
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
	
	public static void registerExpansion(String modid, IExpansionFactory f)
	{
		EXPANSIONS.computeIfAbsent(modid, k -> new ArrayList<>())
				.add(f);
	}
	
	/**
	 * @deprecated backwards compat
	 */
	@Deprecated
	public static void registerExpansion(String modid, Class<? extends Expansion> cl)
	{
		registerExpansion(modid, new IExpansionFactory()
		{
			@Override
			public String getName()
			{
				return cl.getSimpleName();
			}
			
			@Override
			public Expansion create(String modid, Configuration config, Object[] args)
			{
				try
				{
					Constructor<? extends Expansion> exp = cl.getConstructor(String.class, Configuration.class, Object[].class);
					exp.setAccessible(true);
					return exp.newInstance(modid, config, args);
				} catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
				{
					e.printStackTrace();
				}
				return null;
			}
		});
	}
	
	public static List<Expansion> createExpansionList(File dir, Object... args)
	{
		List<Expansion> exps = new ArrayList<>();
		List<String> loadedMods = EXPANSIONS.keySet().stream().filter(Loader::isModLoaded).collect(Collectors.toList());
		for(String modid : loadedMods)
		{
			List<IExpansionFactory> classes = EXPANSIONS.get(modid);
			for(IExpansionFactory c : classes)
				try
				{
					ExpandedEquivalence.LOG.info("Creating expansion " + c.getName() + " for mod " + modid + " (" + Loader.instance().getIndexedModList().get(modid).getName() + ")");
					
					File subMod = new File(dir, modid);
					if(!subMod.isDirectory())
						subMod.mkdir();
					File cfgFile = new File(subMod, c.getConfigName() + ".cfg");
					Configuration cfg = new Configuration(cfgFile);
					Expansion ex = c.create(modid, cfg, args);
					
					if(ex == null)
					{
						ExpandedEquivalence.LOG.warn("Failed to load expansion from factory " + c.getName() + ", skipping.");
						continue;
					}
					
					boolean b = cfg.getCategory("Base").getBooleanEntry("Enabled", true).setDescription("Enable this part of ExpandedEquivalence").getValue();
					
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
	
	public void getConverters(List<IEMCConverter> mappers)
	{
		
	}
	
	protected Logger log;
	
	public Logger getLogger()
	{
		if(log == null) log = LogManager.getLogger(InfoEE.MOD_NAME + "/" + getClass().getSimpleName());
		return log;
	}
	
	public interface IExpansionFactory
	{
		default String getConfigName()
		{
			return "Unnamed";
		}
		
		default String getName()
		{
			return "Unnamed factory";
		}
		
		Expansion create(String modid, Configuration config, Object[] args);
	}
	
	public static class ConfigEMCValue
	{
		final long base;
		final String id, name;
		private long value;
		
		ConfigEMCValue(long base, String id, String name)
		{
			this.base = base;
			this.id = id;
			this.name = name;
			value = base;
		}
		
		public long getValue()
		{
			return value;
		}
	}
}