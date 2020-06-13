// @Zeitheron

// setup

var mod = "basemetals";

function registerEMC(configs)
{
	configs.addEMC(getItem(mod, "adamantine_ingot"), "AdamantineIngot", 2048);
	configs.addEMC(getItem(mod, "antimony_ingot"), "AntimonyIngot", 1024);
	configs.addEMC(getItem(mod, "aquarium_ingot"), "AquariumIngot", 1024);
	configs.addEMC(getItem(mod, "bismuth_ingot"), "BismuthIngot", 1024);
	configs.addEMC(getItem(mod, "brass_ingot"), "BrassIngot", (480 + 128 * 3) / 4);
	configs.addEMC(getItem(mod, "coldiron_ingot"), "ColdIronIngot", 256);
	configs.addEMC(getItem(mod, "cupronickel_ingot"), "CupronickelIngot", (1024 + 128 * 3) / 4);
	configs.addEMC(getItem(mod, "mercury_ingot"), "LiquidMercury", 256);
	configs.addEMC(getItem(mod, "mithril_ingot"), "MithrilIngot", 1024);
	configs.addEMC(getItem(mod, "pewter_ingot"), "PewterIngot", 512);
	configs.addEMC(getItem(mod, "starsteel_ingot"), "StarSteelIngot", 2048);
	configs.addEMC(getItem(mod, "steel_ingot"), "SteelIngot", 320);
	configs.addEMC(getItem(mod, "zinc_ingot"), "ZincIngot", 480);
}