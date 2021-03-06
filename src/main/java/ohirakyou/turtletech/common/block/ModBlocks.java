package ohirakyou.turtletech.common.block;

import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.common.block.generators.creative.CreativeGeneratorBlock;
import ohirakyou.turtletech.common.block.generators.reflex.RedstoneReflexGeneratorBlock;
import ohirakyou.turtletech.common.block.generators.solar.SolarGeneratorBlock;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorBlock;
import ohirakyou.turtletech.common.block.turrets.TurretExtenderBlock;
import ohirakyou.turtletech.common.block.turrets.precisionlaserturret.PrecisionLaserTurretBlock;
import ohirakyou.turtletech.common.item.ItemGroups;
import ohirakyou.turtletech.common.item.ItemModSlab;
import ohirakyou.turtletech.common.material.Materials;
import ohirakyou.turtletech.data.DataModInfo;
import ohirakyou.turtletech.common.block.foundry.FoundryBlock;

import cyano.basemetals.blocks.*;
import cyano.basemetals.material.MetalMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;


public class ModBlocks {
    private static final Map<String,Block> blocks = new HashMap<>();

    public static Block getBlockByName(String name) {
        return blocks.get(name);
    }

    public static PrecisionLaserTurretBlock precision_laser_turret;
    public static Block turret_extender;
    public static FoundryBlock foundry;
    public static SpeedBlock cast_iron_tread;
    public static BlockBase cast_iron_casing;

    public static CreativeGeneratorBlock creative_generator;
    public static RedstoneReflexGeneratorBlock redstone_reflex_generator;
    public static SolarGeneratorBlock solar_reflex_generator;
    public static SoulTurbineGeneratorBlock soul_turbine_generator;

    public static Block cast_iron_block;
    public static Block cast_iron_plate;
    public static Block cast_iron_trapdoor;
    public static Block cast_iron_bars;
    public static BlockDoor cast_iron_door;

    public static BlockGrate cast_iron_grate;

    public static BlockModStairs cast_iron_stairs;
    public static BlockModStairs cast_iron_casing_stairs;
    public static BlockModSlab cast_iron_slab;
    public static BlockModSlab cast_iron_slab_double;


    public static void init() {
        // Generated blocks
        cast_iron_block = createBlock(Materials.cast_iron);
        cast_iron_plate = createPlate(Materials.cast_iron);
        cast_iron_door = createDoor(Materials.cast_iron);
        cast_iron_trapdoor = createTrapDoor(Materials.cast_iron);
        cast_iron_bars = createBars(Materials.cast_iron);

        BlockModSlab[] castIronSlabs = createSlabs(Materials.cast_iron);
        cast_iron_slab = castIronSlabs[0];
        cast_iron_slab_double = castIronSlabs[1];

        // Blocks
        cast_iron_casing = addBlock(new BlockBase(Materials.cast_iron), "cast_iron_casing");
        cast_iron_tread = addBlock(new SpeedBlock(Materials.cast_iron, 1.25f), "cast_iron_tread");
        cast_iron_grate = addBlock(new BlockGrate(Materials.cast_iron), "cast_iron_grate");

        cast_iron_stairs = addBlock(new BlockModStairs(cast_iron_block.getDefaultState()), "cast_iron_stairs");
        cast_iron_casing_stairs = addBlock(new BlockModStairs(cast_iron_block.getDefaultState()), "cast_iron_casing_stairs");

        // Machines
        foundry = addBlock(new FoundryBlock(), "foundry");

        // Turrets
        precision_laser_turret = addBlock(new PrecisionLaserTurretBlock(), "precision_laser_turret");
        turret_extender = addBlock(new TurretExtenderBlock(), "turret_extender");

        // Generators
        creative_generator = addBlock(new CreativeGeneratorBlock(), "creative_generator");
        redstone_reflex_generator = addBlock(new RedstoneReflexGeneratorBlock(), "redstone_reflex_generator");
        solar_reflex_generator = addBlock(new SolarGeneratorBlock(), "solar_reflex_generator");
        soul_turbine_generator = addBlock(new SoulTurbineGeneratorBlock(), "soul_turbine_generator");

        for(Block b : blocks.values()){
            if(b instanceof IOreDictionaryEntry){
                OreDictionary.registerOre(((IOreDictionaryEntry)b).getOreDictionaryName(), b);
            }
            b.setCreativeTab(ItemGroups.tab_blocks);
        }
    }

