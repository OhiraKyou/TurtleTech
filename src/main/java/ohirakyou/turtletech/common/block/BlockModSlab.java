package ohirakyou.turtletech.common.block;

import cyano.basemetals.material.MetalMaterial;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

public class BlockModSlab extends BlockSlab {

    protected final boolean isDoubleSlab;
    public static final PropertyEnum<DummyEnum> DUMMY = PropertyEnum.create("dummy", DummyEnum.class);

    private BlockModSlab singleBlock;
    private BlockModSlab doubleBlock;

    public BlockModSlab getSingleBlock() {return singleBlock;}
    public BlockModSlab getDoubleBlock() {return doubleBlock;}

    public BlockModSlab(MetalMaterial metal, Boolean doubleSlab) {
        this(Material.IRON, doubleSlab);

        setSoundType(SoundType.METAL);
        setHardness(metal.hardness);
        setResistance(metal.getBlastResistance());
        setHarvestLevel("pickaxe", 0);
    }

    public BlockModSlab(Material material, Boolean isDoubleSlab) {
        super(material);

        this.isDoubleSlab = isDoubleSlab;
        if(!isDoubleSlab) {useNeighborBrightness = true;}

        setDefaultState(blockState.getBaseState().withProperty(HALF, EnumBlockHalf.BOTTOM).withProperty(DUMMY, DummyEnum.SINGLETON));
    }

    public BlockModSlab setBlocks(BlockModSlab singleBlock, BlockModSlab doubleBlock) {
        this.singleBlock = singleBlock;
        this.doubleBlock = doubleBlock;

        return this;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF, getVariantProperty());
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (isDoubleSlab) {return getDefaultState();}
        else {return getDefaultState().withProperty(HALF, meta == 8 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);}
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (isDoubleSlab) {return 0;}
        else {return state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0;}
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(getSingleBlock());
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int meta) {
        return Item.getItemFromBlock(getSingleBlock());
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return super.quantityDropped(state, fortune, random);
    }

    @Override
    public ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(getSingleBlock());
    }

    @Override
    public final boolean isDouble() {
        return isDoubleSlab;
    }

    @Nonnull
    @Override
    public final IProperty getVariantProperty() {
        return DUMMY; // Vanilla expects us to store different kinds of slabs into one block ID. Except we don't. We need this dummy property and dummy value to satisfy it.
    }

    @Nonnull
    @Override
    public final Comparable<?> getTypeForItem(@Nonnull ItemStack stack) {
        return DummyEnum.SINGLETON;
    }

    public enum DummyEnum implements IStringSerializable {
        SINGLETON {
            @Nonnull
            @Override
            public String getName() {
                return name().toLowerCase(Locale.ROOT);
            }
        }
    }

}
