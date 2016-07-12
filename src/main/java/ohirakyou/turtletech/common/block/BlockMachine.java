package ohirakyou.turtletech.common.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.item.ItemGroups;
import ohirakyou.turtletech.common.material.MetalMaterialComplex;
import ohirakyou.turtletech.util.GUIUtils;

import java.util.List;
import java.util.Random;

public abstract class BlockMachine extends BlockGUIContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockMachine() {
        this(Material.IRON);
    }

    public BlockMachine(MetalMaterialComplex metal) {
        this(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(metal.hardness);
        setResistance(metal.getBlastResistance());
        setHarvestLevel("pickaxe", 0);
    }

    public BlockMachine(Material material) {
        super(material);
        this.setDefaultState(getDefaultState().withProperty(ACTIVE, true).withProperty(POWERED, false).withProperty(FACING, EnumFacing.NORTH));
        setCreativeTab(ItemGroups.tab_blocks);
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE, FACING, POWERED);
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
        return this.getDefaultState().withProperty( FACING, enumFacing)
                .withProperty(ACTIVE, (metaValue & 0x4) != 0)
                .withProperty(POWERED, (metaValue & 0x8) != 0);
    }

    /**
     * Converts blockstate into metadata
     */
    @Override
    public int getMetaFromState(final IBlockState bs) {
        int extraBit;
        if((bs.getValue(ACTIVE))){
            extraBit = 0x4;
        } else {
            extraBit = 0;
        }
        if((bs.getValue(POWERED))){
            extraBit = extraBit | 0x8;
        }
        return facingToMeta(bs.getValue(FACING)) | extraBit;
    }

    private int facingToMeta(EnumFacing f){
        switch(f){
            case NORTH: return 0;
            case WEST: return 1;
            case SOUTH: return 2;
            case EAST: return 3;
            default: return 0;
        }
    }
    private EnumFacing metaToFacing(int i){
        int f = i & 0x03;
        switch(f){
            case 0: return EnumFacing.NORTH;
            case 1: return EnumFacing.WEST;
            case 2: return EnumFacing.SOUTH;
            case 3: return EnumFacing.EAST;
            default: return EnumFacing.NORTH;
        }
    }


    @Override
    public void onBlockAdded(final World world, final BlockPos coord, final IBlockState state) {
        this.setDefaultFacing(world, coord, state);
    }
    /**
     * This method is called when the block is removed from the world by an entity.
     */
    @Override
    public void onBlockDestroyedByPlayer(World w, BlockPos coord, IBlockState state){
        super.onBlockDestroyedByPlayer(w, coord, state);
    }
    /**
     * This method is called when the block is destroyed by an explosion.
     */
    @Override
    public void onBlockDestroyedByExplosion(World w, BlockPos coord, Explosion boom){
        super.onBlockDestroyedByExplosion(w, coord, boom);
    }

    @Override
    public IBlockState onBlockPlaced(
            World w, BlockPos pos, EnumFacing facing, float x, float y, float z, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty( FACING, placer.getHorizontalFacing().getOpposite());
    }

    /**
     * Creates the blockstate of this block when it is placed in the world
     */
    @Override
    public void onBlockPlacedBy(final World world, final BlockPos coord, final IBlockState bs,
                                final EntityLivingBase placer, final ItemStack srcItemStack) {

        world.setBlockState(coord, bs.withProperty((IProperty) FACING, (Comparable)placer.getHorizontalFacing().getOpposite()), 2);
        /*
        if (srcItemStack.hasDisplayName()) {
            final TileEntity tileEntity = world.getTileEntity(coord);
        }*/
    }


    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state){
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory)te);
            ((IInventory)te).clear();
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }


    @Override
    public Item getItemDropped(final IBlockState state, final Random prng, final int i3) {
        return Item.getItemFromBlock(this);
    }


    /**
     * Sets the default blockstate
     * @param w World instance
     * @param coord Block coordinate
     * @param state Block state
     */
    protected void setDefaultFacing(final World w, final BlockPos coord, final IBlockState state) {
        if (w.isRemote) { return; }

        final IBlockState block = w.getBlockState(coord.north());
        final IBlockState block2 = w.getBlockState(coord.south());
        final IBlockState block3 = w.getBlockState(coord.west());
        final IBlockState block4 = w.getBlockState(coord.east());

        EnumFacing enumFacing = state.getValue(FACING);

        if (enumFacing == EnumFacing.NORTH && block.isFullBlock() && !block2.isFullBlock()) {
            enumFacing = EnumFacing.SOUTH;
        }
        else if (enumFacing == EnumFacing.SOUTH && block2.isFullBlock() && !block.isFullBlock()) {
            enumFacing = EnumFacing.NORTH;
        }
        else if (enumFacing == EnumFacing.WEST && block3.isFullBlock() && !block4.isFullBlock()) {
            enumFacing = EnumFacing.EAST;
        }
        else if (enumFacing == EnumFacing.EAST && block4.isFullBlock() && !block3.isFullBlock()) {
            enumFacing = EnumFacing.WEST;
        }

        w.setBlockState(coord, state.withProperty(FACING, enumFacing), 2);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        GUIUtils.setTooltip(tooltip, getUnlocalizedName());
    }

}