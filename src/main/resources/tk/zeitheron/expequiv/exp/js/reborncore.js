// @Zeitheron

import reborncore.api.recipe.RecipeHandler;
import reborncore.common.recipes.RecipeTranslator;

// setup

var mod = "reborncore"

function registerEMC(configs, cfgFile)
{
    Data.set("reborncore:recipe_types", cfgFile);
}

function addMappers(mappers)
{
    mappers.addMapper("RebornCoreEMCConverter");
}

// mappers

function RebornCoreEMCConverter(emc)
{
    var map = Lists.hashMap();
    var cfgFile = Data.get("reborncore:recipe_types");

    RecipeHandler.recipeList.forEach(function(recipe)
    {
        var type = recipe.getRecipeName();
        if(map.containsKey(type))
        {
            if(!map.get(type)) return;
        } else
        {
            map.put(type, cfgFile.getCategory("Recipe Types").getBooleanEntry(type, true).setDescription("Enable this recipe type?").getValue());
            if(!map.get(type)) return;
        }

        var inputs = Lists.arrayList(recipe.getInputs());
        var outputs = Lists.arrayList(recipe.getOutputs());
        if(!inputs || !outputs || inputs.isEmpty() || outputs.isEmpty()) return;

        var om = Lists.arrayList();
        var im = Lists.arrayList();

        inputs.forEach(function(i)
        {
            var stack = RecipeTranslator.getStackFromObject(i);
            if(!ItemStack.isEmpty(stack)) im.add(Ingredient.of(stack));
        });

        outputs.forEach(function(o)
        {
            om.add(Ingredient.of(o));
        });

        emc.multiMap(om, im);
    });

    if(cfgFile.hasChanged()) cfgFile.save();
}