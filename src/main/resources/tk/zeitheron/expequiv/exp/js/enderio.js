// @Zeitheron

// setup

var mod = "enderio";

function tweakData()
{
    Data.set("minecraft:ore_rate", Math.max(Data.get("minecraft:ore_rate"), 2));
    Data.set("minecraft:coal_ore_rate", Math.max(Data.get("minecraft:coal_ore_rate"), 3));
    Data.set("minecraft:diamond_ore_rate", Math.max(Data.get("minecraft:diamond_ore_rate"), 2));
    Data.set("minecraft:emerald_ore_rate", Math.max(Data.get("minecraft:emerald_ore_rate"), 2));
    Data.set("minecraft:quartz_ore_rate", Math.max(Data.get("minecraft:quartz_ore_rate"), 2));
}

function registerEMC(configs)
{
    var mat = getItem(mod, "item_material");

	configs.addEMC(mat, 20, "GrainsOfInfinity", 32);
	configs.addEMC(mat, 46, "ClippingsAndTrimmings", 32);
	configs.addEMC(mat, 47, "TwigsAndPrunings", 24);
	configs.addEMC(mat, 17, "EnticingCrystal", 20 * 1024);
	configs.addEMC(mat, 16, "EnderCrystal", 19512 + 2048);
	configs.addEMC(mat, 19, "PrecientCrystal", 19512 + 4096);
	configs.addEMC(mat, 21, "Flour", 24);

	configs.addEMC(getItem(mod, "block_enderman_skull"), "EndermanHead", 64 * 1024);
}

function addMappers(mappers)
{
    mappers.addMapper("AlloySmelterEMCMapper");
    mappers.addMapper("SliceNSpliceEMCMapper");
    mappers.addMapper("SAGEMCMapper");
    mappers.addMapper("VatEMCMapper");
}

function inputToIngredients(emc, inputs)
{
    var ci = Lists.arrayList();
    Java.type("java.util.Arrays").stream(inputs).forEach(function(input)
    {
        if(input.getFluidInput() != null) ci.add(Ingredient.of(input.getFluidInput()));
        var equiv = input.getEquivelentInputs();
        if(equiv && equiv.length > 0) ci.add(Ingredient.of(emc, equiv));
        else if(!ItemStack.isEmpty(input.getInput())) ci.add(Ingredient.of(input.getInput()));
    });
    return ci;
}

// Handle IManyToOneRecipe from EnderIO
function handleMtO(emc, recipes)
{
    recipes.getInstance().getRecipes().forEach(function(recipe)
    {
        emc.map(Ingredient.of(recipe.getOutput()), inputToIngredients(emc, recipe.getInputs()));
    });
}

// mappers

function AlloySmelterEMCMapper(emc)
{
import crazypants.enderio.base.recipe.alloysmelter.AlloyRecipeManager;

    handleMtO(emc, AlloyRecipeManager);
}

function SliceNSpliceEMCMapper(emc)
{
import crazypants.enderio.base.recipe.slicensplice.SliceAndSpliceRecipeManager;

    handleMtO(emc, SliceAndSpliceRecipeManager);
}

function SAGEMCMapper(emc)
{
import crazypants.enderio.base.recipe.sagmill.SagMillRecipeManager;

    SagMillRecipeManager.getInstance().getRecipes().forEach(function(recipe)
    {
        var output = recipe.getOutputs().length == 1 ? recipe.getOutputs()[0] : null;
        if(output == null || output.getChance() < 1) return;
        emc.map(output.getOutput(), inputToIngredients(emc, recipe.getInputs()));
    });
}

function VatEMCMapper(emc)
{
import crazypants.enderio.base.recipe.vat.VatRecipeManager;

    VatRecipeManager.getInstance().getRecipes().forEach(function(recipe)
    {
        var output = recipe.getOutputs().length == 1 ? recipe.getOutputs()[0] : null;
        if(output == null || output.getChance() < 1) return;
        var im = inputToIngredients(emc, recipe.getInputs());
        recipe.getInputFluidStacks().forEach(function(fluid) { im.add(Ingredient.of(fluid)); });
        emc.map(output.getOutput(), im);
    });
}