package tk.zeitheron.expequiv.api.js;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class JSStack
{
	public static final ItemStack EMPTY = ItemStack.EMPTY;
	
	public static boolean isEmpty(ItemStack stack)
	{
		return stack == null || stack.isEmpty();
	}
	
	public static int getMetadata(ItemStack stack)
	{
		return stack.getItemDamage();
	}
	
	public static Item getItem(ItemStack stack)
	{
		return stack.getItem();
	}
	
	public static ItemStack of(Item item)
	{
		return new ItemStack(item);
	}
	
	public static ItemStack of(Item item, int amt)
	{
		return new ItemStack(item, amt);
	}
	
	public static ItemStack of(Item item, int amt, int dmg)
	{
		return new ItemStack(item, amt, dmg);
	}
	
	public static ItemStack copy(ItemStack stack)
	{
		return stack.copy();
	}
	
	public static ItemStack fromState(IBlockState state)
	{
		Block blk = state.getBlock();
		Item item = blk.getItemDropped(state, Blk.BLKRAND, 0);
		int q = blk.quantityDropped(state, 0, Blk.BLKRAND);
		int d = blk.damageDropped(state);
		if(item != Items.AIR && item != null && q > 0) return new ItemStack(item, q, d);
		return EMPTY;
	}
	
	private static class Blk extends Block
	{
		public static Random BLKRAND = RANDOM;
		
		public Blk(Material materialIn)
		{
			super(materialIn);
		}
	}
}