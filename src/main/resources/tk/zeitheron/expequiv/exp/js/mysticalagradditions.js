// @Zeitheron

import com.blakebr0.mysticalagradditions.lib.CropType;

// setup

var mod = "mysticalagradditions"

function registerEMC(configs)
{
    var stuff = getItem(mod, "stuff");
	configs.addEMC(stuff, 1, "WitheringSoul", 2048);
	configs.addEMC(stuff, 3, "DragonScale", 6144);
}

function addMappers(mappers)
{
    mappers.addMapper("CropEMCConverter");
}

// mappers

function CropEMCConverter(emc)
{
    var stuff = getItem(mod, "stuff");

    emc.map(Ingredient.of(ItemStack.of(stuff, 3, 0)), Ingredient.of(ItemStack.of(getItem("minecraft", "nether_star"))));
    emc.map(Ingredient.of(ItemStack.of(stuff, 3, 2)), Ingredient.of(ItemStack.of(getItem("minecraft", "dragon_egg"))));

    if(CropType.Type.NETHER_STAR.isEnabled())
        emc.map(Ingredient.of(ItemStack.of(CropType.Type.NETHER_STAR.getCrop(), 9)), Ingredient.of(ItemStack.of(stuff, 1, 0)));

    if(CropType.Type.DRAGON_EGG.isEnabled())
        emc.map(Ingredient.of(ItemStack.of(CropType.Type.DRAGON_EGG.getCrop(), 9)), Ingredient.of(ItemStack.of(stuff, 1, 2)));

    if(CropType.Type.AWAKENED_DRACONIUM.isEnabled())
    {
        var deNugget = getItem("draconicevolution", "nugget");
        emc.map(Ingredient.of(ItemStack.of(CropType.Type.AWAKENED_DRACONIUM.getCrop(), 9)), Ingredient.of(ItemStack.of(deNugget, 3, 1)));
    }
}