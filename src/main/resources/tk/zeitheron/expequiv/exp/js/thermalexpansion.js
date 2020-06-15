// @Zeitheron

import tk.zeitheron.expequiv.api.CountedIngredient;
import net.minecraftforge.fluids.FluidStack;

import cofh.thermalfoundation.init.TFFluids;
import cofh.thermalexpansion.util.managers.machine.BrewerManager;
import cofh.thermalexpansion.util.managers.machine.CentrifugeManager;
import cofh.thermalexpansion.util.managers.machine.ChargerManager;
import cofh.thermalexpansion.util.managers.machine.CompactorManager;
import cofh.thermalexpansion.util.managers.machine.CrucibleManager;
import cofh.thermalexpansion.util.managers.machine.EnchanterManager;
import cofh.thermalexpansion.util.managers.machine.ExtruderManager;
import cofh.thermalexpansion.util.managers.machine.FurnaceManager;
import cofh.thermalexpansion.util.managers.machine.InsolatorManager;
import cofh.thermalexpansion.util.managers.machine.PrecipitatorManager;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.RefineryManager;
import cofh.thermalexpansion.util.managers.machine.SawmillManager;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import cofh.thermalexpansion.util.managers.machine.TransposerManager;

// setup

var mod = "thermalexpansion"

function addMappers(mappers)
{
    mappers.addMapper("BrewerEMCMapper");
    mappers.addMapper("CentrifugeEMCMapper");
    mappers.addMapper("ChargerEMCMapper");
    mappers.addMapper("CompactorEMCMapper");
    mappers.addMapper("CrucibleEMCMapper");
    mappers.addMapper("EnchanterEMCMapper");
    mappers.addMapper("ExtruderEMCMapper");
    mappers.addMapper("FurnaceEMCMapper");
    mappers.addMapper("InsolatorEMCMapper");
    mappers.addMapper("PrecipitatorEMCMapper");
    mappers.addMapper("PulverizerEMCMapper");
    mappers.addMapper("RefineryEMCMapper");
    mappers.addMapper("SawmillEMCMapper");
    mappers.addMapper("SmelterEMCMapper");
    mappers.addMapper("TransposerEMCMapper");
}

function $(array) { return Lists.stream(array); }

function inoutr(emc, recipe)
{
    emc.map(Ingredient.of(recipe.getOutput()), Ingredient.of(recipe.getInput()));
}

function inout(emc, list)
{
    $(list.getRecipeList()).forEach(function(recipe) { inoutr(emc, recipe); });
}

// mappers

function BrewerEMCMapper(emc)
{
    $(BrewerManager.getRecipeList()).forEach(function(recipe)
    {
        var im = Lists.arrayList();
        im.add(Ingredient.of(recipe.getInput()));
        im.add(Ingredient.of(recipe.getInputFluid()));
        emc.map(Ingredient.of(recipe.getOutputFluid()), im);
    });
}

function CentrifugeEMCMapper(emc)
{
    $(CentrifugeManager.getRecipeList()).forEach(function(recipe)
    {
        if(recipe.getOutput().size() == 1)
        {
            var everything = emc.fake();

            var im = Lists.arrayList();
            if(recipe.getFluid()) im.add(Ingredient.of(recipe.getFluid()));
            if(!ItemStack.isEmpty(recipe.getInput())) im.add(Ingredient.of(recipe.getInput()));
            emc.map(everything.stack(1), im);

            var chance = recipe.getChance().get(0);
            var mult = Math.round(100 / chance);
            emc.map(recipe.getOutput().get(0), everything.stack(mult));
        }
    });
}

function ChargerEMCMapper(emc)
{
    inout(emc, ChargerManager);
}

function CompactorEMCMapper(emc)
{
    Lists.stream(CompactorManager.Mode.values()).forEach(function(mode)
    {
        $(CompactorManager.getRecipeList(mode)).forEach(function(recipe)
        {
            inoutr(emc, recipe);
        });
    });
}

function CrucibleEMCMapper(emc)
{
    inout(emc, CrucibleManager);
}

function EnchanterEMCMapper(emc)
{
    $(EnchanterManager.getRecipeList()).forEach(function(recipe)
    {
        var im = Lists.arrayList();
        im.add(Ingredient.of(recipe.getPrimaryInput()));
        im.add(Ingredient.of(recipe.getSecondaryInput()));
        emc.map(Ingredient.of(recipe.getOutput()), im);
    });
}

function ExtruderEMCMapper(emc)
{
    var extrude = function(b)
    {
        $(ExtruderManager.getRecipeList(b)).forEach(function(recipe)
        {
            var im = Lists.arrayList();
            im.add(Ingredient.of(recipe.getInputCold()));
            im.add(Ingredient.of(recipe.getInputHot()));
            emc.map(Ingredient.of(recipe.getOutput()), im);
        });
    }

    extrude(true);
    extrude(false);
}

function FurnaceEMCMapper(emc)
{
    $(FurnaceManager.getRecipeList(false)).forEach(function(recipe)
    {
        inoutr(emc, recipe);
    });

    $(FurnaceManager.getRecipeList(true)).forEach(function(recipe)
    {
        var im = Lists.arrayList();
        im.add(Ingredient.of(recipe.getInput()));
        im.add(Ingredient.of(new FluidStack(TFFluids.fluidPyrotheum, recipe.getCreosote())));
        emc.map(Ingredient.of(recipe.getOutput()), im);
    });
}

function InsolatorEMCMapper(emc)
{
    $(InsolatorManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getPrimaryOutput()), Ingredient.of(recipe.getPrimaryInput()));
    });
}

function PrecipitatorEMCMapper(emc)
{
    inout(emc, PrecipitatorManager);
}

function PulverizerEMCMapper(emc)
{
    $(PulverizerManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getPrimaryOutput()), Ingredient.of(recipe.getInput()));
    });
}

function RefineryEMCMapper(emc)
{
    $(RefineryManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getOutputFluid()), Ingredient.of(recipe.getInput()));
    });
}

function SawmillEMCMapper(emc)
{
    $(SawmillManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getPrimaryOutput()), Ingredient.of(recipe.getInput()));
    });
}

function SmelterEMCMapper(emc)
{
    $(SmelterManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getPrimaryOutput()), Ingredient.of(recipe.getPrimaryInput()));
    });
}

function TransposerEMCMapper(emc)
{
    $(TransposerManager.getFillRecipeList()).forEach(function(recipe)
    {
        var im = Lists.arrayList();
        im.add(Ingredient.of(recipe.getInput()));
        im.add(Ingredient.of(recipe.getFluid()));
        emc.map(Ingredient.of(recipe.getOutput()), im);
    });

    $(TransposerManager.getExtractRecipeList()).forEach(function(recipe)
    {
        var im = Lists.arrayList();
        im.add(Ingredient.of(recipe.getInput()));
        im.add(CountedIngredient.create(recipe.getOutput(), -1));
        emc.map(Ingredient.of(recipe.getFluid()), im);
    });
}