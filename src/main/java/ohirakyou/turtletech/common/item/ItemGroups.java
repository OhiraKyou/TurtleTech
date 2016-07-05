package ohirakyou.turtletech.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cyano.basemetals.init.FunctionalCreativeTab;
import ohirakyou.turtletech.common.block.ModBlocks;
import ohirakyou.turtletech.data.DataModInfo;

public class ItemGroups extends cyano.basemetals.init.ItemGroups {
    public static CreativeTabs tab_blocks;
    public static CreativeTabs tab_items;
    public static CreativeTabs tab_tools;

    private static boolean initDone = false;
    public static void init(){
        if(initDone) return;

        tab_tools = tab_items = tab_blocks = new FunctionalCreativeTab(DataModInfo.MOD_ID.concat(".blocks"), false,
                ()->Item.getItemFromBlock(ModBlocks.precision_laser_turret),
                (ItemStack a,ItemStack b)->{
                    int delta = ModItems.getSortingValue(a)-ModItems.getSortingValue(b);
                    if(delta == 0) return a.getItem().getUnlocalizedName().compareToIgnoreCase(b.getItem().getUnlocalizedName());
                    return delta;
                });


        initDone = true;
    }
}
