// @Zeitheron

// setup

var mod = "thermalfoundation";

function registerEMC(configs)
{
    var material = getItem(mod, "material");

	configs.addEMC(material, 167, "EnderiumIngot", 1280);
	configs.addEMC(material, 864, "Slag", 16);
	configs.addEMC(material, 865, "RichSlag", 128);
	configs.addEMC(material, 866, "Cinnabar", 512);
	configs.addEMC(material, 892, "Bitumen", 256);
	configs.addEMC(material, 2048, "BlizzRod", 1536);
	configs.addEMC(material, 2049, "BlizzPowder", 1536 / 4);
	configs.addEMC(material, 2050, "BlitzRod", 1536);
	configs.addEMC(material, 2051, "BlitzPowder", 1536 / 4);
	configs.addEMC(material, 2052, "BasalzRod", 1536);
	configs.addEMC(material, 2053, "BasalzPowder", 1536 / 4);
}