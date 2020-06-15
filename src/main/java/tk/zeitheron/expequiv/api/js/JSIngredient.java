package tk.zeitheron.expequiv.api.js;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import tk.zeitheron.expequiv.api.CountedIngredient;
import tk.zeitheron.expequiv.api.FakeItem;
import tk.zeitheron.expequiv.api.IEMC;

import java.util.Collection;
import java.util.stream.StreamSupport;

public class JSIngredient
{
	public static IEMC defaultEMC = IEMC.PE_WRAPPER;
	
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
	
	public static CountedIngredient of(IEMC emc, Iterable<ItemStack> stacks)
	{
		if(stacks instanceof Collection)
		{
			Collection<ItemStack> scoll = (Collection<ItemStack>) stacks;
			return of(emc, scoll.toArray(new ItemStack[0]));
		}
		return of(emc, StreamSupport.stream(stacks.spliterator(), false).toArray(ItemStack[]::new));
	}
	
	public static CountedIngredient of(Iterable<ItemStack> stacks)
	{
		return of(defaultEMC, stacks);
	}
	
	public static CountedIngredient of(IEMC emc, Iterable<ItemStack> stacks, int count)
	{
		if(stacks instanceof Collection)
		{
			Collection<ItemStack> scoll = (Collection<ItemStack>) stacks;
			return of(emc, scoll.toArray(new ItemStack[0]));
		}
		return of(emc, count, StreamSupport.stream(stacks.spliterator(), false).toArray(ItemStack[]::new));
	}
	
	public static CountedIngredient of(Iterable<ItemStack> stacks, int count)
	{
		return of(defaultEMC, stacks, count);
	}
	
	public static CountedIngredient of(IEMC emc, ItemStack... stacks)
	{
		return of(emc, Ingredient.fromStacks(stacks));
	}
	
	public static CountedIngredient of(IEMC emc, int count, ItemStack... stacks)
	{
		return of(emc, count, Ingredient.fromStacks(stacks));
	}
	
	public static CountedIngredient of(IEMC emc, Ingredient ingredient)
	{
		return FakeItem.create(emc, 1, ingredient);
	}
	
	public static CountedIngredient of(Ingredient ingredient)
	{
		return FakeItem.create(defaultEMC, 1, ingredient);
	}
	
	public static CountedIngredient of(IEMC emc, int count, Ingredient ingredient)
	{
		return FakeItem.create(emc, count, ingredient);
	}
	
	public static CountedIngredient of(int count, Ingredient ingredient)
	{
		return FakeItem.create(defaultEMC, count, ingredient);
	}
	
	public static CountedIngredient of(ItemStack... stacks)
	{
		return of(defaultEMC, stacks);
	}
	
	public static CountedIngredient of(FluidStack stack)
	{
		return CountedIngredient.create(stack, 1);
	}
	
	public static CountedIngredient of(ItemStack stack, int count)
	{
		return CountedIngredient.create(stack.copy().splitStack(1), count);
	}
	
	public static CountedIngredient merge(Collection<CountedIngredient> ingredients)
	{
		return merge(defaultEMC, ingredients);
	}
	
	public static CountedIngredient merge(IEMC emc, Collection<CountedIngredient> ingredients)
	{
		FakeItem fake = new FakeItem();
		emc.map(fake, 1, ingredients);
		return fake.stack(1);
	}
	
	public static boolean isEmpty(Ingredient ingr)
	{
		return ingr == null || ingr.getMatchingStacks().length == 0;
	}
	
	public static CountedIngredient decode(IEMC emc, Object obj)
	{
		return CountedIngredient.tryCreate(emc, obj);
	}
	
	public static CountedIngredient decode(Object obj)
	{
		return decode(defaultEMC, obj);
	}
	
	public static void add(CountedIngredient i, Collection<CountedIngredient> coll)
	{
		if(i != null && i.getCount() > 0 && i.getIngredient() != null)
			coll.add(i);
	}
}