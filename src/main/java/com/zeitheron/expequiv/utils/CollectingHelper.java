package com.zeitheron.expequiv.utils;

import java.lang.reflect.Field;

import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.collector.LongToBigFractionCollector;
import moze_intel.projecte.emc.collector.WildcardSetValueFixCollector;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;

public class CollectingHelper
{
	public static <T> LongToBigFractionCollector<T, ?> getLTBFC(IMappingCollector<T, ?> mapper)
	{
		try
		{
			Field inner = WildcardSetValueFixCollector.class.getDeclaredField("inner");
			inner.setAccessible(true);
			return (LongToBigFractionCollector) inner.get(mapper);
		} catch(Throwable er)
		{
			er.printStackTrace();
		}
		return null;
	}
}