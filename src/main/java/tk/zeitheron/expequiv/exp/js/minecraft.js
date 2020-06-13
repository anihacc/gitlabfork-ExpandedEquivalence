// @Zeitheron

// setup

function registerEMC(configs)
{
	configs.addEMC(getItem("minecraft", "dragon_breath"), "DragonBreath", "Dragon's Breath", 1025);
	configs.addEMC(getItem("minecraft", "experience_bottle"), "XPBottle", "Bottle o' Enchanting", 864);
	configs.addEMC(getItem("minecraft", "elytra"), "Elytra", 65536);
	configs.addEMC(getItem("minecraft", "totem_of_undying"), "TotemOfUndying", 1024 * 50);
	configs.addEMC(getItem("minecraft", "skull"), 1, "WitherSkeletonSkull", 1024 * 48);
	configs.addEMC(getItem("minecraft", "skull"), 5, "DragonHead", 1024 * 192);
	configs.addEMC(getItem("minecraft", "blaze_powder"), "BlazePowder", 1536 / 5);
}