// @Zeitheron

// setup

var mod = "astralsorcery";

function tweakData()
{
    Data.set("minecraft:ore_rate", Math.max(Data.get("minecraft:ore_rate"), 3));
    Data.set("minecraft:diamond_ore_rate", Math.max(Data.get("minecraft:diamond_ore_rate"), 4));
    Data.set("minecraft:emerald_ore_rate", Math.max(Data.get("minecraft:emerald_ore_rate"), 4));
}

function registerEMC(configs)
{
	configs.addEMC(getItem(mod, "itemcraftingcomponent"), 0, "Aquamarine", 256);
	configs.addEMC(getItem(mod, "itemcraftingcomponent"), 1, "Starmetal", 512);
	configs.addEMC(getItem(mod, "itemcraftingcomponent"), 2, "Starmetal", 512);
	configs.addEMC(getItem(mod, "blockinfusedwood"), 0, "InfusedWood", 48);
}

function addMappers(mappers)
{
    mappers.addMapper("AltarEMCMapper");
    mappers.addMapper("StarlightInfusionEMCMapper");
}

// mappers

import tk.zeitheron.expequiv.api.FakeItem;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import hellfirepvp.astralsorcery.common.crafting.infusion.InfusionRecipeRegistry;

function AltarEMCMapper(emc)
{
    var recipes = Lists.hashSet();
    AltarRecipeRegistry.recipes.values().forEach(function(f) { recipes.addAll(f); });
    AltarRecipeRegistry.mtRecipes.values().forEach(function(f) { recipes.addAll(f); });
    recipes.forEach(function(recipe)
    {
        var result = recipe.getOutputForMatching();
        if(ItemStack.isEmpty(result)) return;
        var im = Lists.arrayList();

        var nt = recipe.getNativeRecipe();

        (nt.func_192400_c ? nt.func_192400_c() : nt.getIngredients()).forEach(function(i)
        {
            if(!Ingredient.isEmpty(i))
                im.add(FakeItem.create(emc, 1, i));
        });

        var xp = emc.fake(100 * recipe.getCraftExperience() * recipe.getCraftExperienceMultiplier());
        im.add(xp.stack(1));
        emc.map(Ingredient.of(result), im);
    });
}

function StarlightInfusionEMCMapper(emc)
{
    var recipes = Lists.hashSet();
    recipes.addAll(InfusionRecipeRegistry.recipes);
    recipes.addAll(InfusionRecipeRegistry.mtRecipes);
    recipes.forEach(function(recipe)
    {
        var result = recipe.getOutputForMatching();
        if(ItemStack.isEmpty(result)) return;
        var starlight = emc.fake(1000 * recipe.getLiquidStarlightConsumptionChance());
        emc.map(Ingredient.of(result), FakeItem.create(emc, 1, recipe.getInput().getRecipeIngredient()), starlight.stack(1));
    });
}