package tk.zeitheron.expequiv.api.js;

import tk.zeitheron.expequiv.api.CountedIngredient;
import tk.zeitheron.expequiv.api.IEMC;

public class JSEMCMapping implements IEMC
{
	public final IEMC emc;
	
	public JSEMCMapping(IEMC emc)
	{
		this.emc = emc;
	}
	
	@Override
	public void map(CountedIngredient output, CountedIngredient... in)
	{
		emc.map(output, in);
	}
}