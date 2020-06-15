// @Zeitheron

// setup

var mod = "buildcraftcore";

function addMappers(mappers)
{
    mappers.addMapper("LaserTableEMCMapper");
}

// mappers

import tk.zeitheron.expequiv.api.FakeItem;

function LaserTableEMCMapper(emc)
{
import buildcraft.lib.recipe.AssemblyRecipeRegistry;

    AssemblyRecipeRegistry.REGISTRY.values().forEach(function(recipe)
    {
        var result = recipe.getOutputPreviews().stream().findFirst().orElse(ItemStack.EMPTY);
        if(!ItemStack.isEmpty(result))
        {
            var im = Lists.arrayList();
            im.add(emc.fake(recipe.getRequiredMicroJoulesFor(ItemStack.copy(result)) / 20000000).stack(1));
            recipe.getInputsFor(ItemStack.copy(result)).forEach(function(i)
            {
                if(!Ingredient.isEmpty(i.ingredient))
                    im.add(FakeItem.create(emc, i.count, i.ingredient));
            });
            emc.map(Ingredient.of(result), im);
        }
    });
}