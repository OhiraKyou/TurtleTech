package ohirakyou.turtletech.common.crafting;

import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.CrusherRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ohirakyou.turtletech.common.material.Materials;
import ohirakyou.turtletech.common.block.ModBlocks;
import ohirakyou.turtletech.common.item.ModItems;
import ohirakyou.turtletech.data.DataMods;
import org.apache.commons.lang3.ArrayUtils;

//// TODO: 6/14/2016  Automate creation of recipe enable/disable toggles in config

public final class ModCrafting {

    private static boolean initDone = false;
    public static void init() {
        if(initDone)return;

        initMetalRecipes();
        initCustomRecipes();

        initDone = true;
    }


    private static void initCustomRecipes() {
        // Miscellaneous cast iron items
        addHollowCubeRecipe(ModBlocks.cast_iron_casing, ModBlocks.cast_iron_plate, "plateCastIron");

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.cast_iron_tread, 2),
                " i ",
                "ibi",
                'i', ModItems.cast_iron_ingot, 'b', ModBlocks.cast_iron_block));


        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.cast_iron_grate, 6),
                "i i",
                " i ",
                "i i",
                'i', "ingotCastIron"));


        // Stairs
        addStairsRecipe(ModBlocks.cast_iron_stairs, ModBlocks.cast_iron_block);
        addStairsRecipe(ModBlocks.cast_iron_casing_stairs, ModBlocks.cast_iron_casing);


        // Slabs
        addSlabRecipe(ModBlocks.cast_iron_slab, ModBlocks.cast_iron_block);


        // Generators
        addCastIronGeneratorRecipe(ModBlocks.redstone_reflex_generator, Blocks.PISTON);
        addCastIronGeneratorRecipe(ModBlocks.solar_reflex_generator, Blocks.PISTON, Blocks.DAYLIGHT_DETECTOR);

        addCastIronGeneratorRecipe(ModBlocks.soul_turbine_generator, "ingotSoularium");
        if (!(Loader.isModLoaded(DataMods.ENDERIO))) {
            addCastIronGeneratorRecipe(ModBlocks.soul_turbine_generator, "ingotGold", Blocks.SOUL_SAND, "ingotGold");
        }


        // Turrets
        addCastIronMachineRecipe(ModBlocks.precision_laser_turret, "pde", new Object[]{
                'p', "PSU", 'd', "gemDiamond", 'e', "gemEmerald"});
        addCastIronMachineRecipe(ModBlocks.precision_laser_turret, "ped", new Object[]{
                'p', "PSU", 'd', "gemDiamond", 'e', "gemEmerald"});

        if (!(Loader.isModLoaded(DataMods.ELECTRIC_ADVANTAGE))) {
            // Fallback for when Electric Advantage is not installed
            addCastIronMachineRecipe(ModBlocks.precision_laser_turret, "ede", new Object[]{
                    'd', "gemDiamond", 'e', "gemEmerald"});
        }


        addCastIronMachineRecipe(ModBlocks.turret_extender,
                " p ",
                " i ",
                new Object[]{'p', "plateCastIron", 'i', Blocks.PISTON});



        // Machines
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.foundry,
                "coc",
                "bcb",
                "bbb",
                'c', Items.CLAY_BALL, 'o', new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE), 'b', "ingotBrick"));
    }

    private static void addSlabRecipe(Block slab, Block block) {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(slab, 4),
                "bb",
                'b', block));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block, 1),
                "s",
                "s",
                's', slab));
    }

    private static void addStairsRecipe(Block stairs, Block block) {
        // Blocks -> stairs
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stairs, 8),
                "b  ",
                "bb ",
                "bbb",
                'b', block));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stairs, 8),
                "  b",
                " bb",
                "bbb",
                'b', block));

        // Stairs -> blocks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block, 3),
                "ss",
                "ss",
                's', stairs));
    }

    private static void addHollowCubeRecipe(Block cube, Block plate, String plateName) {
        // Plates -> cube
        GameRegistry.addRecipe(new ShapelessOreRecipe(cube,
                plateName, plateName, plateName, plateName, plateName, plateName));

        // Cube -> plates
        GameRegistry.addShapelessRecipe(new ItemStack(plate, 6), cube);
    }

    private static void addCastIronGeneratorRecipe(Block generator, Object driver) {
        addCastIronGeneratorRecipe(generator, " d ", new Object[]{'d', driver});

        //todo create a simple base generator and add a recipe that uses it and the driver
        //GameRegistry.addRecipe(new ShapelessOreRecipe(generator, driver, ModBlocks.simple_generator));
    }

    private static void addCastIronGeneratorRecipe(Block generator, Object driver1, Object driver2) {
        Object[] materials = new Object[]{'a', driver1, 'b', driver2};
        addCastIronGeneratorRecipe(generator, "ba ", materials);
        addCastIronGeneratorRecipe(generator, " ab", materials);
    }

    private static void addCastIronGeneratorRecipe(Block generator, Object driver1, Object driver2, Object driver3) {
        Object[] materials = new Object[]{'a', driver1, 'b', driver2, 'c', driver3};
        addCastIronGeneratorRecipe(generator, "abc", materials);
    }

    private static void addCastIronGeneratorRecipe(Block generator, String topRow, Object[] driverMaterials) {
        // Capital letters are used here to distinguish shared generator materials from unique driver materials
        Object[] generatorMaterials = {'O', "ingotCopper", 'I', "ingotIron",};
        Object[] materials = ArrayUtils.addAll(driverMaterials, generatorMaterials);

        addCastIronMachineRecipe(generator, topRow, "OIO", materials);
    }


    private static void addCastIronMachineRecipe(Block machine, String topRow, Object[] machineMaterials) {
        addCastIronMachineRecipe(machine, new Object[]{topRow}, machineMaterials);
    }

    private static void addCastIronMachineRecipe(Block machine, String topRow, String middleRow, Object[] machineMaterials) {
        addCastIronMachineRecipe(machine, new Object[]{topRow, middleRow}, machineMaterials);
    }

    private static void addCastIronMachineRecipe(Block machine, Object[] rows, Object[] machineMaterials) {
        // Capital letters are used here to distinguish shared machine materials from unique recipe materials
        Object[] shapeDefinition = ArrayUtils.add(rows, "RCR");

        Object[] generatorMaterials = {'R', "dustRedstone", 'C', ModBlocks.cast_iron_casing};

        Object[] finalRecipe = ArrayUtils.addAll(shapeDefinition, machineMaterials);
        finalRecipe = ArrayUtils.addAll(finalRecipe, generatorMaterials);

        GameRegistry.addRecipe(new ShapedOreRecipe(machine, finalRecipe));
    }


    private static void initMetalRecipes(){
        MetalMaterial metal = Materials.cast_iron;

        String baseName = metal.getName()+"_";
        String oreDictName = metal.getCapitalizedName();

        Item axe = ModItems.getItemByName(baseName+"axe");
        Item blend = ModItems.getItemByName(baseName+"blend");
        Item boots = ModItems.getItemByName(baseName+"boots");
        Item chestplate = ModItems.getItemByName(baseName+"chestplate");
        Item crackhammer = ModItems.getItemByName(baseName+"crackhammer");
        Item door = ModItems.getItemByName(baseName+"door_item");
        Item helmet = ModItems.getItemByName(baseName+"helmet");
        Item hoe = ModItems.getItemByName(baseName+"hoe");
        Item ingot = ModItems.getItemByName(baseName+"ingot");
        Item leggings = ModItems.getItemByName(baseName+"leggings");
        Item nugget = ModItems.getItemByName(baseName+"nugget");
        Item pickaxe = ModItems.getItemByName(baseName+"pickaxe");
        Item powder = ModItems.getItemByName(baseName+"powder");
        Item shovel = ModItems.getItemByName(baseName+"shovel");
        Item sword = ModItems.getItemByName(baseName+"sword");
        Item rod = ModItems.getItemByName(baseName+"rod");
        Item gear = ModItems.getItemByName(baseName+"gear");
        Block bars = ModBlocks.getBlockByName(baseName+"bars");
        Block block = ModBlocks.getBlockByName(baseName+"block");
        Block plate = ModBlocks.getBlockByName(baseName+"plate");
        Block ore = ModBlocks.getBlockByName(baseName+"ore");
        Block trapdoor = ModBlocks.getBlockByName(baseName+"trapdoor");

        Item arrow = ModItems.getItemByName(baseName+"arrow");
        Item bow = ModItems.getItemByName(baseName+"bow");
        Item bolt = ModItems.getItemByName(baseName+"bolt");
        Item crossbow = ModItems.getItemByName(baseName+"crossbow");
        Item shears = ModItems.getItemByName(baseName+"shears");
        Item smallblend = ModItems.getItemByName(baseName+"smallblend");
        Item smallpowder = ModItems.getItemByName(baseName+"smallpowder");
        Item fishingrod = ModItems.getItemByName(baseName+"fishingrod");

        // NOTE: smelting XP is based on output item, not input item
        // ingot-related recipes
        if(ore != null && powder != null){
            CrusherRecipeRegistry.addNewCrusherRecipe("ore"+oreDictName,new ItemStack(powder,2));
        }
        if(ore != null && ingot != null){
            GameRegistry.addSmelting(ore, new ItemStack(ingot,1), metal.getOreSmeltXP());
        }
        if(ingot != null && powder != null){
            CrusherRecipeRegistry.addNewCrusherRecipe("ingot"+oreDictName,new ItemStack(powder,1));
            GameRegistry.addSmelting(powder, new ItemStack(ingot,1), metal.getOreSmeltXP());
        }
        if(ingot != null && blend != null){
            GameRegistry.addSmelting(blend, new ItemStack(ingot,1), metal.getOreSmeltXP());
        }
        if(ingot != null && nugget != null){
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(nugget,9), new ItemStack(ingot)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ingot), "xxx","xxx","xxx",'x',"nugget"+oreDictName));
        }
        if(ingot != null && block != null){
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot,9), new ItemStack(block)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block), "xxx","xxx","xxx",'x',"ingot"+oreDictName));
        }
        if(ingot != null && plate != null){
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(plate,3), "xxx",'x',"ingot"+oreDictName));
            GameRegistry.addSmelting(plate, new ItemStack(ingot,1), metal.getOreSmeltXP());
        }
        if(block != null && powder != null){
            CrusherRecipeRegistry.addNewCrusherRecipe("block"+oreDictName, new ItemStack(powder,9));
        }
        if(ingot != null && bars != null){
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bars,16), "xxx","xxx",'x',"ingot"+oreDictName));
            OreDictionary.registerOre("bars", bars);
        }
        if(ingot != null && rod != null){
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(rod,4), "x","x",'x',"ingot"+oreDictName));
            OreDictionary.registerOre("stick"+oreDictName,rod);
            OreDictionary.registerOre("rod"+oreDictName,rod);
            OreDictionary.registerOre("rod",rod);
        }
        if(nugget != null && rod != null){
            GameRegistry.addSmelting(rod, new ItemStack(nugget,4), 0);
        }
        if(rod != null && bars != null){
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bars,4), "xxx",'x',"rod"+oreDictName));
        }
        if(rod != null && ingot != null && gear != null){
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(gear,4), " x ","x/x"," x ",'x',"ingot"+oreDictName,'/',"rod"+oreDictName));
            OreDictionary.registerOre("gear"+oreDictName,gear);
            OreDictionary.registerOre("gear",gear);
        }
        if(ingot != null && door != null){
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(door,3), "xx","xx","xx",'x',"ingot"+oreDictName));
            OreDictionary.registerOre("door", door);
        }
        if(ingot != null && trapdoor != null){
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(trapdoor), "xx","xx",'x',"ingot"+oreDictName));
            OreDictionary.registerOre("trapdoor", trapdoor);
        }

        if(blend != null && smallblend != null){
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smallblend,9), new ItemStack(blend)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blend), "xxx","xxx","xxx",'x',new ItemStack(smallblend)));
            GameRegistry.addSmelting(smallblend, new ItemStack(nugget,1), metal.getOreSmeltXP());
        }
        if(powder != null && smallpowder != null){
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smallpowder,9), new ItemStack(powder)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(powder), "xxx","xxx","xxx",'x',new ItemStack(smallpowder)));
            GameRegistry.addSmelting(smallpowder, new ItemStack(nugget,1), metal.getOreSmeltXP());
            CrusherRecipeRegistry.addNewCrusherRecipe("nugget"+oreDictName,new ItemStack(smallpowder,1));
        }

        // Armor and tools
        if(ingot != null && boots != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(boots), "x x", "x x", 'x', "ingot"+oreDictName));
        if(ingot != null && helmet != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helmet), "xxx", "x x", 'x', "ingot"+oreDictName));
        if(ingot != null && chestplate != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chestplate), "x x", "xxx", "xxx", 'x', "ingot"+oreDictName));
        if(ingot != null && leggings != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(leggings), "xxx", "x x", "x x", 'x', "ingot"+oreDictName));
        if(ingot != null && axe != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "x/", " /", 'x', "ingot"+oreDictName,'/', "stickWood"));
        if(block != null && crackhammer != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(crackhammer), "x", "/", "/" , 'x', "block"+oreDictName, '/', "stickWood"));
        if(ingot != null && hoe != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", " /" , " /", 'x', "ingot"+oreDictName, '/', "stickWood"));
        if(ingot != null && pickaxe != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pickaxe), "xxx", " / ", " / ", 'x', "ingot"+oreDictName, '/', "stickWood"));
        if(ingot != null && shovel != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel), "x", "/", "/", 'x', "ingot"+oreDictName, '/', "stickWood"));
        if(ingot != null && sword != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword), "x", "x" , "/", 'x', "ingot"+oreDictName, '/', "stickWood"));
        if(ingot != null && shears != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shears), " x", "x " , 'x', "ingot"+oreDictName));

        // Bows and crossbows
        if(rod != null && arrow != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(arrow), "x", "y", "z", 'x', "nugget"+oreDictName, 'y', "rod"+oreDictName,'z' ,net.minecraft.init.Items.FEATHER));
        if(rod != null && bow != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bow), " xy", "x y", " xy", 'x', "rod"+oreDictName, 'y', net.minecraft.init.Items.STRING));

        // alloy blends
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.cast_iron_blend, 2), "dustIron", "dustCarbon"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.cast_iron_blend, 2), "dustIron", "dustCoal"));
    }

}
