// @Zeitheron

// setup

var mod = "appliedenergistics2";

function registerEMC(configs)
{
import appeng.api.AEApi;

	configs.addEMC(AEApi.instance().definitions().materials().certusQuartzCrystalCharged().maybeStack(1).orElse(ItemStack.EMPTY), "ChargedCertusQuartz", 64);
	configs.addEMC(AEApi.instance().definitions().materials().matterBall().maybeItem().orElse(null), 6, "MatterBall", 256);
	configs.addEMC(AEApi.instance().definitions().materials().purifiedCertusQuartzCrystal().maybeStack(1).orElse(ItemStack.EMPTY), "PurifiedCertusQuartz", 32);
	configs.addEMC(AEApi.instance().definitions().materials().purifiedNetherQuartzCrystal().maybeStack(1).orElse(ItemStack.EMPTY), "PurifiedNetherQuartz", 128);
	configs.addEMC(AEApi.instance().definitions().materials().purifiedFluixCrystal().maybeStack(1).orElse(ItemStack.EMPTY), "PurifiedFluixCrystal", 128);
	configs.addEMC(AEApi.instance().definitions().materials().singularity().maybeItem().orElse(null), 47, "Singularity", 256000);
	configs.addEMC(AEApi.instance().definitions().blocks().skyStoneBlock().maybeItem().orElse(null), "SkyStone", 64)
}

function addMappers(mappers)
{
    mappers.addMapper("GrinderEMCConverter");
    mappers.addMapper("InscriberEMCConverter");
}

// mappers

import it.unimi.dsi.fastutil.ints.IntArrayList;

function GrinderEMCConverter(emc)
{
import appeng.api.AEApi;

    AEApi.instance().registries().grinder().getRecipes().forEach(function(recipe)
    {
        var input = recipe.getInput();
        var output = recipe.getOutput();
        emc.map(Ingredient.of(input), Ingredient.of(output));
    });
}

function InscriberEMCConverter(emc)
{
import appeng.api.AEApi;

    var material = getItem(mod, "material");
    var presses = Lists.intList();
    presses.add(13);
    presses.add(14);
    presses.add(15);
    presses.add(19);
    AEApi.instance().registries().inscriber().getRecipes().forEach(function(recipe)
    {
        var im = Lists.arrayList();
        var inputs = Lists.arrayList();
        inputs.add(recipe.getInputs().get(0));
        inputs.add(recipe.getTopOptional().orElse(ItemStack.EMPTY));
        inputs.add(recipe.getBottomOptional().orElse(ItemStack.EMPTY));
        inputs.forEach(function(i)
        {
            var press = !ItemStack.isEmpty(i) && ItemStack.getItem(i) == material && presses.contains(ItemStack.getMetadata(i));
            if(!ItemStack.isEmpty(i) && !press) im.add(Ingredient.of(i));
        });
        var out = recipe.getOutput();
        if(!ItemStack.isEmpty(out)) emc.map(Ingredient.of(out), im);
    });
}