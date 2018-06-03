package com.zeitheron.expequiv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pengu.hammercore.HammerCore;
import com.zeitheron.expequiv.exp.Expansion;
import com.zeitheron.expequiv.exp.ExpansionReg;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.event.EMCRemapEvent;
import moze_intel.projecte.emc.EMCMapper;
import moze_intel.projecte.emc.SimpleGraphMapper;
import moze_intel.projecte.emc.SimpleStack;
import moze_intel.projecte.emc.arithmetics.HiddenFractionArithmetic;
import moze_intel.projecte.emc.collector.DumpToFileCollector;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.collector.IntToFractionCollector;
import moze_intel.projecte.emc.collector.WildcardSetValueFixCollector;
import moze_intel.projecte.emc.generators.FractionToIntGenerator;
import moze_intel.projecte.emc.json.NSSItem;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import moze_intel.projecte.emc.mappers.SmeltingMapper;
import moze_intel.projecte.emc.pregenerated.PregeneratedEMC;
import moze_intel.projecte.impl.EMCProxyImpl;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

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
		SmeltingMapper.class.getName();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
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
					Expansion.registerExpansion(reg.modid(), acl);
				}
			} catch(Throwable er)
			{
				er.printStackTrace();
			}
		});
		
		File cfgDir = e.getSuggestedConfigurationFile();
		String path = cfgDir.getAbsolutePath();
		path = path.substring(0, path.length() - 4);
		
		final File cfgsDir = new File(path);
		if(!cfgsDir.isDirectory())
			cfgsDir.mkdirs();
		
		expansions = Expansion.createExpansionList(InfoEE.MOD_ID, InfoEE.MOD_NAME, InfoEE.MOD_VERSION);
		expansions.forEach(MinecraftForge.EVENT_BUS::register);
		expansions.forEach(ex ->
		{
			File subMod = new File(cfgsDir, ex.modid);
			if(!subMod.isDirectory())
				subMod.mkdir();
			File cfgFile = new File(subMod, ex.getClass().getSimpleName() + ".cfg");
			Configuration cfg = new Configuration(cfgFile);
			ex.setConfig(cfg);
			ex.preInit(cfg);
			if(cfg.hasChanged())
				cfg.save();
		});
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
	
	@SubscribeEvent
	public void remapEMC(EMCRemapEvent evt)
	{
		/*
		Map<NormalizedSimpleStack, Integer> graphMapperValues;
		
		Map<IEMCMapper<NormalizedSimpleStack, Integer>, Configuration> cfgs = new HashMap<>();
		List<IEMCMapper<NormalizedSimpleStack, Integer>> emcMappers = new ArrayList<>();
		
		expansions.forEach(ex -> 
		{
			List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers = new ArrayList<>();
			ex.getMappers(mappers);
			for(IEMCMapper<NormalizedSimpleStack, Integer> m : mappers)
				cfgs.put(m, ex.getConfig());
			emcMappers.addAll(mappers);
		});
		
		SimpleGraphMapper mapper = new SimpleGraphMapper(new HiddenFractionArithmetic());
		FractionToIntGenerator<NormalizedSimpleStack> valueGenerator = new FractionToIntGenerator(mapper);
		IMappingCollector<NormalizedSimpleStack, Integer> mappingCollector = new WildcardSetValueFixCollector(new IntToFractionCollector(mapper));
		{
			LOG.info("Starting to collect Mappings in " + emcMappers.size() + " Mappers...");
			for(IEMCMapper emcMapper : emcMappers)
			{
				try
				{
					DumpToFileCollector.currentGroupName = emcMapper.getName();
					emcMapper.addMappings(mappingCollector, cfgs.get(emcMapper));
				} catch(Exception e)
				{
					LOG.fatal("Exception during Mapping Collection from Mapper {}. PLEASE REPORT THIS! EMC VALUES MIGHT BE INCONSISTENT!", (Object) emcMapper.getClass().getName());
					e.printStackTrace();
				}
			}
			DumpToFileCollector.currentGroupName = "NSSHelper";
			NormalizedSimpleStack.addMappings(mappingCollector);
			LOG.info("Mapping Collection finished");
			mappingCollector.finishCollection();
			LOG.info("Starting to generate Values:");
			graphMapperValues = valueGenerator.generateValues();
			LOG.info("Generated Values... Unfiltered " + graphMapperValues.size());
			graphMapperValues.entrySet().removeIf(e -> !(e.getKey() instanceof NSSItem) || ((NSSItem) e.getKey()).damage == OreDictionary.WILDCARD_VALUE || (Integer) e.getValue().intValue() <= 0);
			LOG.info("Filtered Values... Outcome " + graphMapperValues.size());
		}
		for(Map.Entry<NormalizedSimpleStack, Integer> entry : graphMapperValues.entrySet())
		{
			NSSItem normStackItem = (NSSItem) entry.getKey();
			Item obj = (Item) Item.REGISTRY.getObject(new ResourceLocation(normStackItem.itemName));
			if(obj != null)
			{
				EMCMapper.emc.put(new SimpleStack(obj.getRegistryName(), normStackItem.damage), entry.getValue());
				continue;
			}
			LOG.warn("Could not add EMC value for {}|{}. Can not get ItemID!", (Object) normStackItem.itemName, (Object) normStackItem.damage);
		}
		*/
	}
	
	public static void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{
		Map<IEMCMapper<NormalizedSimpleStack, Integer>, Configuration> cfgs = new HashMap<>();
		
		instance.expansions.forEach(ex ->
		{
			List<IEMCMapper<NormalizedSimpleStack, Integer>> mappers = new ArrayList<>();
			ex.getMappers(mappers);
			for(IEMCMapper<NormalizedSimpleStack, Integer> m : mappers)
				cfgs.put(m, ex.getConfig());
		});
		
		for(Map.Entry<IEMCMapper<NormalizedSimpleStack, Integer>, Configuration> e : cfgs.entrySet())
		{
			e.getKey().addMappings(mapper, e.getValue());
			LOG.info("Collected Mappings from " + e.getKey().getClass().getName());
		}
	}
}