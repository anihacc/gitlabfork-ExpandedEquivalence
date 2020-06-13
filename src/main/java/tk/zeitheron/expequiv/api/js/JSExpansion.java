package tk.zeitheron.expequiv.api.js;

import com.zeitheron.hammercore.cfg.file1132.Configuration;
import moze_intel.projecte.api.proxy.IEMCProxy;
import moze_intel.projecte.api.proxy.ITransmutationProxy;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.zeitheron.expequiv.ExpandedEquivalence;
import tk.zeitheron.expequiv.InfoEE;
import tk.zeitheron.expequiv.api.CountedIngredient;
import tk.zeitheron.expequiv.api.IEMC;
import tk.zeitheron.expequiv.api.IEMCConverter;
import tk.zeitheron.expequiv.exp.Expansion;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
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
				.addClassPointer(JSStack.class, "ItemStack")
				.processImports();
		
		this.engine = new ScriptEngineManager().getEngineByName("nashorn");
		this.engine.eval(js.read());
		this.invocable = (Invocable) engine;
	}
	
	public void contruct()
	{
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
		invoke("registerEMC", jsc);
		if(getConfig().hasChanged()) getConfig().save();
	}
	
	@Override
	public void getConverters(List<IEMCConverter> mappers)
	{
		invoke("addMappers", new MapperAcceptor(mappers, this));
	}
	
	public Object invoke(String fun, Object... args)
	{
		try
		{
			return invocable.invokeFunction(fun, args);
		} catch(ScriptException e)
		{
			e.printStackTrace();
		} catch(NoSuchMethodException ignored)
		{
		}
		return null;
	}
	
	@Override
	public Logger getLogger()
	{
		if(log == null) log = LogManager.getLogger(InfoEE.MOD_NAME + "/InternalJS");
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
			converters.add(new IEMCConverter()
			{
				@Override
				public String getName()
				{
					return expansion.modid + ".js/" + fun;
				}
				
				@Override
				public void register(IEMC emc, Configuration cfg)
				{
					expansion.invoke(fun, new JSEMCMapping(emc));
				}
			});
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
	
	public static class JSIngredient
	{
		public static CountedIngredient of(Item item, int amount)
		{
			return CountedIngredient.create(item, amount);
		}
		
		public static CountedIngredient of(Item item, int metadata, int amount)
		{
			return CountedIngredient.create(new ItemStack(item, 1, metadata), amount);
		}
		
		public static CountedIngredient of(ItemStack stack)
		{
			return CountedIngredient.create(stack.copy().splitStack(1), stack.getCount());
		}
		
		public static CountedIngredient of(ItemStack stack, int count)
		{
			return CountedIngredient.create(stack.copy().splitStack(1), count);
		}
		
		public static List<CountedIngredient> list()
		{
			return new ArrayList<>();
		}
	}
	
	public static class Internal
	{
		public static ExpansionContext context = null;
		public static Logger logger = ExpandedEquivalence.LOG;
		
		public static void setContext(ExpansionContext ctx)
		{
			context = ctx;
			logger = ctx.logger;
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
	}
}