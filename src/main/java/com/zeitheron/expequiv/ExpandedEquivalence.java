package com.zeitheron.expequiv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zeitheron.expequiv.api.IEMC;
import com.zeitheron.expequiv.api.IEMCConverter;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;
import com.zeitheron.hammercore.HammerCore;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.impl.EMCProxyImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = InfoEE.MOD_ID, name = InfoEE.MOD_NAME, version = InfoEE.MOD_VERSION, certificateFingerprint = "4d7b29cd19124e986da685107d16ce4b49bc0a97", dependencies = "required-after:hammercore;required-after:projecte")
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
		LOG.warn("It is highly recommended that you redownload mod from https://minecraft.curseforge.com/projects/295222 !");
		LOG.warn("*****************************");
		HammerCore.invalidCertificates.put(InfoEE.MOD_ID, "https://minecraft.curseforge.com/projects/295222");
	}
	
	@EventHandler
	public void construct(FMLConstructionEvent e)
	{
		// Register expansions either under FMLConstructionEvent or annotate
		// with @ExpansionReg
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		List<Class<? extends Expansion>> annotated = new ArrayList<>();
		Set<ASMData> asmDatas = e.getAsmData().getAll(ExpansionReg.class.getCanonicalName());
		asmDatas.forEach(asm ->
		{
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
		
		instance.expansions.forEach(ex ->
		{
			List<IEMCConverter> mappers = new ArrayList<>();
			ex.getConverters(mappers);
			for(IEMCConverter m : mappers)
			{
				try
				{
					m.register(IEMC.PE_WRAPPER, ex.getConfig());
					LOG.info("Collected EMC convertions from " + m.getClass().getName());
				} catch(Throwable err)
				{
					LOG.fatal("Exception while gathering EMC convertions from converter " + m.getClass().getName() + ". PLEASE REPORT THIS! EMC VALUES MIGHT BE INCONSISTENT!", err);
				}
			}
		});
	}
}