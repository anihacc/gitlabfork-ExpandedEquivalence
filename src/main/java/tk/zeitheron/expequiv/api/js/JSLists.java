package tk.zeitheron.expequiv.api.js;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSLists
{
	public static <T> List<T> arrayList()
	{
		return new ArrayList<>();
	}
	
	public static IntList intList()
	{
		return new IntArrayList();
	}
	
	public static <T> Set<T> hashSet()
	{
		return new HashSet<>();
	}
}