    private static <BLOCK extends Block> BLOCK addBlock(BLOCK block, String name, String suffix){
        return addBlock(block, name + "_" + suffix);
    }

    private static <BLOCK extends Block> BLOCK addBlock(BLOCK block, String name){
        ResourceLocation location = new ResourceLocation(DataModInfo.MOD_ID, name);

        block.setRegistryName(location);
        block.setUnlocalizedName(location.toString());
        GameRegistry.register(block);

        // Add the item for this block
        // Skip doors due to them actually being, visually, two blocks in one (bottom and top)
        // Skip slabs because they must be initialized in single/double pairs and itemized later
        if (!(block instanceof BlockMetalDoor) && !(block instanceof BlockModSlab)) {
            ItemBlock itemBlock = new ItemBlock(block);
            itemBlock.setRegistryName(location);
            itemBlock.setUnlocalizedName(location.toString());
            GameRegistry.register(itemBlock);
        }

        blocks.put(name, block);
        return block;
    }

    private static Block createBlock(MetalMaterial metal){
        return createBlock(metal, false);
    }

    private static Block createBlock(MetalMaterial metal, boolean glow){
        return addBlock(new BlockMetalBlock(metal, glow), metal.getName(), "block");
    }

    private static Block createPlate(MetalMaterial metal) {
        return addBlock(new BlockMetalPlate(metal), metal.getName(), "plate");
    }

    private static Block createBars(MetalMaterial metal){
        return addBlock(new BlockMetalBars(metal), metal.getName(), "bars");
    }

    private static Block createOre(MetalMaterial metal){
        return addBlock(new BlockMetalOre(metal), metal.getName(), "ore");
    }

    private static BlockDoor createDoor(MetalMaterial metal){
        return (BlockDoor)addBlock(new ohirakyou.turtletech.common.block.BlockMetalDoor(metal), metal.getName(), "door");
    }

    private static Block createTrapDoor(MetalMaterial metal){
        return addBlock(new BlockMetalTrapDoor(metal), metal.getName(), "trapdoor");
    }



    private static BlockModSlab[] createSlabs(MetalMaterial metal) {
        return createSlabs(new BlockModSlab(metal, false), new BlockModSlab(metal, true), metal.getName());
    }

    private static BlockModSlab[] createSlabs(Material material, String slabName) {
        return createSlabs(new BlockModSlab(material, false), new BlockModSlab(material, true), slabName);
    }

    private static BlockModSlab[] createSlabs(BlockModSlab singleSlab, BlockModSlab doubleSlab, String slabName) {
        addBlock(singleSlab, slabName, "slab");
        addBlock(doubleSlab, slabName, "slab_double");
        singleSlab.setBlocks(singleSlab, doubleSlab);
        doubleSlab.setBlocks(singleSlab, doubleSlab);

        return new BlockModSlab[] {singleSlab, doubleSlab};
    }



    @SideOnly(Side.CLIENT)
    public static void registerItemRenders() {
        for(String name : blocks.keySet()) {
            if(blocks.get(name) instanceof BlockMetalDoor) continue;  // skip doors, which are two visible blocks

            Item item = Item.getItemFromBlock(blocks.get(name));
            ResourceLocation location = new ResourceLocation(DataModInfo.MOD_ID, name);
            ModelResourceLocation modelLocation = new ModelResourceLocation(location, "inventory");
            final int meta = 0;  // for readability

            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, modelLocation);
        }
    }
}
