package ohirakyou.turtletech.common.item;

import cyano.basemetals.blocks.*;
import cyano.basemetals.items.*;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.common.block.*;
import ohirakyou.turtletech.common.block.BlockMetalDoor;
import ohirakyou.turtletech.common.material.Materials;
import ohirakyou.turtletech.data.DataModInfo;

import java.util.*;

public class ModItems {

    private static Map<Item,String> itemRegistry = new HashMap<>();
    private static Map<String,Item> allItems = new HashMap<>();
    private static Map<MetalMaterial,List<Item>> itemsByMetal = new HashMap<>();

    private static Map<BlockDoor,Item> doorMap = new HashMap<>();



    @SuppressWarnings("rawtypes")
    private static Map<Class,Integer> classSortingValues = new HashMap<>();
    private static Map<MetalMaterial,Integer> materialSortingValues = new HashMap<>();

    public static Item getItemByName(String name){
        return allItems.get(name);
    }

    public static String getNameOfItem(Item i){
        return itemRegistry.get(i);
    }

    public static Map<MetalMaterial,List<Item>> getItemsByMetal(){
        return Collections.unmodifiableMap(itemsByMetal);
    }


    public static Item cast_iron_axe;
    public static Item cast_iron_blend;
    public static Item cast_iron_boots;
    public static Item cast_iron_chestplate;
    public static Item cast_iron_crackhammer;
    public static Item cast_iron_door;
    public static Item cast_iron_gear;
    public static Item cast_iron_helmet;
    public static Item cast_iron_hoe;
    public static Item cast_iron_ingot;
    public static Item cast_iron_leggings;
    public static Item cast_iron_nugget;
    public static Item cast_iron_pickaxe;
    public static Item cast_iron_powder;
    public static Item cast_iron_shovel;
    public static Item cast_iron_sword;
    public static Item cast_iron_rod;



    public static Item getDoorItemForBlock(BlockMetalDoor b){
        return doorMap.get(b);
    }


    private static boolean initDone = false;
    public static void init(){
        if(initDone) return;

        cast_iron_axe = create_axe(Materials.cast_iron);
        cast_iron_blend = create_blend(Materials.cast_iron);
        cast_iron_boots = create_boots(Materials.cast_iron);
        cast_iron_chestplate = create_chestplate(Materials.cast_iron);
        cast_iron_crackhammer = create_crackhammer(Materials.cast_iron);
        cast_iron_door = create_door(Materials.cast_iron, ModBlocks.cast_iron_door);
        cast_iron_gear = create_gear(Materials.cast_iron);
        cast_iron_helmet = create_helmet(Materials.cast_iron);
        cast_iron_hoe = create_hoe(Materials.cast_iron);
        cast_iron_ingot = create_ingot(Materials.cast_iron);
        cast_iron_leggings = create_leggings(Materials.cast_iron);
        cast_iron_nugget = create_nugget(Materials.cast_iron);
        cast_iron_pickaxe = create_pickaxe(Materials.cast_iron);
        cast_iron_powder = create_powder(Materials.cast_iron);
        cast_iron_shovel = create_shovel(Materials.cast_iron);
        cast_iron_sword = create_sword(Materials.cast_iron);
        cast_iron_rod = create_rod(Materials.cast_iron);


        // Ore dictionary registration
        for(Item i : itemRegistry.keySet()){
            allItems.put(itemRegistry.get(i), i);
            if(i instanceof IOreDictionaryEntry){
                OreDictionary.registerOre(((IOreDictionaryEntry)i).getOreDictionaryName(), i);
            }
        }


        // Sorting
        int ss = 0;
        classSortingValues.put(BlockMetalOre.class, ++ss * 10000);
        classSortingValues.put(BlockMetalBlock.class, ++ss * 10000);
        classSortingValues.put(BlockMetalPlate.class, ++ss * 10000);
        classSortingValues.put(BlockMetalBars.class, ++ss * 10000);
        classSortingValues.put(BlockMetalDoor.class, ++ss * 10000);
        classSortingValues.put(BlockMetalTrapDoor.class, ++ss * 10000);
        classSortingValues.put(InteractiveFluidBlock.class, ++ss * 10000);
        classSortingValues.put(ItemMetalIngot.class, ++ss * 10000);
        classSortingValues.put(ItemMetalNugget.class, ++ss * 10000);
        classSortingValues.put(ItemMetalPowder.class, ++ss * 10000);
        classSortingValues.put(ItemMetalBlend.class, classSortingValues.get(ItemMetalPowder.class));
        classSortingValues.put(ItemMetalSmallBlend.class, ++ss * 10000);
        classSortingValues.put(ItemMetalSmallPowder.class, ++ss * 10000);
        classSortingValues.put(ItemMetalCrackHammer.class, ++ss * 10000);
        classSortingValues.put(ItemMetalPickaxe.class, ++ss * 10000);
        classSortingValues.put(ItemMetalShovel.class, ++ss * 10000);
        classSortingValues.put(ItemMetalAxe.class, ++ss * 10000);
        classSortingValues.put(ItemMetalHoe.class, ++ss * 10000);
        classSortingValues.put(ItemMetalSword.class, ++ss * 10000);
        classSortingValues.put(ItemMetalArmor.class, ++ss * 10000);
        classSortingValues.put(ItemMetalShears.class, ++ss * 10000);
        classSortingValues.put(GenericMetalItem.class, ++ss * 10000);
        classSortingValues.put(ItemMetalDoor.class, classSortingValues.get(BlockMetalDoor.class));

        List<MetalMaterial> metlist = new ArrayList<>(Materials.getAllMetals().size());
        metlist.addAll(Materials.getAllMetals());
        metlist.sort((MetalMaterial a, MetalMaterial b)-> a.getName().compareToIgnoreCase(b.getName()));
        for(int i = 0; i < metlist.size(); i++){
            materialSortingValues.put(metlist.get(i), i*100);
        }

        initDone = true;
    }

