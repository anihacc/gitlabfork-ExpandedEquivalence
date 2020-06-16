// @Zeitheron

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import java.lang.Long;

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

var defaultPotionEMCs =
{
    "minecraft:empty": 0,
    "minecraft:water": 1,
    "minecraft:mundane": 1+32,
    "minecraft:thick": 1+384,
    "minecraft:awkward": 1+24,
    "minecraft:night_vision": 1+24+1880,
    "minecraft:long_night_vision": 1+24+1880+64,
    "minecraft:invisibility": 1+24+1880+192,
    "minecraft:long_invisibility": 1+24+1880+192+64,
    "minecraft:leaping": 1+24+128,
    "minecraft:long_leaping": 1+24+128+64,
    "minecraft:strong_leaping": 1+24+128+384,
    "minecraft:fire_resistance": 1+24+339,
    "minecraft:long_fire_resistance": 1+24+339+64,
    "minecraft:swiftness": 1+24+32,
    "minecraft:long_swiftness": 1+24+32+64,
    "minecraft:strong_swiftness": 1+24+32+384,
    "minecraft:slowness": 1+24+32+192,
    "minecraft:long_slowness": 1+24+32+192+64,
    "minecraft:water_breathing": 1+24+64,
    "minecraft:long_water_breathing": 1+24+64+64,
    "minecraft:healing": 1+24+1832,
    "minecraft:strong_healing": 1+24+1832+384,
    "minecraft:harming": 1+24+128+64+192,
    "minecraft:strong_harming": 1+24+128+64+192+384,
    "minecraft:poison": 1+24+128,
    "minecraft:long_poison": 1+24+128+64,
    "minecraft:strong_poison": 1+24+128+384,
    "minecraft:regeneration": 1+24+4096,
    "minecraft:long_regeneration": 1+24+4096+64,
    "minecraft:strong_regeneration": 1+24+4096+384,
    "minecraft:strength": 1+24+307,
    "minecraft:long_strength": 1+24+307+64,
    "minecraft:strong_strength": 1+24+307+384,
    "minecraft:weakness": 1+192,
    "minecraft:long_weakness": 1+192+64
};

function registerEMC(configs, cfgFile)
{
    var ptypes = Lists.hashMap();
    ForgeRegistries.POTION_TYPES.forEach(function(type)
    {
        var name = type.getRegistryName();
        var defEMC = defaultPotionEMCs[name.toString()];
        if(!defEMC || defEMC < 1) defEMC = 1;
        ptypes.put(type, cfgFile.getCategory("PotionTypes").getLongEntry(name.toString(), defEMC, 0, Long.MAX_VALUE).setDescription("The EMC value for this potion type").getValue());
    });
    Data.set("minecraft:potion_type_costs", ptypes);

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