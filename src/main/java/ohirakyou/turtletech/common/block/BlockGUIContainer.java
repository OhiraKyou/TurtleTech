package ohirakyou.turtletech.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.*;
import ohirakyou.turtletech.TurtleTech;

import static net.minecraft.init.Items.BUCKET;

public abstract class BlockGUIContainer extends BlockContainer{

    public int guiId = -1;

    public BlockGUIContainer(Material material) {
        super(material);
    }

    @Override
    public boolean onBlockActivated(
            World world, BlockPos pos, IBlockState bs, EntityPlayer player, EnumHand hand, ItemStack heldItem,
            EnumFacing facing, float hitX, float hitY, float hitZ) {

        // Open GUI
        if (guiId > -1) {
            TileEntity te = world.getTileEntity(pos);

            if (te != null) {
                player.openGui(TurtleTech.instance, guiId, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        return true;
    }


    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state){
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof IInventory) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory)te);
            ((IInventory)te).clear();
            world.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(world, pos, state);
    }
}
