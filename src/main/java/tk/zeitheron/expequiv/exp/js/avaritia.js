// @Zeitheron

// setup

function registerEMC(configs)
{
	configs.addEMC(getItem("avaritia", "resource"), 2, "NeutronPile", 512);
}

function addMappers(mappers)
{
    mappers.addMapper("CompressionEMCConverter");
    mappers.addMapper("ExtremeEMCConverter");
}

// mappers

import morph.avaritia.recipe.AvaritiaRecipeManager;
import tk.zeitheron.expequiv.api.FakeItem;

function CompressionEMCConverter(emc)
{
    AvaritiaRecipeManager.COMPRESSOR_RECIPES.values().forEach(function(recipe)
    {
        var result = recipe.getResult();
        if(ItemStack.isEmpty(result)) return;
        var cost = recipe.getCost() / 2;
        var im = Lists.arrayList();
        recipe.getIngredients().forEach(function(i)
        {
            if(!Ingredient.isEmpty(i))
                im.add(FakeItem.create(emc, 1, i));
        });
        emc.map(Ingredient.of(result), im);
    });
}

function ExtremeEMCConverter(emc)
{
    AvaritiaRecipeManager.EXTREME_RECIPES.values().forEach(function(recipe)
    {
        var result = recipe.getRecipeOutput();
        if(ItemStack.isEmpty(result)) return;
        var im = Lists.arrayList();
        recipe.getIngredients().forEach(function(i)
        {
            if(!Ingredient.isEmpty(i))
                im.add(FakeItem.create(emc, 1, i));
        });
        emc.map(Ingredient.of(result), im);
    });
}