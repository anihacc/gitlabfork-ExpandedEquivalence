// @Zeitheron

// setup

var mod = "compactmachines3";

function addMappers(mappers)
{
    mappers.addMapper("MiniaturizationEMCMapper");
}

// mappers

import tk.zeitheron.expequiv.api.FakeItem;

function MiniaturizationEMCMapper(emc)
{
import org.dave.compactmachines3.miniaturization.MultiblockRecipes;

    MultiblockRecipes.getRecipes().forEach(function(recipe)
    {
        var result = recipe.getTargetStack();
        if(ItemStack.isEmpty(result)) return;
        var im = Lists.arrayList();
        im.add(Ingredient.of(recipe.getCatalystStack()));
        recipe.getRequiredItemStacks().forEach(function(i) { im.add(Ingredient.of(i)); });
        emc.map(Ingredient.of(result), im);
    });
}