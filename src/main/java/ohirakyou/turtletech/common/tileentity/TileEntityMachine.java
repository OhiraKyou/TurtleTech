package ohirakyou.turtletech.common.tileentity;

import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import ohirakyou.turtletech.TurtleTech;
import ohirakyou.turtletech.common.block.BlockMachine;
import ohirakyou.turtletech.util.ChatUtils;

import java.util.Arrays;

public abstract class TileEntityMachine extends TileEntitySimple implements ITickable {

    public TileEntityMachine(String unlocalizedName){
        super(unlocalizedName);
    }

    public IBlockState getBlockState(){
        return getWorld().getBlockState(getPos());
    }

    public boolean isActive() {
        return !this.hasRedstoneSignal() && getWorld().getBlockState(getPos()).getValue(BlockMachine.ACTIVE);
    }

    protected boolean hasRedstoneSignal() {
        return getWorld().isBlockPowered(getPos());
    }

    public void setActiveState(boolean active) {
        IBlockState oldState = getWorld().getBlockState(getPos());
        if(oldState.getBlock() instanceof BlockMachine
                && oldState.getValue(BlockMachine.ACTIVE) != active ){
            final TileEntity save = this;
            final World w = getWorld();
            final BlockPos pos = this.getPos();
            IBlockState newState = oldState.withProperty(BlockMachine.ACTIVE, active);

            w.setBlockState(pos, newState,3);

            if(save != null){
                w.removeTileEntity(pos);
                save.validate();
                w.setTileEntity(pos, save);
            }
        }
    }

    public float getYaw() {
        return getFacing().getOpposite().getHorizontalAngle();
    }

    public EnumFacing getFacing() {
        IBlockState state = getWorld().getBlockState(getPos());
        if (state.getBlock() instanceof BlockMachine) {
            return state.getValue(BlockMachine.FACING);
        }

        return EnumFacing.NORTH;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

}
