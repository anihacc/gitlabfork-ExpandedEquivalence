package tk.zeitheron.expequiv;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.impl.EMCProxyImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.zeitheron.expequiv.api.IEMC;
import tk.zeitheron.expequiv.api.IEMCConverter;
import tk.zeitheron.expequiv.api.js.JSExpansion;
import tk.zeitheron.expequiv.api.js.JSSource;
import tk.zeitheron.expequiv.exp.Expansion;
import tk.zeitheron.expequiv.exp.ExpansionReg;

import javax.script.ScriptException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod(modid = InfoEE.MOD_ID, name = InfoEE.MOD_NAME, version = InfoEE.MOD_VERSION, certificateFingerprint = "9f5e2a811a8332a842b34f6967b7db0ac4f24856", dependencies = "required-after:hammercore;required-after:projecte", updateJSON = "https://dccg.herokuapp.com/api/fmluc/295222")
public class ExpandedEquivalence
{
	public List<Expansion> expansions;
	@Instance
	public static ExpandedEquivalence instance;
	public static final Logger LOG = LogManager.getLogger(InfoEE.MOD_ID);
	
	@EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		LOG.warn("*****************************");
		LOG.warn("WARNING: Somebody has been tampering with ExpandedEquivalence jar!");
		LOG.warn("It is highly recommended that you redownload mod from https://www.curseforge.com/projects/295222 !");
		LOG.warn("*****************************");
		HammerCore.invalidCertificates.put(InfoEE.MOD_ID, "https://www.curseforge.com/projects/295222");
	}
	
	@EventHandler
	public void construct(FMLConstructionEvent e)
	{
		// Register expansions either under FMLConstructionEvent or annotate
		// with @ExpansionReg
		
		ProgressManager.ProgressBar bar = ProgressManager.push("Loading JS", Loader.instance().getIndexedModList().size());
		for(String mod : Loader.instance().getIndexedModList().keySet().stream().sorted().collect(Collectors.toList()))
		{
			String name;
			bar.step(name = Loader.instance().getIndexedModList().get(mod).getName());
			JSSource src = JSSource.fromLocalResource("tk/zeitheron/expequiv/exp/js/" + mod + ".js");
			if(src.exists())
			{
				Expansion.IExpansionFactory factory = new Expansion.IExpansionFactory()
				{
					@Override
					public String getName()
					{
						return "internal";
					}
					
					@Override
					public Expansion create(String modid, Configuration config, Object[] args)
					{
						try
						{
							JSExpansion jse = new JSExpansion(modid, config, args, src);
							jse.contruct();
							return jse;
						} catch(ScriptException scriptException)
						{
							scriptException.printStackTrace();
						}
						return null;
					}
				};
				
				Expansion.registerExpansion(mod, factory);
				LOG.info("Created JavaScript expansion for mod " + name);
			}
		}
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		List<Class<? extends Expansion>> annotated = new ArrayList<>();
		Set<ASMData> asmDatas = e.getAsmData().getAll(ExpansionReg.class.getCanonicalName());
		asmDatas.forEach(asm ->
		{
			if(Loader.isModLoaded(asm.getAnnotationInfo().get("modid").toString()))
				try
				{
					Class<?> cl = Class.forName(asm.getClassName());
					ExpansionReg reg = cl.getAnnotation(ExpansionReg.class);
					if(reg != null && Expansion.class.isAssignableFrom(cl))
					{
						Class<? extends Expansion> acl = cl.asSubclass(Expansion.class);
						annotated.add(acl);
						Expansion.registerExpansion(reg.modid(), acl);
					}
				} catch(Throwable er)
				{
					if(er instanceof NoClassDefFoundError || er instanceof ClassNotFoundException)
						return;
					er.printStackTrace();
				}
		});
		
		LOG.info("Registered " + annotated.size() + " new possible expansions based off @ExpansionReg:");
		for(Class<? extends Expansion> c : annotated)
			LOG.info("  " + c.getName());
		
		File cfgDir = e.getSuggestedConfigurationFile();
		String path = cfgDir.getAbsolutePath();
		path = path.substring(0, path.lastIndexOf(File.separator));
		
		final File cfgsDir = new File(path, "Expanded Equivalence");
		if(!cfgsDir.isDirectory())
			cfgsDir.mkdirs();
		
		expansions = Expansion.createExpansionList(cfgsDir, InfoEE.MOD_ID, InfoEE.MOD_NAME, InfoEE.MOD_VERSION);
		
		LOG.info("Created " + expansions.size() + " expansions.");
		
		expansions.forEach(ex ->
		{
			ex.preInit$(ex.getConfig());
			if(ex.getConfig().hasChanged())
				ex.getConfig().save();
		});
		
		expansions.forEach(MinecraftForge.EVENT_BUS::register);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		expansions.forEach(ex -> ex.init());
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		expansions.forEach(ex -> ex.postInit(EMCProxyImpl.instance, ProjectEAPI.getTransmutationProxy()));
	}
	
	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent e)
	{
		instance.expansions.forEach(ex ->
		{
			List<IEMCConverter> mappers = new ArrayList<>();
			ex.getConverters(mappers);
			for(IEMCConverter m : mappers)
			{
				try
				{
					m.register(IEMC.PE_WRAPPER, ex.getConfig());
					LOG.info("Collected EMC convertions from " + m.getName());
				} catch(Throwable err)
				{
					LOG.fatal("Exception while gathering EMC convertions from converter " + m.getName() + ". PLEASE REPORT THIS! EMC VALUES MIGHT BE INCONSISTENT!", err);
				}
			}
		});
	}
}