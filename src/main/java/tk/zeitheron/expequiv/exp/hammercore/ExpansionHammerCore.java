package tk.zeitheron.expequiv.exp.hammercore;

import com.zeitheron.hammercore.cfg.file1132.Configuration;
import tk.zeitheron.expequiv.api.IEMCMapper;
import tk.zeitheron.expequiv.exp.Expansion;
import tk.zeitheron.expequiv.exp.ExpansionReg;

import java.util.List;

@ExpansionReg(modid = "hammercore")
public class ExpansionHammerCore extends Expansion
{
	public ExpansionHammerCore(String modid, Configuration config, Object[] args)
	{
		super(modid, config, args);
	}
	
	@Override
	public void getMappers(List<IEMCMapper> list)
	{
		list.add(new AbstractEMCMapper());
	}
}