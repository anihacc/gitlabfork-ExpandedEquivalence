// @Zeitheron

// setup

var mod = "techreborn";

function tweakData()
{
    Data.set("minecraft:ore_rate", Math.max(Data.get("minecraft:ore_rate"), 2));
    Data.set("minecraft:coal_ore_rate", Math.max(Data.get("minecraft:coal_ore_rate"), 3));
    Data.set("minecraft:diamond_ore_rate", Math.max(Data.get("minecraft:diamond_ore_rate"), 2));
    Data.set("minecraft:emerald_ore_rate", Math.max(Data.get("minecraft:emerald_ore_rate"), 2));
    Data.set("minecraft:quartz_ore_rate", Math.max(Data.get("minecraft:quartz_ore_rate"), 4));
}

function registerEMC(configs)
{
	configs.addEMC(getItem(mod, "part"), 31, "Sap", 32);
	configs.addEMC(getItem(mod, "nuggets"), 7, "IridiumNugget", 3968);
}