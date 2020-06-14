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
    mappers.addMapper("BrewerEMCConverter");
    mappers.addMapper("CentrifugeEMCConverter");
    mappers.addMapper("ChargerEMCConverter");
    mappers.addMapper("CompactorEMCConverter");
    mappers.addMapper("CrucibleEMCConverter");
    mappers.addMapper("EnchanterEMCConverter");
    mappers.addMapper("ExtruderEMCConverter");
    mappers.addMapper("FurnaceEMCConverter");
    mappers.addMapper("InsolatorEMCConverter");
    mappers.addMapper("PrecipitatorEMCConverter");
    mappers.addMapper("PulverizerEMCConverter");
    mappers.addMapper("RefineryEMCConverter");
    mappers.addMapper("SawmillEMCConverter");
    mappers.addMapper("SmelterEMCConverter");
    mappers.addMapper("TransposerEMCConverter");
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

function BrewerEMCConverter(emc)
{
    $(BrewerManager.getRecipeList()).forEach(function(recipe)
    {
        var im = Lists.arrayList();
        im.add(Ingredient.of(recipe.getInput()));
        im.add(Ingredient.of(recipe.getInputFluid()));
        emc.map(Ingredient.of(recipe.getOutputFluid()), im);
    });
}

function CentrifugeEMCConverter(emc)
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

function ChargerEMCConverter(emc)
{
    inout(emc, ChargerManager);
}

function CompactorEMCConverter(emc)
{
    Lists.stream(CompactorManager.Mode.values()).forEach(function(mode)
    {
        $(CompactorManager.getRecipeList(mode)).forEach(function(recipe)
        {
            inoutr(emc, recipe);
        });
    });
}

function CrucibleEMCConverter(emc)
{
    inout(emc, CrucibleManager);
}

function EnchanterEMCConverter(emc)
{
    $(EnchanterManager.getRecipeList()).forEach(function(recipe)
    {
        var im = Lists.arrayList();
        im.add(Ingredient.of(recipe.getPrimaryInput()));
        im.add(Ingredient.of(recipe.getSecondaryInput()));
        emc.map(Ingredient.of(recipe.getOutput()), im);
    });
}

function ExtruderEMCConverter(emc)
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

function FurnaceEMCConverter(emc)
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

function InsolatorEMCConverter(emc)
{
    $(InsolatorManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getPrimaryOutput()), Ingredient.of(recipe.getPrimaryInput()));
    });
}

function PrecipitatorEMCConverter(emc)
{
    inout(emc, PrecipitatorManager);
}

function PulverizerEMCConverter(emc)
{
    $(PulverizerManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getPrimaryOutput()), Ingredient.of(recipe.getInput()));
    });
}

function RefineryEMCConverter(emc)
{
    $(RefineryManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getOutputFluid()), Ingredient.of(recipe.getInput()));
    });
}

function SawmillEMCConverter(emc)
{
    $(SawmillManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getPrimaryOutput()), Ingredient.of(recipe.getInput()));
    });
}

function SmelterEMCConverter(emc)
{
    $(SmelterManager.getRecipeList()).forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getPrimaryOutput()), Ingredient.of(recipe.getPrimaryInput()));
    });
}

function TransposerEMCConverter(emc)
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