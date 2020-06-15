// @Zeitheron

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;

// setup

function registerEMC(configs)
{
	
}

function addMappers(mappers)
{
	mappers.addMapper("TableEMCMapper");
	mappers.addMapper("EnderCrafterEMCMapper");
	mappers.addMapper("CombinationEMCMapper");
	mappers.addMapper("CompressorEMCMapper");
}

function io(emc, type)
{
    type.getInstance().getRecipes().forEach(function(recipe) { Vanilla.handleIRecipe(emc, recipe); });
}

// mappers

function TableEMCMapper(emc)
{
    io(emc, TableRecipeManager);
}

function EnderCrafterEMCMapper(emc)
{
    io(emc, EnderCrafterRecipeManager);
}

function CombinationEMCMapper(emc)
{
    CombinationRecipeManager.getInstance().getRecipes().forEach(function(recipe)
    {
        var result = recipe.getOutput();
        if(ItemStack.isEmpty(result)) return;
        var im = Lists.arrayList();
        Ingredient.add(Ingredient.of(recipe.getInput()), im);
        recipe.getPedestalItems().forEach(function(pedestal) { Ingredient.add(Ingredient.decode(pedestal), im); });
        emc.map(Ingredient.of(result), im);
    });
}

function CompressorEMCMapper(emc)
{
    CompressorRecipeManager.getInstance().getRecipes().forEach(function(recipe)
    {
        var result = recipe.getOutput();
        if(ItemStack.isEmpty(result)) return;
        var im = Lists.arrayList();
        var input = Ingredient.decode(recipe.getInput());
        if(input) im.add(input.stack(recipe.getInputCount()));
        if(recipe.consumeCatalyst())
        {
            var catal = recipe.getCatalyst();
            if(!ItemStack.isEmpty(catal)) im.add(Ingredient.of(catal));
        }
        im.add(emc.fake(recipe.getPowerCost() / 125).stack(1));
        emc.map(Ingredient.of(result), im);
    });
}