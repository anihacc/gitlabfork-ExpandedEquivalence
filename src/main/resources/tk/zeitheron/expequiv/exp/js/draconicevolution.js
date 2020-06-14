// @Zeitheron

// setup

var mod = "draconicevolution";

function registerEMC(configs)
{
	configs.addEMC(getItem(mod, "draconium_dust"), "DraconiumDust", 8192);
	configs.addEMC(getItem(mod, "dragon_heart"), "DragonHeart", 139264);
	configs.addEMC(getItem(mod, "chaos_shard"), "ChaosShard", 1024000);
}

function addMappers(mappers)
{
    mappers.addMapper("FusionEMCConverter");
}

// mappers

function FusionEMCConverter(emc)
{
import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;

    FusionRecipeAPI.getRecipes().forEach(function(recipe)
    {
        var result = recipe.getRecipeOutput(recipe.getRecipeCatalyst());
        if(ItemStack.isEmpty(result)) return;
        var im = Lists.arrayList();
        im.add(Ingredient.of(ItemStack.copy(recipe.getRecipeCatalyst())));
        recipe.getRecipeIngredients().forEach(function(i)
        {
            var dec = Ingredient.decode(emc, i);
            if(dec) im.add(dec);
        });
        emc.map(Ingredient.of(result), im);
    });
}