    private static Item registerItem(Item item, String name, String suffix, MetalMaterial metal, CreativeTabs tab){
        name = name + "_" + suffix;

        ResourceLocation location = new ResourceLocation(DataModInfo.MOD_ID, name);

        item.setRegistryName(location);
        item.setUnlocalizedName(location.toString());
        GameRegistry.register(item);

        itemRegistry.put(item, name);

        if(tab != null){ item.setCreativeTab(tab); }

        if(metal != null){
            itemsByMetal.computeIfAbsent(metal, (MetalMaterial g)->new ArrayList<>());
            itemsByMetal.get(metal).add(item);
        }

        return item;
    }


    private static Item create_ingot(MetalMaterial metal){
        return registerItem(new ItemMetalIngot(metal), metal.getName(), "ingot", metal, ItemGroups.tab_items);
    }

    private static Item create_nugget(MetalMaterial metal){
        return registerItem(new ItemMetalNugget(metal), metal.getName(), "nugget", metal, ItemGroups.tab_items);
    }

    private static Item create_powder(MetalMaterial metal){
        return registerItem(new ItemMetalPowder(metal), metal.getName(), "powder", metal, ItemGroups.tab_items);
    }

    private static Item create_blend(MetalMaterial metal){
        return registerItem(new ItemMetalBlend(metal), metal.getName(), "blend", metal, ItemGroups.tab_items);
    }

    private static Item create_rod(MetalMaterial metal){
        return registerItem(new GenericMetalItem(metal), metal.getName(), "rod", metal, ItemGroups.tab_items);
    }


    private static Item create_gear(MetalMaterial metal){
        return registerItem(new GenericMetalItem(metal), metal.getName(), "gear", metal, ItemGroups.tab_items);
    }

    private static Item create_axe(MetalMaterial metal){
        return registerItem(new ItemMetalAxe(metal), metal.getName(), "axe", metal, ItemGroups.tab_tools);
    }

    private static Item create_crackhammer(MetalMaterial metal){
        return registerItem(new ItemMetalCrackHammer(metal), metal.getName(), "crackhammer", metal, ItemGroups.tab_tools);
    }

