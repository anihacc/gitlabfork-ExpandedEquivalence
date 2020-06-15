// @Zeitheron

// setup

var mod = "forestry";

function registerEMC(configs)
{
	for(var i = 0; i < 7; ++i) configs.addEMC(getItem(mod, "fruits"), i, "AllFruits", 24);
	configs.addEMC(getItem(mod, "pollen"), 0, "PollenCluster", 32);
	configs.addEMC(getItem(mod, "ash"), "Ash", 32);
	configs.addEMC(getItem(mod, "propolis"), 2, "PulsaingPropolis", 40);
	configs.addEMC(getItem(mod, "propolis"), "Propolis", 64);
	configs.addEMC(getItem(mod, "phosphor"), "Phosphor", 64);
	configs.addEMC(getItem(mod, "beeswax"), "Beeswax", 64);
	configs.addEMC(getItem(mod, "crafting_material"), 2, "SilkWisp", 128);
	configs.addEMC(getItem(mod, "propolis"), 1, "StickyPropolis", 128);
	configs.addEMC(getItem(mod, "honey_drop"), "HoneyDrop", 128);
	configs.addEMC(getItem(mod, "apatite"), "Apatite", 128);
	configs.addEMC(getItem(mod, "peat"), "Peat", 128);
	configs.addEMC(getItem(mod, "pollen"), 1, "CrystallinePollenCluster", 512);
	configs.addEMC(getItem(mod, "propolis"), 3, "SilkyPropolis", 512);
	configs.addEMC(getItem(mod, "royal_jelly"), "RoyalJelly", 512);
	configs.addEMC(getItem(mod, "grafter_proven"), "ProvenGrafter", 16384);
}

function addMappers(mappers)
{
    mappers.addMapper("CarpenterEMCMapper");
    mappers.addMapper("ThermionicEMCMapper");
    mappers.addMapper("MoistenerEMCMapper");
    mappers.addMapper("SqueezerEMCMapper");
    mappers.addMapper("StillEMCMapper");
}

// mappers
import forestry.api.recipes.RecipeManagers;

function CarpenterEMCMapper(emc)
{
    RecipeManagers.carpenterManager.recipes().forEach(function(recipe)
    {
        var grid = recipe.getCraftingGridRecipe();
        var result = grid.getOutput();
        var im = Lists.arrayList();
        if(!ItemStack.isEmpty(recipe.getBox())) im.add(Ingredient.of(recipe.getBox()));
        recipe.getCraftingGridRecipe().getRawIngredients().forEach(function(inputs) { if(!inputs.isEmpty()) im.add(Ingredient.of(inputs)); });
        var fluid = recipe.getFluidResource();
        if(fluid) im.add(Ingredient.of(fluid));
        emc.map(Ingredient.of(result), im);
    });
}

function ThermionicEMCMapper(emc)
{
    RecipeManagers.fabricatorManager.recipes().forEach(function(recipe)
    {
        var result = recipe.getRecipeOutput();
        var im = Lists.arrayList();
        if(!ItemStack.isEmpty(recipe.getPlan())) im.add(Ingredient.of(recipe.getPlan()));
        if(recipe.getLiquid()) im.add(Ingredient.of(recipe.getLiquid()));
        recipe.getIngredients().forEach(function(inputs) { if(!inputs.isEmpty()) im.add(Ingredient.of(inputs)); });
        emc.map(Ingredient.of(result), im);
    });

    RecipeManagers.fabricatorSmeltingManager.recipes().forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getProduct()), Ingredient.of(recipe.getResource()));
    });
}

function MoistenerEMCMapper(emc)
{
    RecipeManagers.moistenerManager.recipes().forEach(function(recipe)
    {
        var result = recipe.getProduct();
        emc.map(Ingredient.of(result), Ingredient.of(recipe.getResource()));
    });
}

function SqueezerEMCMapper(emc)
{
    RecipeManagers.squeezerManager.recipes().forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getFluidOutput()), Ingredient.of(recipe.getResources()));
    });
}

function StillEMCMapper(emc)
{
    RecipeManagers.stillManager.recipes().forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getOutput()), Ingredient.of(recipe.getInput()));
    });
}