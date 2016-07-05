package ohirakyou.turtletech.common.block.turrets;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.common.material.Materials;

import javax.annotation.Nullable;

public class TurretExtenderBlock extends BlockContainer {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public TurretExtenderBlock() {
        super(Material.IRON);
        setHardness(Materials.cast_iron.hardness);
        setSoundType(SoundType.METAL);

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(ACTIVE, false));
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { ACTIVE, FACING });
    }


    @Override
    public TurretExtenderTileEntity createNewTileEntity(World w, int meta) {
        return new TurretExtenderTileEntity();
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
    }

    public static EnumFacing getFacingFromEntity(BlockPos pos, EntityLivingBase placer) {
        if (MathHelper.abs((float)placer.posX - (float)pos.getX()) < 2.0F && MathHelper.abs((float)placer.posZ - (float)pos.getZ()) < 2.0F) {
            double d0 = placer.posY + (double)placer.getEyeHeight();

            if (d0 - (double)pos.getY() > 2.0D) { return EnumFacing.UP; }

            if ((double)pos.getY() - d0 > 0.0D) { return EnumFacing.DOWN; }
        }

        return placer.getHorizontalFacing().getOpposite();
    }


    /**
     * Converts metadata into blockstate
     */
    @Override
    public IBlockState getStateFromMeta(final int metaValue) {
        EnumFacing enumFacing = metaToFacing(metaValue);
        if (enumFacing.getAxis() == EnumFacing.Axis.Y) {
            enumFacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumFacing)
                .withProperty(ACTIVE, (metaValue & 0x8) != 0);
    }

    /**
     * Converts blockstate into metadata
     */
    @Override
    public int getMetaFromState(final IBlockState bs) {
        int extraBit;
        if(bs.getValue(ACTIVE)){
            extraBit = 0x8;
        } else {
            extraBit = 0;
        }
        return facingToMeta(bs.getValue(FACING)) | extraBit;
    }

    private int facingToMeta(EnumFacing f){
        return f.getIndex();
    }
    private EnumFacing metaToFacing(int i){
        int f = i & 0x07;
        return EnumFacing.values()[f];
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


    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

        // Get example usage from BlockPistonBase
        // Use in tile entity instead?

        return true;
    }


    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
