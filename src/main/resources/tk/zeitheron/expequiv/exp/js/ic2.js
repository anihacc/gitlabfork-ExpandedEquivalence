// @Zeitheron

// setup

var mod = "ic2";

function tweakData()
{
    Data.set("minecraft:ore_rate", Math.max(Data.get("minecraft:ore_rate"), 2));
    Data.set("minecraft:coal_ore_rate", Math.max(Data.get("minecraft:coal_ore_rate"), 2));
    Data.set("minecraft:diamond_ore_rate", Math.max(Data.get("minecraft:diamond_ore_rate"), 2));
    Data.set("minecraft:emerald_ore_rate", Math.max(Data.get("minecraft:emerald_ore_rate"), 2));
    Data.set("minecraft:quartz_ore_rate", Math.max(Data.get("minecraft:quartz_ore_rate"), 2));
}

function registerEMC(configs)
{
    var resourceBlock = getItem(mod, "resource");
    var ingot = getItem(mod, "ingot");
    var nuclear = getItem(mod, "nuclear");
    var dust = getItem(mod, "dust");
    var misc_resource = getItem(mod, "misc_resource");

    configs.registerEMC(ingot, 5, 320);
    configs.addEMC(misc_resource, 1, "IridiumOre", 32 * 1024);

	configs.registerEMC(nuclear, 2, 4096);
	configs.registerEMC(nuclear, 5, 4096);

	configs.registerEMC(dust, 0, 160);
	configs.registerEMC(dust, 1, 32);
	configs.registerEMC(dust, 4, 128);
	configs.registerEMC(dust, 5, 8192);
	configs.registerEMC(dust, 7, 2048);
	configs.registerEMC(dust, 8, 256);
	configs.registerEMC(dust, 9, 864);
	configs.registerEMC(dust, 10, 512);
	configs.registerEMC(dust, 12, 64);
	configs.registerEMC(dust, 14, 512);
	configs.registerEMC(dust, 15, 1);
	configs.registerEMC(dust, 17, 256);
	configs.registerEMC(dust, 24, 256 * 2);
}

function addMappers(mappers)
{
    mappers.addMapper("AdvRecipeEMCMapper");
    mappers.addMapper("DefaultIC2MachineEMCMapper");
    mappers.addMapper("ElectrolyzerEMCMapper");
    mappers.addMapper("FermenterEMCMapper");
}

import tk.zeitheron.expequiv.api.FakeItem;

function processDM(emc, registry)
{
    registry.getRecipes().forEach(function(recipe)
    {
        if(recipe.getOutput().size() == 1)
        {
            var result = recipe.getOutput().stream().findFirst().orElse(ItemStack.EMPTY);
            if(ItemStack.isEmpty(result)) return;
            var input = recipe.getInput();
            emc.map(Ingredient.of(result), FakeItem.create(emc, input.getAmount(), input.getIngredient()));
        }
    });
}

// mappers

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import ic2.api.recipe.Recipes;
import ic2.core.recipe.AdvRecipe;
import ic2.core.recipe.AdvShapelessRecipe;

function AdvRecipeEMCMapper(emc)
{
    Vanilla.getCraftingRecipes().forEach(function(recipe)
    {
        if(AdvRecipe.class.isAssignableFrom(recipe.getClass()) || AdvShapelessRecipe.class.isAssignableFrom(recipe.getClass()))
        {
            var result = Vanilla.getRecipeOutput(recipe);
            var ingredints = Vanilla.getIngredients(recipe);
            if(ItemStack.isEmpty(result)) return;
            var im = Lists.arrayList();
            ingredints.forEach(function(i) { if(!Ingredient.isEmpty(i)) im.add(Ingredient.of(i)); });
            emc.map(Ingredient.of(result), im);
        }
    });
}

function DefaultIC2MachineEMCMapper(emc)
{
    processDM(emc, Recipes.macerator);
    processDM(emc, Recipes.blastfurnace);
    processDM(emc, Recipes.blockcutter);
    processDM(emc, Recipes.centrifuge);
    processDM(emc, Recipes.compressor);
    processDM(emc, Recipes.extractor);
    processDM(emc, Recipes.metalformerCutting);
    processDM(emc, Recipes.metalformerExtruding);
    processDM(emc, Recipes.metalformerRolling);
    processDM(emc, Recipes.oreWashing);
}

function ElectrolyzerEMCMapper(emc)
{
    Recipes.electrolyzer.getRecipeMap().forEach(function(input, recipe)
    {
        input = FluidRegistry.getFluid(input);
        if(!input) return;

        var im = Lists.arrayList();
        Lists.stream(recipe.outputs).forEach(function(output)
        {
            var outFluid = output.getOutput();
            if(!outFluid) return;
            im.add(Ingredient.of(outFluid));
        });

        emc.map(Ingredient.merge(im), Ingredient.of(new FluidStack(input, recipe.inputAmount)));
    });
}

function FermenterEMCMapper(emc)
{
    Recipes.fermenter.getRecipeMap().forEach(function(input, recipe)
    {
        input = FluidRegistry.getFluid(input);
        if(!input) return;

        var result = FluidRegistry.getFluid(recipe.output);
        if(!result) return;

        emc.map(Ingredient.of(new FluidStack(result, recipe.outputAmount)), Ingredient.of(new FluidStack(input, recipe.inputAmount)));
    });
}