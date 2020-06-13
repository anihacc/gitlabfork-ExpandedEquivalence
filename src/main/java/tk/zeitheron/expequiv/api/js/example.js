/*
* This script is an example code for a module
*/

/*

// This scripting API provides the following structures that can be used:

// in this struct, configKey should be without spaces, and words are going to be separated for config description (ex. ConfigKey => Config Key)
struct Configs
{
    void addEMC(Item item, String configKey, long EMC);

    void addEMC(Item item, int metadata, String configKey, long EMC);
	
    void addEMC(Item item, String configKey, String displayName, long EMC);

    void addEMC(Item item, int metadata, String configKey, String displayName, long EMC);
}

struct EMCMapping
{
    void map(Ingredient output, Ingredient... inputs);

	// EMC per one fake item
    FakeIngredient fake(long EMC);
}

struct FakeIngredient
{
    CountedIngredient stack(int amount);
}

struct Ingredient
{
    static Ingredient of(Item item, int amount);

    static Ingredient of(Item item, int metadata, int amount);
	
	static List<Ingredient> list();
}

/// Static methods that can be used directly in the script:
struct this
{
	static warn(String msg);
	
	static error(String msg);
	
	static Item getItem(String namespace, String key);
}

struct MapperList
{
	void addMapper(String MapperFunc);
}

MapperFunc should be function(EMCMapping)

*/



function registerEMC(configs)
{
	
}

function addMappers(mappers)
{
	
}