    private static Item create_hoe(MetalMaterial metal){
        return registerItem(new ItemMetalHoe(metal), metal.getName(), "hoe", metal, ItemGroups.tab_tools);
    }

    private static Item create_pickaxe(MetalMaterial metal){
        return registerItem(new ItemMetalPickaxe(metal), metal.getName(), "pickaxe", metal, ItemGroups.tab_tools);
    }

    private static Item create_shovel(MetalMaterial metal){
        return registerItem(new ItemMetalShovel(metal), metal.getName(), "shovel", metal, ItemGroups.tab_tools);
    }

    private static Item create_sword(MetalMaterial metal){
        return registerItem(new ItemMetalSword(metal), metal.getName(), "sword", metal, ItemGroups.tab_tools);
    }

    private static Item create_helmet(MetalMaterial metal){
        return registerItem(ItemMetalArmor.createHelmet(metal), metal.getName(), "helmet", metal, ItemGroups.tab_tools);
    }

    private static Item create_chestplate(MetalMaterial metal){
        return registerItem(ItemMetalArmor.createChestplate(metal), metal.getName(), "chestplate", metal, ItemGroups.tab_tools);
    }

    private static Item create_leggings(MetalMaterial metal){
        return registerItem(ItemMetalArmor.createLeggings(metal), metal.getName(), "leggings", metal, ItemGroups.tab_tools);
    }

    private static Item create_boots(MetalMaterial metal){
        return registerItem(ItemMetalArmor.createBoots(metal), metal.getName(), "boots", metal, ItemGroups.tab_tools);
    }

    private static Item create_shears(MetalMaterial metal){
        return registerItem(new ItemMetalShears(metal), metal.getName(), "shears", metal, ItemGroups.tab_tools);
    }

    private static Item create_smallblend(MetalMaterial metal){
        return registerItem(new ItemMetalSmallBlend(metal), metal.getName(), "smallblend", metal, ItemGroups.tab_items);
    }

    private static Item create_smallpowder(MetalMaterial metal){
        return registerItem(new ItemMetalSmallPowder(metal), metal.getName(), "smallpowder", metal, ItemGroups.tab_items);
    }

    private static Item create_door(MetalMaterial metal, BlockDoor door){
        ResourceLocation location = new ResourceLocation(DataModInfo.MOD_ID, metal.getName()+"_door");
        Item item = new ItemMetalDoor(door, metal);
        registerItem(item, location.getResourcePath().toString(), "item", metal, ItemGroups.tab_blocks);
        item.setUnlocalizedName(location.toString()); // Hack to set name right
        doorMap.put(door, item);
        return item;
    }


    @SideOnly(Side.CLIENT)
    public static void registerItemRenders(){
        for(Item i : itemRegistry.keySet()){
            ResourceLocation location = new ResourceLocation(DataModInfo.MOD_ID, itemRegistry.get(i));
            final ModelResourceLocation modelLocation = new ModelResourceLocation(location, "inventory");

            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(i, 0, modelLocation);
        }
    }



    @SuppressWarnings("rawtypes")
    public static int getSortingValue(ItemStack a){
        int classVal = 990000;
        int metalVal = 9900;
        if(a.getItem() instanceof ItemBlock && ((ItemBlock)a.getItem()).getBlock() instanceof IMetalObject){
            classVal = classSortingValues.computeIfAbsent(((ItemBlock)a.getItem()).getBlock().getClass(),
                    (Class c)->990000);
            metalVal = materialSortingValues.computeIfAbsent(((IMetalObject)((ItemBlock)a.getItem()).getBlock()).getMetalMaterial(),
                    (MetalMaterial m)->9900);
        } else if(a.getItem() instanceof IMetalObject){
            classVal = classSortingValues.computeIfAbsent(a.getItem().getClass(),
                    (Class c)->990000);
            metalVal = materialSortingValues.computeIfAbsent(((IMetalObject)a.getItem()).getMetalMaterial(),
                    (MetalMaterial m)->9900);
        }
        return classVal + metalVal + (a.getMetadata() % 100);
    }
}
