package ohirakyou.turtletech.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import ohirakyou.turtletech.data.DataModInfo;

public class ItemBase extends Item {
    public ItemBase(String itemName) {
        setUnlocalizedName(DataModInfo.MOD_ID + "." + itemName);
        setRegistryName(getUnlocalizedName());

        setCreativeTab(CreativeTabs.MISC);
    }
}
