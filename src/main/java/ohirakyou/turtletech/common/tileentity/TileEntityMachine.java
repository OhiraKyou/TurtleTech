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
    public int[] dataArray = new int[0];

    public TileEntityMachine(String unlocalizedName){
        this(unlocalizedName, 0);
    }

    public TileEntityMachine(String unlocalizedName, int numInputSlots){
        super(unlocalizedName, numInputSlots);
    }

    public IBlockState getBlockState(){
        return getWorld().getBlockState(getPos());
    }



    public boolean isActive() {
        return !this.hasRedstoneSignal() && getWorld().getBlockState(getPos()).getValue(BlockMachine.ACTIVE);
    }

    protected boolean hasRedstoneSignal() {
//        TurtleTech.logger.info("hasRedstoneSignal check at " + getPos() +
//                ".  Redstone: " + redstone + ", isBlockPowered: " + getWorld().isBlockPowered(getPos()));
        //return redstone;
        return getWorld().isBlockPowered(getPos());
    }

    private boolean redstone = false;
    private int[] syncArrayOld = null;
    private int[] syncArrayNew = null;

    @Override
    public void powerUpdate() {
        //redstone = getWorld().isBlockPowered(getPos());
        //TurtleTech.logger.info("powerUpdate happening at " + getPos() + ".  Redstone: " + redstone);

        // automatically detect when a sync is needed
        if(syncArrayOld == null || syncArrayNew == null
                || this.getDataFieldArray().length != syncArrayOld.length){
            int size = this.getDataFieldArray().length;
            syncArrayOld = new int[size];
            syncArrayNew = new int[size];
        }
        this.prepareDataFieldsForSync();
        System.arraycopy(this.getDataFieldArray(), 0, syncArrayNew, 0, syncArrayNew.length);
        if(!Arrays.equals(syncArrayOld, syncArrayNew)){
            this.sync();
            System.arraycopy(syncArrayNew, 0, syncArrayOld, 0, syncArrayOld.length);
        }
    }



    public void setActiveState(boolean active) {
        IBlockState oldState = getWorld().getBlockState(getPos());
        if(oldState.getBlock() instanceof BlockMachine
                && (Boolean)oldState.getValue(BlockMachine.ACTIVE) != active ){
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




    // Data management

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagRoot){
        super.writeToNBT(tagRoot);
        saveTo(tagRoot);
        return tagRoot;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagRoot){
        super.readFromNBT(tagRoot);
        loadFrom(tagRoot);
    }

    protected abstract void saveTo(NBTTagCompound tagRoot);
    protected abstract void loadFrom(NBTTagCompound tagRoot);

    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    public EnumFacing getFacing() {
        IBlockState state = getWorld().getBlockState(getPos());
        if (state.getBlock() instanceof BlockMachine) {
            return state.getValue(BlockMachine.FACING);
        }

        return EnumFacing.NORTH;
    }


    @Override
    public int[] getDataFieldArray() {
        return dataArray;
    }

    @Override
    public void prepareDataFieldsForSync() {
        //dataArray[0] = Float.floatToIntBits(getEnergy());
    }

    @Override
    public void onDataFieldUpdate() {
        //this.setAThing(Float.intBitsToFloat(dataArray[0]));
    }
}
