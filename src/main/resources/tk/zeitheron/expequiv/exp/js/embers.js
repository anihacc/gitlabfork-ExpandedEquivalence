// @Zeitheron

// setup

var mod = "embers";

function registerEMC(configs)
{
	configs.addEMC(getItem(mod, "ingot_dawnstone"), "DawnstoneIngot", 1024 * 3);
	configs.addEMC(getItem(mod, "shard_ember"), "EmberShard", 120);
	configs.addEMC(getItem(mod, "alchemic_waste"), "AlchemicalWaste", 32);
	configs.addEMC(getItem(mod, "archaic_brick"), "ArchaicBrick", 32);
	configs.addEMC(getItem(mod, "dust_ash"), "AshDust", 32);
}

function addMappers(mappers)
{
    mappers.addMapper("StamperEMCConverter");
    mappers.addMapper("AlchemyEMCConverter");
}

// mappers

import teamroots.embers.recipe.RecipeRegistry;

function StamperEMCConverter(emc)
{
    RecipeRegistry.stampingRecipes.forEach(function(recipe)
    {
        var im = Lists.arrayList();
        if(recipe.input != null) im.add(Ingredient.decode(emc, recipe.input));
        if(recipe.fluid != null) im.add(Ingredient.of(recipe.fluid));
        emc.map(Ingredient.of(recipe.result), im);
    });
}

function AlchemyEMCConverter(emc)
{
    RecipeRegistry.alchemyRecipes.forEach(function(recipe)
    {
        var im = Lists.arrayList();
        im.add(Ingredient.decode(emc, recipe.centerIngredient));
        recipe.outsideIngredients.forEach(function(i) { im.add(Ingredient.decode(emc, i)); });
        emc.map(Ingredient.of(recipe.result), im);
    });
}