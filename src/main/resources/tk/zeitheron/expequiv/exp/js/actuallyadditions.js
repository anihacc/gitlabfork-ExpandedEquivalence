// @Zeitheron

// setup

var mod = "actuallyadditions";

function tweakData()
{
    Data.set("minecraft:ore_rate", Math.max(Data.get("minecraft:ore_rate"), 2));
    Data.set("minecraft:coal_ore_rate", Math.max(Data.get("minecraft:coal_ore_rate"), 2));
    Data.set("minecraft:diamond_ore_rate", Math.max(Data.get("minecraft:diamond_ore_rate"), 2));
    Data.set("minecraft:emerald_ore_rate", Math.max(Data.get("minecraft:emerald_ore_rate"), 2));
    Data.set("minecraft:quartz_ore_rate", Math.max(Data.get("minecraft:quartz_ore_rate"), 3));
}

function registerEMC(configs)
{
    var item_misc = getItem(mod, "item_misc");
	configs.addEMC(item_misc, 13, "Canola", 64);
	configs.addEMC(item_misc, 15, "BatWings", 480);

	configs.addEMC(getItem(mod, "item_coffee_beans"), "CoffeeBeans", 64);
	configs.addEMC(getItem(mod, "item_food"), 16, "Rice", 64);
	configs.addEMC(getItem(mod, "item_solidified_experience"), "SolidifiedExperience", 863);

    configs.addEMC(getItem(mod, "block_misc"), 3, "BlackQuartzOre", 256);
}

function addMappers(mappers)
{
    mappers.addMapper("LaserEMCMapper");
    mappers.addMapper("EmpowererEMCMapper");
}

// mappers

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import tk.zeitheron.expequiv.api.FakeItem;

function LaserEMCMapper(emc)
{
    ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES.forEach(function(recipe)
    {
        var result = recipe.getOutput();
        if(ItemStack.isEmpty(result)) return;
        var energy = Math.max(25, recipe.getEnergyUsed() / 125);
        var im = Lists.arrayList();
        im.add(FakeItem.create(emc, recipe.getInput(), 1));
        im.add(emc.fake(energy).stack(1));
        emc.map(Ingredient.of(result), im);
    });
}

function EmpowererEMCMapper(emc)
{
    ActuallyAdditionsAPI.EMPOWERER_RECIPES.forEach(function(recipe)
    {
        var result = recipe.getOutput();
        if(ItemStack.isEmpty(result)) return;
        var im = Lists.arrayList();
        im.add(FakeItem.create(emc, recipe.getInput(), 1));
        if(recipe.getStandOne() != null) im.add(FakeItem.create(emc, recipe.getStandOne(), 1));
        if(recipe.getStandTwo() != null) im.add(FakeItem.create(emc, recipe.getStandTwo(), 1));
        if(recipe.getStandThree() != null) im.add(FakeItem.create(emc, recipe.getStandThree(), 1));
        if(recipe.getStandFour() != null) im.add(FakeItem.create(emc, recipe.getStandFour(), 1));
        var energy = Math.max(25, recipe.getEnergyPerStand() * (im.size() - 1) / 125);
        im.add(emc.fake(energy).stack(1));
        emc.map(Ingredient.of(result), im);
    });
}