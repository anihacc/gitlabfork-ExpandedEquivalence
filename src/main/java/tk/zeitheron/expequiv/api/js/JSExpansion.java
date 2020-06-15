package tk.zeitheron.expequiv.api.js;

import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.lib.nashorn.JSCallbackInfo;
import com.zeitheron.hammercore.lib.nashorn.JSScript;
import com.zeitheron.hammercore.lib.nashorn.JSSource;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.api.proxy.ITransmutationProxy;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.zeitheron.expequiv.ExpandedEquivalence;
import tk.zeitheron.expequiv.InfoEE;
import tk.zeitheron.expequiv.api.IEMC;
import tk.zeitheron.expequiv.api.IEMCMapper;
import tk.zeitheron.expequiv.exp.Expansion;

import javax.script.ScriptException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JSExpansion extends Expansion
{
	public final JSScript script;
	
	public JSExpansion(String modid, Configuration config, Object[] args, JSSource js) throws ScriptException
	{
		super(modid, config, args);
		
		js = js
				.inheritClassMethods(Internal.class, (name, method) -> !name.equals("setContext"))
				.addClassPointer(JSIngredient.class, "Ingredient")
				.addClassPointer(JSLists.class, "Lists")
				.addClassPointer(JSStack.class, "ItemStack")
				.addClassPointer(JSReflection.class, "Reflection")
				.addClassPointer(JSVanilla.class, "Vanilla")
				.addClassPointer(JSData.class, "Data")
				.processImports();
		
		this.script = new JSScript(js);
	}
	
	public void contruct(int phase)
	{
		if(phase == 0) invoke("setupData");
		else if(phase == 1) invoke("tweakData");
	}
	
	@Override
	public long getCfgEMC(long base, String id)
	{
		return super.getCfgEMC(base, id);
	}
	
	@Override
	public long getCfgEMC(long base, String id, String name)
	{
		return super.getCfgEMC(base, id, name);
	}
	
	@Override
	public void postInit(IEMCProxy emcProxy, ITransmutationProxy transmutateProxy)
	{
		JSConfigs jsc = new JSConfigs(this, getConfig());
		Internal.setContext(new ExpansionContext(this));
		JSCallbackInfo cb = invoke("registerEMC", jsc, getConfig());
		if(cb.functionExists && cb.error != null) cb.error.printStackTrace();
		if(getConfig().hasChanged()) getConfig().save();
	}
	
	@Override
	public void getMappers(List<IEMCMapper> list)
	{
		Internal.setContext(new ExpansionContext(this));
		invoke("addMappers", new MapperAcceptor(list, this));
	}
	
	public JSCallbackInfo invoke(String fun, Object... args)
	{
		return script.callFunction(fun, args);
	}
	
	@Override
	public Logger getLogger()
	{
		if(log == null) log = LogManager.getLogger(InfoEE.MOD_NAME + "/" + modid + ".js");
		return log;
	}
	
	public static class MapperAcceptor
	{
		final List<IEMCMapper> converters;
		final JSExpansion expansion;
		
		public MapperAcceptor(List<IEMCMapper> converters, JSExpansion expansion)
		{
			this.converters = converters;
			this.expansion = expansion;
		}
		
		public void addMapper(String fun)
		{
			converters.add(new JSEMCMapper(fun));
		}
		
		public class JSEMCMapper implements IEMCMapper
		{
			final String func;
			
			public JSEMCMapper(String func)
			{
				this.func = func;
			}
			
			@Override
			public String getName()
			{
				return expansion.modid + ".js/" + func;
			}
			
			@Override
			public void register(IEMC emc, Configuration cfg)
			{
				JSIngredient.defaultEMC = new JSEMCMapping(emc);
				Internal.setContext(new ExpansionContext(expansion));
				
				JSCallbackInfo cb = expansion.invoke(func, JSIngredient.defaultEMC);
				
				if(!cb.functionExists) Internal.error("Unable to find EMC Mapper " + getName());
				else if(!cb.callSuccessful) Internal.error("Failed to call EMC Mapper " + getName());
				else Internal.info("Processed EMC Mapper " + getName());
				
				if(cb.error != null) cb.error.printStackTrace();
			}
			
			@Override
			public boolean doLogRegistration()
			{
				return false;
			}
		}
	}
	
	public static class ExpansionContext
	{
		public final JSExpansion expansion;
		public final Logger logger;
		
		public ExpansionContext(JSExpansion expansion)
		{
			this.expansion = expansion;
			this.logger = expansion.getLogger();
		}
	}
	
	public static class Internal
	{
		public static ExpansionContext context = null;
		public static Logger logger = ExpandedEquivalence.LOG;
		
		public static void setContext(ExpansionContext ctx)
		{
			context = ctx;
			logger = ctx == null ? ExpandedEquivalence.LOG : ctx.logger;
		}
		
		public static void warn(String msg)
		{
			logger.warn(msg);
		}
		
		public static void error(String msg)
		{
			logger.error(msg);
		}
		
		public static Item getItem(String namespace, String key)
		{
			Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(namespace, key));
			if(i == null || i == Items.AIR) error("Unable to find item " + namespace + ":" + key);
			return i;
		}
		
		public static void info(String msg)
		{
			logger.info(msg);
		}
		
		public static Logger getLog()
		{
			return logger;
		}
		
		public static String toString(Object anything)
		{
			if(JSLists.isArray(anything)) return Arrays.toString((Object[]) anything);
			return Objects.toString(anything);
		}
	}
}