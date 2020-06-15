package tk.zeitheron.expequiv.exp.hammercore;

import com.zeitheron.hammercore.api.crafting.AbstractRecipeRegistry;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import net.minecraft.item.ItemStack;
import tk.zeitheron.expequiv.api.CountedIngredient;
import tk.zeitheron.expequiv.api.IEMC;
import tk.zeitheron.expequiv.api.IEMCMapper;
import tk.zeitheron.expequiv.api.js.JSIngredient;
import tk.zeitheron.expequiv.api.js.JSLists;

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
					ItemStack result = recipe.getRecipeOutput();
					List<CountedIngredient> im = JSLists.arrayList();
					recipe.getIngredients().forEach(ingredient -> JSIngredient.add(JSIngredient.decode(emc, ingredient), im));
					emc.map(result, im);
				});
		if(cfg.hasChanged()) cfg.save();
	}
}