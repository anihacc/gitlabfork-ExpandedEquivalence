// @Zeitheron

// setup

var mod = "botania";

function registerEMC(configs)
{
    for(var i = 0; i < 16; ++i)
    {
        configs.addEMC(getItem(mod, "flower"), i, "Flower", 16);

        if(i <= 7)
        {
            configs.addEMC(getItem(mod, "doubleflower1"), i, "DoubleFlower", 32);
            configs.addEMC(getItem(mod, "doubleflower2"), i, "DoubleFlower", 32);
        }
    }

    configs.addEMC(getItem(mod, "recordgaia1"), "ScathedMusicDiscEndureEmptiness", 4096);
    configs.addEMC(getItem(mod, "recordgaia2"), "ScathedMusicDiscFightForQuiescence", 4096);
}

function addMappers(mappers)
{
    mappers.addMapper("ManaPoolEMCMapper");
    mappers.addMapper("ElvenTradeEMCMapper");
    mappers.addMapper("PureDaisyEMCMapper");
    mappers.addMapper("TerrestrialEMCMapper");
}

// mappers

import tk.zeitheron.expequiv.api.FakeItem;
import vazkii.botania.api.BotaniaAPI;

function ManaPoolEMCMapper(emc)
{
    BotaniaAPI.manaInfusionRecipes.forEach(function(recipe)
    {
        var input = Ingredient.decode(emc, recipe.getInput());
        if(input)
        {
            var output = ItemStack.copy(recipe.getOutput());
            var mana = emc.fake(Math.ceil(recipe.getManaToConsume() / 10));

            var im = Lists.arrayList();
            im.add(input);
            im.add(mana.stack(1));

            emc.map(Ingredient.of(output), im);
        }
    });
}

function ElvenTradeEMCMapper(emc)
{
    BotaniaAPI.elvenTradeRecipes.forEach(function(recipe)
    {
        var im = Lists.arrayList();
        recipe.getInputs().forEach(function(o)
        {
            var dec = Ingredient.decode(emc, o);
            if(dec) im.add(dec);
        });
        var output = recipe.getOutputs().stream().filter(function(s) { return !ItemStack.isEmpty(s); }).findFirst().orElse(ItemStack.EMPTY);
        if(!ItemStack.isEmpty(output)) emc.map(Ingredient.of(output), im);
    });
}

function PureDaisyEMCMapper(emc)
{
    BotaniaAPI.pureDaisyRecipes.forEach(function(recipe)
    {
        var oi = recipe.getInput();
        var input = Ingredient.decode(emc, oi);
        var state = recipe.getOutputState();
        var output = ItemStack.fromState(state);
        if(input != null && !ItemStack.isEmpty(output)) emc.map(Ingredient.of(output), input);
    });
}

function TerrestrialEMCMapper(emc)
{
import vazkii.botania.common.item.ModItems;
    var out = Ingredient.of(ItemStack.of(ModItems.manaResource, 1, 4));
    var im = Lists.arrayList();
    im.add(emc.fake(Math.ceil(600000 / 10)).stack(1));
    im.add(Ingredient.of(ItemStack.of(ModItems.manaResource, 1, 0)));
    im.add(Ingredient.of(ItemStack.of(ModItems.manaResource, 1, 1)));
    im.add(Ingredient.of(ItemStack.of(ModItems.manaResource, 1, 2)));
    emc.map(out, im);
}