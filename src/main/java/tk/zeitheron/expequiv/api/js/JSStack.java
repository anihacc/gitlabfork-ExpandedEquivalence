package tk.zeitheron.expequiv.api.js;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class JSStack
{
	public static final ItemStack EMPTY = ItemStack.EMPTY;
	
	public static boolean isEmpty(ItemStack stack)
	{
		return stack.isEmpty();
	}
	
	public static int getMetadata(ItemStack stack)
	{
		return stack.getItemDamage();
	}
	
	public static Item getItem(ItemStack stack)
	{
		return stack.getItem();
	}
	
	public static NonNullList<ItemStack> list()
	{
		return NonNullList.create();
	}
}