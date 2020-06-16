// @Zeitheron

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import java.lang.Integer;

var BloodMagicRecipes = BloodMagicAPI.INSTANCE.getRecipeRegistrar();

// setup

var mod = "bloodmagic";

function registerEMC(configs, cfgFile)
{
    Data.set("bloodmagic:lpc", cfgFile.getCategory("Misc").getIntEntry("BloodCost", 4, 0, Integer.MAX_VALUE).setDescription("Determines cost of one LP (Life Point).").getValue());
    Data.set("bloodmagic:spc", cfgFile.getCategory("Misc").getIntEntry("SoulCost", 100, 0, Integer.MAX_VALUE).setDescription("Determines cost of one soul.").getValue());

    configs.addEMC(getItem(mod, "blood_shard"), "WeakBloodShard", 2048);
    configs.addEMC(getItem(mod, "item_demon_crystal"), 0, "DemonWillCrystal", 1024);
    configs.addEMC(getItem(mod, "item_demon_crystal"), 1, "CorrosiveWillCrystal", 1024);
    configs.addEMC(getItem(mod, "item_demon_crystal"), 2, "DestructiveWillCrystal", 1024);
    configs.addEMC(getItem(mod, "item_demon_crystal"), 3, "VengefulWillCrystal", 1024);
    configs.addEMC(getItem(mod, "item_demon_crystal"), 4, "SteadfastWillCrystal", 1024);
}

function addMappers(mappers)
{
    mappers.addMapper("BloodAltarEMCMapper");
    mappers.addMapper("AlchemyTableEMCMapper");
    mappers.addMapper("TartaricForgeEMCMapper");
    mappers.addMapper("AlchemyArrayEMCMapper");
}

// mappers

function BloodAltarEMCMapper(emc)
{
    var cost = Math.max(0, Data.get("bloodmagic:lpc"));

    BloodMagicRecipes.getAltarRecipes().forEach(function(recipe)
    {
        var im = Lists.arrayList();
        Ingredient.add(Ingredient.decode(recipe.getInput()), im);
        im.add(emc.fake(recipe.getSyphon() * cost).stack(1));
        emc.map(Ingredient.of(recipe.getOutput()), im);
    });
}

function AlchemyTableEMCMapper(emc)
{
    var cost = Math.max(0, Data.get("bloodmagic:lpc"));

    BloodMagicRecipes.getAlchemyRecipes().forEach(function(recipe)
    {
        var im = Lists.arrayList();
        recipe.getInput().forEach(function(ingredient) { Ingredient.add(Ingredient.decode(ingredient), im); })
        im.add(emc.fake(recipe.getSyphon() * cost).stack(1));
        emc.map(Ingredient.of(recipe.getOutput()), im);
    });
}

function TartaricForgeEMCMapper(emc)
{
    var cost = Math.max(0, Data.get("bloodmagic:spc"));

    BloodMagicRecipes.getTartaricForgeRecipes().forEach(function(recipe)
    {
        var im = Lists.arrayList();
        recipe.getInput().forEach(function(ingredient) { Ingredient.add(Ingredient.decode(ingredient), im); })
        im.add(emc.fake(recipe.getSoulDrain() * cost).stack(1));
        emc.map(Ingredient.of(recipe.getOutput()), im);
    });
}

function AlchemyArrayEMCMapper(emc)
{
    BloodMagicRecipes.getAlchemyArrayRecipes().forEach(function(recipe)
    {
        var im = Lists.arrayList();
        Ingredient.add(Ingredient.decode(recipe.getInput()), im);
        Ingredient.add(Ingredient.decode(recipe.getCatalyst()), im);
        emc.map(Ingredient.of(recipe.getOutput()), im);
    });
}