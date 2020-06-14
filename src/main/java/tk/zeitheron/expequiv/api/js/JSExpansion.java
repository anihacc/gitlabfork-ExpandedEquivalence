package tk.zeitheron.expequiv.api.js;

import com.zeitheron.hammercore.cfg.file1132.Configuration;
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
import tk.zeitheron.expequiv.api.IEMCConverter;
import tk.zeitheron.expequiv.exp.Expansion;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class JSExpansion extends Expansion
{
	public final ScriptEngine engine;
	public final Invocable invocable;
	
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
		
		this.engine = new ScriptEngineManager(null).getEngineByName("nashorn");
		this.engine.eval(js.read());
		this.invocable = (Invocable) engine;
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
		invoke("registerEMC", jsc, getConfig());
		if(getConfig().hasChanged()) getConfig().save();
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		Internal.setContext(new ExpansionContext(this));
		invoke("addMappers", new MapperAcceptor(mappers, this));
	}
	
	public JSCallbackInfo invoke(String fun, Object... args)
	{
		try
		{
			return new JSCallbackInfo(invocable.invokeFunction(fun, args));
		} catch(NoSuchMethodException e)
		{
			return new JSCallbackInfo(false, false, e);
		} catch(Throwable e)
		{
			return new JSCallbackInfo(true, false, e);
		}
	}
	
	@Override
	public Logger getLogger()
	{
		if(log == null) log = LogManager.getLogger(InfoEE.MOD_NAME + "/" + modid + ".js");
		return log;
	}
	
	public static class MapperAcceptor
	{
		final List<IEMCConverter> converters;
		final JSExpansion expansion;
		
		public MapperAcceptor(List<IEMCConverter> converters, JSExpansion expansion)
		{
			this.converters = converters;
			this.expansion = expansion;
		}
		
		public void addMapper(String fun)
		{
			converters.add(new JSEMCConverter(fun));
		}
		
		public class JSEMCConverter implements IEMCConverter
		{
			final String func;
			
			public JSEMCConverter(String func)
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
	}
}