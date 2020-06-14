// @Zeitheron

import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;

// setup

var mod = "mysticalagriculture"

function registerEMC(configs)
{
    var crafting = getItem(mod, "crafting");
	configs.addEMC(crafting, 0, "InferiumEssence", 8);
	configs.addEMC(crafting, 1, "PrudentiumEssence", 8 * 4);
	configs.addEMC(crafting, 2, "IntermediumEssence", 8 * 16);
	configs.addEMC(crafting, 3, "SuperiumEssence", 8 * 64);
	configs.addEMC(crafting, 4, "SupremiumEssence", 8 * 256);
	configs.addEMC(crafting, 5, "ProsperityShard", 64);

	configs.addEMC(getItem(mod, "fertilized_essence"), "FertilizedEssence", 96);
	configs.addEMC(getItem(mod, "chunk"), "MobChunk", 1024);
}

function addMappers(mappers)
{
    mappers.addMapper("SeedReprocessorEMCConverter");
}

// mappers

function SeedReprocessorEMCConverter(emc)
{
    ReprocessorManager.getRecipes().forEach(function(recipe)
    {
        var result = recipe.getOutput();
        if(ItemStack.isEmpty(result)) return;
        var inp = Ingredient.decode(recipe.getInput());
        if(inp) emc.map(Ingredient.of(result), inp);
        else warn("Skipped seed reprocessor recipe with result item " + result);
    });
}