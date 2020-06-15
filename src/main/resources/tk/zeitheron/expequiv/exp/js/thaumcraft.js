// @Zeitheron

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.IThaumcraftRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import java.lang.Integer;
var NativeItemStack = Java.type("net.minecraft.item.ItemStack");

// setup

var mod = "thaumcraft";

function registerEMC(configs, cfgFile)
{
    Data.set("thaumcraft:instability_penalty", cfgFile.getCategory("Additional").getIntEntry("Instability", 2048, 0, Integer.MAX_VALUE).setDescription("Additional EMC penalty per instability point.").getValue());
    var aspectCosts = Lists.hashMap();
    Aspect.aspects.values().forEach(function(a)
    {
        aspectCosts.put(a, cfgFile.getCategory("AspectEMC").getIntEntry(a.getName(), 64, 0, Integer.MAX_VALUE).setDescription("Default cost of " + a.getName() + ".").getValue());
    });
    Data.set("thaumcraft:aspect_costs", aspectCosts);

	configs.addEMC(getItem(mod, "shimmerleaf"), "Shimmerleaf", 128);
	configs.addEMC(getItem(mod, "cinderpearl"), "Cinderpearl", 768);
	configs.addEMC(getItem(mod, "brain"), "ZombieBrains", 256);
	configs.addEMC(getItem(mod, "salis_mundus"), "SalisMundus", 128);
	configs.addEMC(getItem(mod, "amber"), "Amber", 512);
	configs.addEMC(getItem(mod, "void_seed"), "VoidSeed", 2048);
	configs.addEMC(getItem(mod, "nugget"), 10, "RareEarths", 4096);
	configs.addEMC(getItem(mod, "primordial_pearl"), "PrimordialPearl", 131072);

	Data.set("thaumcraft:primordial_pearl", configs.getEMC("PrimordialPearl", 131072));
	Data.set("thaumcraft:vis_crystal", configs.getEMC("VisCrystal", 32));
}

function addMappers(mappers)
{
    mappers.addMapper("MagicalEMCMapper");
}

function getAspectCost(Aspect)
{
    return Math.max(0, Data.get("thaumcraft:aspect_costs").get(Aspect));
}

// mappers

function MagicalEMCMapper(emc)
{
    var primordialPearlCost = Data.get("thaumcraft:primordial_pearl");
    var visCrystalCost = Data.get("thaumcraft:vis_crystal");

    var primordialPearlStack = ItemStack.of(getItem(mod, "primordial_pearl"));

    ThaumcraftApi.getCraftingRecipes().values().forEach(function(recipe)
    {
        var type = recipe.getClass();
        var result = null;
        var pearls = Lists.arrayList();

        var im = Lists.arrayList();

        if(IArcaneRecipe.class.isAssignableFrom(type))
        {
            result = Vanilla.getRecipeOutput(recipe);

            Vanilla.getIngredients(recipe).forEach(function(i)
            {
                if(i.apply(primordialPearlStack)) pearls.add(i);
                else im.add(Ingredient.of(1, i));
            });

            var vis = recipe.getCrystals();
            Lists.stream(vis.getAspects()).forEach(function(a)
            {
                im.add(emc.fake(getAspectCost(a) + visCrystalCost).stack(vis.getAmount(a)));
            });
        } else if(CrucibleRecipe.class.isAssignableFrom(type))
        {
            result = recipe.getRecipeOutput();
            var i = recipe.getCatalyst();
            if(i.apply(primordialPearlStack)) pearls.add(i);
            else im.add(Ingredient.of(1, i));

            var vis = recipe.getAspects();
            Lists.stream(vis.getAspects()).forEach(function(a)
            {
                im.add(emc.fake(getAspectCost(a)).stack(vis.getAmount(a)));
            });
        } else if(InfusionRecipe.class.isAssignableFrom(type))
        {
            var r = recipe.getRecipeOutput();
            if(!r) return;

            if(Item.class.isAssignableFrom(r.getClass())) result = ItemStack.of(r);
            if(Block.class.isAssignableFrom(r.getClass())) result = ItemStack.of(r);
            if(NativeItemStack.class.isAssignableFrom(r.getClass())) result = r;

            var items = Lists.arrayList();
            items.add(recipe.sourceInput);
            items.addAll(recipe.getComponents());
            items.forEach(function(i)
            {
                if(i.apply(primordialPearlStack)) pearls.add(i);
                else im.add(Ingredient.of(1, i));
            });

            var vis = recipe.getAspects();
            Lists.stream(vis.getAspects()).forEach(function(a)
            {
                im.add(emc.fake(getAspectCost(a)).stack(vis.getAmount(a)));
            });

            im.add(emc.fake(Data.get("thaumcraft:instability_penalty") * recipe.instability).stack(1));
        }

        if(ItemStack.isEmpty(result)) return;
        pearls = pearls.size();
        if(pearls > 0) im.add(emc.fake(primordialPearlCost / 8).stack(pearls));

        emc.map(Ingredient.of(result), im);
    });
}