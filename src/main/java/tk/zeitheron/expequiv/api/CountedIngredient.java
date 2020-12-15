package tk.zeitheron.expequiv.api;

import com.zeitheron.hammercore.api.EnergyUnit;
import com.zeitheron.hammercore.api.crafting.IBaseIngredient;
import com.zeitheron.hammercore.api.crafting.IEnergyIngredient;
import com.zeitheron.hammercore.api.crafting.IFluidIngredient;
import com.zeitheron.hammercore.api.crafting.IItemIngredient;
import com.zeitheron.hammercore.utils.charging.fe.FECharge;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fluids.FluidStack;
import tk.zeitheron.expequiv.api.js.JSData;
import tk.zeitheron.expequiv.api.js.JSLists;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CountedIngredient
{
	private final int count;
	private final Object ingredient;
	
	private CountedIngredient(int count, Object ingredient)
	{
		this.count = count;
		this.ingredient = ingredient;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public Object getIngredient()
	{
		return ingredient;
	}
	
	public CountedIngredient stack(int amount)
	{
		return new CountedIngredient(this.count * amount, ingredient);
	}
	
	public static CountedIngredient create(ItemStack stack, int count)
	{
		if(stack.isEmpty() || count < 1)
			return null;
		
		IEMC emc = IEMC.PE_WRAPPER;
		
		if(stack.getItem() instanceof ItemPotion)
		{
			PotionType type = PotionUtils.getPotionFromItem(stack);
			Map<PotionType, Long> costs = (Map<PotionType, Long>) JSData.get("minecraft:potion_type_costs");
			if(costs != null && costs.containsKey(type))
			{
				long emcv = costs.get(type);
				List<CountedIngredient> ci = JSLists.arrayList();
				ci.add(emc.fake(emcv).stack(1));
				
				ci.add(create(new ItemStack(Items.GLASS_BOTTLE)));
				
				if(stack.getItem() == Items.SPLASH_POTION)
				{
					ci.add(create(new ItemStack(Items.GUNPOWDER)));
				}
				
				if(stack.getItem() == Items.LINGERING_POTION)
				{
					ci.add(create(new ItemStack(Items.DRAGON_BREATH)));
				}
				
				FakeItem out = emc.fake();
				emc.map(out.stack(1), ci);
				return out.stack(count);
			}
		}
		
		return new CountedIngredient(count, stack.copy().splitStack(1));
	}
	
	public static CountedIngredient create(ItemStack stack)
	{
		return create(stack, stack.getCount());
	}
	
	public static CountedIngredient create(Item item, int count)
	{
		return new CountedIngredient(count, item);
	}
	
	public static CountedIngredient create(Block block, int count)
	{
		return new CountedIngredient(count, block);
	}
	
	public static CountedIngredient create(FluidStack fluid, int count)
	{
		return new CountedIngredient(count, fluid);
	}
	
	public static CountedIngredient create(FluidStack fluid)
	{
		if(fluid == null)
			return null;
		return new CountedIngredient(fluid.amount, fluid);
	}
	
	public static CountedIngredient create(String od, int count)
	{
		return new CountedIngredient(count, od);
	}
	
	public static CountedIngredient create(FakeItem item, int count)
	{
		return new CountedIngredient(count, item.getHolder());
	}
	
	public static CountedIngredient tryCreate(IEMC emc, Object x)
	{
		if(x instanceof ItemStack)
			return create((ItemStack) x);
		
		if(x instanceof Item)
			return create((Item) x, 1);
		
		if(x instanceof Block)
		{
			Item blk = Item.getItemFromBlock((Block) x);
			if(blk != Items.AIR) return create(blk, 1);
			else return null;
		}
		
		if(x instanceof Ingredient)
			return FakeItem.create(emc, (Ingredient) x, 1);
		
		if(x instanceof List)
		{
			List<?> unawareList = (List) x;
			List<CountedIngredient> inputs = unawareList
					.stream()
					.map(o -> tryCreate(emc, o))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
			CountedIngredient out = emc.fake().stack(1);
			emc.map(out, inputs);
			return out;
		}
		
		if(x instanceof String)
			return create(x.toString(), 1);
		
		if(x instanceof FluidStack)
			return create((FluidStack) x);
		
		if(x instanceof FECharge)
		{
			long emcv = (long) Math.ceil(EnergyUnit.EMC.convertFrom(((FECharge) x).FE, EnergyUnit.FE));
			return emc.fake(emcv).stack(1);
		}
		
		// Add native HammerCore support
		if(x instanceof IBaseIngredient)
		{
			IBaseIngredient base = (IBaseIngredient) x;
			
			if(base instanceof IItemIngredient)
			{
				IItemIngredient item = (IItemIngredient) base;
				return tryCreate(emc, item.asIngredient());
			}
			
			if(base instanceof IFluidIngredient)
			{
				IFluidIngredient fluid = (IFluidIngredient) base;
				List<FluidStack> matching = fluid.asIngredient();
				List<CountedIngredient> inputs = matching.stream().map(CountedIngredient::create).collect(Collectors.toList());
				FakeItem all = emc.fake();
				emc.map(all.stack(1), inputs);
				return all.stack(1);
			}
			
			if(base instanceof IEnergyIngredient)
			{
				IEnergyIngredient energy = (IEnergyIngredient) base;
				long emcv = (long) Math.ceil(EnergyUnit.EMC.convertFrom(energy.getAmount().doubleValue(), energy.getUnit()));
				return emc.fake(emcv).stack(1);
			}
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		return "CountedIngredient{" +
				"count=" + count +
				", ingredient=" + ingredient +
				'}';
	}
}