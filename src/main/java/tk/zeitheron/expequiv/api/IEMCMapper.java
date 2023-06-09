package tk.zeitheron.expequiv.api;

import com.zeitheron.hammercore.cfg.file1132.Configuration;

public interface IEMCMapper
{
	void register(IEMC emc, Configuration cfg);
	
	default String getName()
	{
		return getClass().getName();
	}
	
	default boolean doLogRegistration() { return true; }
}