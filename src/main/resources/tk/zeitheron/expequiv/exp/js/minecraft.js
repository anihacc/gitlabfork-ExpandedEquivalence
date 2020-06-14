// @Zeitheron

// setup

var mod = "minecraft";

function setupData()
{
    Data.setup("minecraft:ore_rate", 1); // How many ingots/gems can be obtained from ore. Directly affects EMC values for ores
    Data.setup("minecraft:coal_ore_rate", 1);
    Data.setup("minecraft:diamond_ore_rate", 1);
    Data.setup("minecraft:emerald_ore_rate", 1);
    Data.setup("minecraft:quartz_ore_rate", 1);
}

/*
function tweakData()
{
    Data.set("minecraft:ore_rate", Math.max(Data.get("minecraft:ore_rate"), 2)); // ore doubling
}
*/

function registerEMC(configs)
{
	configs.addEMC(getItem(mod, "dragon_breath"), "DragonBreath", "Dragon's Breath", 1025);
	configs.addEMC(getItem(mod, "experience_bottle"), "XPBottle", "Bottle o' Enchanting", 864);
	configs.addEMC(getItem(mod, "elytra"), "Elytra", 65536);
	configs.addEMC(getItem(mod, "totem_of_undying"), "TotemOfUndying", 1024 * 50);
	configs.addEMC(getItem(mod, "skull"), 1, "WitherSkeletonSkull", 1024 * 48);
	configs.addEMC(getItem(mod, "skull"), 5, "DragonHead", 1024 * 192);
	configs.addEMC(getItem(mod, "blaze_powder"), "BlazePowder", 1536 / 5);
	
    var rate = Math.max(1, Data.get("minecraft:ore_rate"));
    var coalRate = Math.max(1, Data.get("minecraft:coal_ore_rate"));
    var diamondRate = Math.max(1, Data.get("minecraft:diamond_ore_rate"));
    var emeraldRate = Math.max(1, Data.get("minecraft:emerald_ore_rate"));
    var quartzRate = Math.max(1, Data.get("minecraft:quartz_ore_rate"));
    
    configs.registerEMC(getItem(mod, "gold_ore"), 2048 * rate);
    configs.registerEMC(getItem(mod, "iron_ore"), 256 * rate);
    configs.registerEMC(getItem(mod, "coal_ore"), 128 * coalRate);
    configs.registerEMC(getItem(mod, "diamond_ore"), 8192 * diamondRate);
    configs.registerEMC(getItem(mod, "emerald_ore"), 16 * 1024 * emeraldRate);
    configs.registerEMC(getItem(mod, "quartz_ore"), 256 * quartzRate);
}