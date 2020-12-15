package tk.zeitheron.expequiv.exp.hammercore;

import com.zeitheron.hammercore.api.crafting.AbstractRecipeRegistry;
import com.zeitheron.hammercore.api.crafting.ICraftingResult;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import tk.zeitheron.expequiv.api.CountedIngredient;
import tk.zeitheron.expequiv.api.IEMC;
import tk.zeitheron.expequiv.api.IEMCMapper;
import tk.zeitheron.expequiv.api.js.JSIngredient;
import tk.zeitheron.expequiv.api.js.JSLists;

import java.util.ArrayList;
import java.util.List;

class AbstractEMCMapper implements IEMCMapper
{
	@Override
	public void register(IEMC emc, Configuration cfg)
	{
		AbstractRecipeRegistry
				.getAllRegistries()
				.stream()
				.filter(registry ->
						cfg.getCategory("Recipe Categories")
								.getBooleanEntry(registry.getRegistryId().toString(), true)
								.setDescription("Enable this recipe category?")
								.getValue())
				.flatMap(r -> r.getRecipes().stream())
				.forEach(recipe ->
				{
					ICraftingResult<?> result = recipe.getResult();
					
					if(result.hasBaseOutput())
					{
						Object[] outputs = result.getAllBaseOutputs();
						
						List<CountedIngredient> im = JSLists.arrayList();
						recipe.getIngredients().forEach(ingredient -> JSIngredient.add(JSIngredient.decode(emc, ingredient), im));
						
						if(outputs.length > 1)
						{
							List<CountedIngredient> out = new ArrayList<>();
							for(Object o : outputs)
								out.add(JSIngredient.decode(o));
							emc.multiMap(out, im);
						} else
						{
							emc.map(JSIngredient.decode(outputs[0]), im);
						}
					}
				});
		if(cfg.hasChanged()) cfg.save();
	}
}