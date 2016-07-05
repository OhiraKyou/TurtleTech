package ohirakyou.turtletech.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ohirakyou.turtletech.common.block.ModBlocks;
import ohirakyou.turtletech.data.DataModInfo;

public class TurtleTechCreativeTab extends CreativeTabs {
    public TurtleTechCreativeTab() {
        super(DataModInfo.MOD_ID);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(ModBlocks.precision_laser_turret);
    }
}
