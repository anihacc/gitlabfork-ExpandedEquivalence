// @Zeitheron

// setup

var mod = "immersiveengineering";

function tweakData()
{
    Data.set("minecraft:ore_rate", Math.max(Data.get("minecraft:ore_rate"), 2));
    Data.set("minecraft:coal_ore_rate", Math.max(Data.get("minecraft:coal_ore_rate"), 4));
    Data.set("minecraft:diamond_ore_rate", Math.max(Data.get("minecraft:diamond_ore_rate"), 2));
    Data.set("minecraft:emerald_ore_rate", Math.max(Data.get("minecraft:emerald_ore_rate"), 2));
    Data.set("minecraft:quartz_ore_rate", Math.max(Data.get("minecraft:quartz_ore_rate"), 3));
}

function registerEMC(configs)
{
    configs.addEMC(getItem(mod, "material"), 4, "IndustrialHempFiber", 24);
    configs.addEMC(getItem(mod, "material"), 7, "Slag", 16);
    configs.addEMC(getItem(mod, "material"), 17, "CokeDust", 128);
    configs.addEMC(getItem(mod, "material"), 24, "Nitrate", 32);
    configs.addEMC(getItem(mod, "material"), 18, "HOPGraphite", "HOP Graphite", 128 * 8);
    configs.addEMC(getItem(mod, "material"), 19, "HOPGraphite", "HOP Graphite", 128 * 8);
    configs.addEMC(getItem(mod, "material"), 24, "Nitrate", 32);
    configs.addEMC(getItem(mod, "treated_wood"), 0, "TreatedWood", 12);
}

function addMappers(mappers)
{
    mappers.addMapper("AlloyEMCMapper");
    mappers.addMapper("ArcEMCConverter");
    mappers.addMapper("BlastFurnaceEMCConverter");
    mappers.addMapper("BlueprintEMCConverter");
    mappers.addMapper("CokeOvenEMCConverter");
    mappers.addMapper("CrusherEMCConverter");
    mappers.addMapper("MetalPressEMCConverter");
    mappers.addMapper("MixerEMCConverter");
    mappers.addMapper("RefineryEMCMapper");
    mappers.addMapper("SqueezerEMCConverter");
}

function IEStackToIngr(i)
{
    if(i.stackList && !i.stackList.isEmpty()) return Ingredient.of(i.stackList).stack(i.inputSize);
    if(i.oreName && i.oreName.length() > 0) return Ingredient.decode(oreName).stack(i.inputSize);
    if(i.fluid) return Ingredient.of(fluid);
    if(!ItemStack.isEmpty(i.stack)) return Ingredient.of(i.stack);
}

function IEStackToIngrList(i, list)
{
    i = IEStackToIngr(i);
    if(i) list.add(i);
}

// mappers

function AlloyEMCMapper(emc)
{
import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;

    AlloyRecipe.recipeList.forEach(function(recipe)
    {
        var result = recipe.output;
        if(ItemStack.isEmpty(result)) return;
        var im = Lists.arrayList();
        if(recipe.input0) IEStackToIngrList(recipe.input0, im);
        if(recipe.input1) IEStackToIngrList(recipe.input1, im);
        emc.map(Ingredient.of(result), im);
    });
}

function ArcEMCConverter(emc)
{
import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;

    ArcFurnaceRecipe.recipeList.forEach(function(recipe)
    {
        var result = recipe.output;
        if(ItemStack.isEmpty(result)) return;
        var input = IEStackToIngr(recipe.input);
        if(input) emc.map(Ingredient.of(result), input);
    });
}

function BlastFurnaceEMCConverter(emc)
{
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;

    BlastFurnaceRecipe.recipeList.forEach(function(recipe)
    {
        var result = recipe.output;
        if(ItemStack.isEmpty(result)) return;
        var input = Ingredient.decode(recipe.input);
        if(input) emc.map(Ingredient.of(result), input);
    });
}

function BlueprintEMCConverter(emc)
{
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;

    BlueprintCraftingRecipe.recipeList.values().forEach(function(recipe)
    {
        var result = recipe.output;
        if(ItemStack.isEmpty(result)) return;
        var im = Lists.arrayList();
        Lists.stream(recipe.inputs).forEach(function(input) { IEStackToIngrList(input, im); });
        emc.map(Ingredient.of(result), im);
    });
}

function CokeOvenEMCConverter(emc)
{
import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;

    CokeOvenRecipe.recipeList.forEach(function(recipe)
    {
        var result = recipe.output;
        if(ItemStack.isEmpty(result)) return;
        var input = Ingredient.decode(recipe.input);
        if(input) emc.map(Ingredient.of(result), input);
    });
}

function CrusherEMCConverter(emc)
{
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;

    CrusherRecipe.recipeList.forEach(function(recipe)
    {
        var result = recipe.output;
        if(ItemStack.isEmpty(result)) return;
        var input = IEStackToIngr(recipe.input);
        if(input) emc.map(Ingredient.of(result), input);
    });
}

function MetalPressEMCConverter(emc)
{
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;

    MetalPressRecipe.recipeList.values().forEach(function(recipe)
    {
        var result = recipe.output;
        if(ItemStack.isEmpty(result)) return;
        var input = IEStackToIngr(recipe.input);
        if(input) emc.map(Ingredient.of(result), input);
    });
}

function MixerEMCConverter(emc)
{
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;

    MixerRecipe.recipeList.forEach(function(recipe)
    {
        var result = recipe.fluidOutput;
        var im = Lists.arrayList();
        im.add(Ingredient.of(recipe.fluidInput));
        Lists.stream(recipe.itemInputs).forEach(function(input) { IEStackToIngrList(input, im); });
        emc.map(Ingredient.of(result), im);
    });
}

function RefineryEMCMapper(emc)
{
import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;

    RefineryRecipe.recipeList.forEach(function(recipe)
    {
        var result = recipe.output;
        if(!result) return;
        var im = Lists.arrayList();
        if(recipe.input0) im.add(Ingredient.of(recipe.input0));
        if(recipe.input1) im.add(Ingredient.of(recipe.input1));
        emc.map(Ingredient.of(result), im);
    });
}

function SqueezerEMCConverter(emc)
{
import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;

    SqueezerRecipe.recipeList.forEach(function(recipe)
    {
        var input = IEStackToIngr(recipe.input);
        if(input)
        {
            if(!ItemStack.isEmpty(recipe.itemOutput)) emc.map(Ingredient.of(recipe.itemOutput), input);
            else if(recipe.fluidOutput) emc.map(Ingredient.of(recipe.fluidOutput), input);
        }
    });
}