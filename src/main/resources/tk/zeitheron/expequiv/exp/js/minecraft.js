// @Zeitheron

// setup

var mod = "minecraft";

function registerEMC(configs)
{
	configs.addEMC(getItem(mod, "dragon_breath"), "DragonBreath", "Dragon's Breath", 1025);
	configs.addEMC(getItem(mod, "experience_bottle"), "XPBottle", "Bottle o' Enchanting", 864);
	configs.addEMC(getItem(mod, "elytra"), "Elytra", 65536);
	configs.addEMC(getItem(mod, "totem_of_undying"), "TotemOfUndying", 1024 * 50);
	configs.addEMC(getItem(mod, "skull"), 1, "WitherSkeletonSkull", 1024 * 48);
	configs.addEMC(getItem(mod, "skull"), 5, "DragonHead", 1024 * 192);
	configs.addEMC(getItem(mod, "blaze_powder"), "BlazePowder", 1536 / 5);
}