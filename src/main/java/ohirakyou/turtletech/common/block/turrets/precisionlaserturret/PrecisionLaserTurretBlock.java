package ohirakyou.turtletech.common.block.turrets.precisionlaserturret;

import net.darkhax.tesla.api.ITeslaHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import ohirakyou.turtletech.common.block.turrets.BlockTurret;

import net.darkhax.tesla.capability.TeslaCapabilities;
import ohirakyou.turtletech.common.material.Materials;
import ohirakyou.turtletech.util.ChatUtils;

import javax.annotation.Nullable;
import java.util.List;

public class PrecisionLaserTurretBlock extends BlockTurret {
    private final AxisAlignedBB bounds = new AxisAlignedBB(0, 0, 0, 1f, 0.1f, 1f);

    public PrecisionLaserTurretBlock() {
        super(Materials.cast_iron);
    }

    @Override
    public PrecisionLaserTileEntity createNewTileEntity(World world, int meta) {
        return new PrecisionLaserTileEntity();
    }

    public boolean onBlockActivated(
            World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
            @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        if(player.isSneaking()) {
            return super.onBlockActivated(worldIn, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
        }

        if (!worldIn.isRemote) {
            final TileEntity tile = worldIn.getTileEntity(pos);

            if (tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, side)) {
                final ITeslaHolder holder = tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, side);

                ChatUtils.sendSpamlessMessage(1337, new TextComponentString(I18n.format(
                        "Stored tesla: " + holder.getStoredPower() + " / " + holder.getCapacity()
                )));
            }
        }

        return true;
    }



    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState bs, final IBlockAccess world, final BlockPos coord) {
        return bounds;
    }

    @Override
    public void addCollisionBoxToList(final IBlockState bs, final World world, final BlockPos coord,
                                      final AxisAlignedBB box, final List<AxisAlignedBB> collisionBoxList,
                                      final Entity entity) {

        super.addCollisionBoxToList(coord, box, collisionBoxList, bounds);
    }
}
