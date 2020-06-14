package tk.zeitheron.expequiv.api.js;

import java.util.HashMap;
import java.util.Map;

public class JSData
{
	private static final Map<String, Object> map = new HashMap<>();
	
	public static void setup(String key, Object value)
	{
		map.putIfAbsent(key, value);
	}
	
	public static void set(String key, Object value)
	{
		map.put(key, value);
	}
	
	public static Object get(String key)
	{
		return map.get(key);
	}
}