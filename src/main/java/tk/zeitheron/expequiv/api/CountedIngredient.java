package tk.zeitheron.expequiv.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

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
	
	public static CountedIngredient create(ItemStack stack, int count)
	{
		if(stack.isEmpty() || count < 1)
			return null;
		stack = stack.copy();
		stack.setCount(1);
		return new CountedIngredient(count, stack);
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
			return create((Block) x, 1);
		if(x instanceof Ingredient)
			return FakeItem.create(emc, (Ingredient) x, 1);
		if(x instanceof String)
			return create(x.toString(), 1);
		if(x instanceof FluidStack)
			return create((FluidStack) x);
		return null;
	}
}