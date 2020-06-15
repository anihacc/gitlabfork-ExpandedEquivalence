// @Zeitheron

import sonar.calculator.mod.common.recipes.AlgorithmSeparatorRecipes;
import sonar.calculator.mod.common.recipes.AtomicCalculatorRecipes;
import sonar.calculator.mod.common.recipes.ConductorMastRecipes;
import sonar.calculator.mod.common.recipes.CalculatorRecipes;
import sonar.calculator.mod.common.recipes.ExtractionChamberRecipes;
import sonar.calculator.mod.common.recipes.FabricationChamberRecipes;
import sonar.calculator.mod.common.recipes.FlawlessCalculatorRecipes;
import sonar.calculator.mod.common.recipes.PrecisionChamberRecipes;
import sonar.calculator.mod.common.recipes.ProcessingChamberRecipes;
import sonar.calculator.mod.common.recipes.ReassemblyChamberRecipes;
import sonar.calculator.mod.common.recipes.RestorationChamberRecipes;
import sonar.calculator.mod.common.recipes.ScientificRecipes;
import sonar.calculator.mod.common.recipes.StoneSeparatorRecipes;

// setup

var mod = "calculator";

function addMappers(mappers)
{
    mappers.addMapper("AlgorithmSeparatorMapper");
    mappers.addMapper("AtomicCalculatorMapper");
    mappers.addMapper("ConductorMastMapper");
    mappers.addMapper("CalculatorMapper");
    mappers.addMapper("ExtractionChamberMapper");
    mappers.addMapper("FabricationChamberMapper");
    mappers.addMapper("FlawlessCalculatorMapper");
    mappers.addMapper("PrecisionChamberMapper");
    mappers.addMapper("ProcessingChamberMapper");
    mappers.addMapper("ReassemblyChamberMapper");
    mappers.addMapper("RestorationChamberMapper");
    mappers.addMapper("ScientificCalculatorMapper");
    mappers.addMapper("StoneSeparatorMapper");
}

function defSonarRecipe(emc, recipe)
{
    var inputs = Lists.arrayList();

    recipe.inputs().forEach(function(input)
    {
        var jei = input.getJEIValue();
        if(jei.size() == 1) inputs.add(Ingredient.of(ItemStack.copy(jei.get(0))));
        else if(!jei.isEmpty()) jei.forEach(function(stack) { inputs.add(Ingredient.of(ItemStack.copy(stack))); });
    });

    var outputs = Lists.arrayList();

    recipe.outputs().forEach(function(output)
    {
        output.getJEIValue().forEach(function(stack)
        {
            var i = Ingredient.of(ItemStack.copy(stack));
            if(i) outputs.add(i);
        })
    });

    emc.multiMap(outputs, inputs);
}

function dsr(emc, from)
{
    from.instance().getRecipes().forEach(function(recipe) { defSonarRecipe(emc, recipe); });
}

// mappers

function AlgorithmSeparatorMapper(emc)
{
    dsr(emc, AlgorithmSeparatorRecipes);
}

function AtomicCalculatorMapper(emc)
{
    dsr(emc, AtomicCalculatorRecipes);
}

function ConductorMastMapper(emc)
{
    dsr(emc, ConductorMastRecipes);
}

function CalculatorMapper(emc)
{
    dsr(emc, CalculatorRecipes);
}

function ExtractionChamberMapper(emc)
{
    dsr(emc, ExtractionChamberRecipes);
}

function FabricationChamberMapper(emc)
{
    dsr(emc, FabricationChamberRecipes);
}

function FlawlessCalculatorMapper(emc)
{
    dsr(emc, FlawlessCalculatorRecipes);
}

function PrecisionChamberMapper(emc)
{
    dsr(emc, PrecisionChamberRecipes);
}

function ProcessingChamberMapper(emc)
{
    dsr(emc, ProcessingChamberRecipes);
}

function ReassemblyChamberMapper(emc)
{
    dsr(emc, ReassemblyChamberRecipes);
}

function RestorationChamberMapper(emc)
{
    dsr(emc, RestorationChamberRecipes);
}

function ScientificCalculatorMapper(emc)
{
    dsr(emc, ScientificRecipes);
}

function StoneSeparatorMapper(emc)
{
    dsr(emc, StoneSeparatorRecipes);
}