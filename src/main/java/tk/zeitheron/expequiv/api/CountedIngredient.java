package tk.zeitheron.expequiv.api;

import com.zeitheron.hammercore.api.EnergyUnit;
import com.zeitheron.hammercore.api.crafting.IBaseIngredient;
import com.zeitheron.hammercore.api.crafting.IEnergyIngredient;
import com.zeitheron.hammercore.api.crafting.IFluidIngredient;
import com.zeitheron.hammercore.api.crafting.IItemIngredient;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
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
			List unawareList = (List) x;
			if(!unawareList.isEmpty())
			{
				Object unawareType = unawareList.get(0);
				if(unawareType instanceof ItemStack)
				{
					List<ItemStack> aware = (List<ItemStack>) unawareList;
					return tryCreate(emc, Ingredient.fromStacks(aware.toArray(new ItemStack[0])));
				}
			}
		}
		
		if(x instanceof String)
			return create(x.toString(), 1);
		
		if(x instanceof FluidStack)
			return create((FluidStack) x);
		
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