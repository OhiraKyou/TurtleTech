package ohirakyou.turtletech.common.tileentity;

import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import ohirakyou.turtletech.common.block.BlockMachine;
import ohirakyou.turtletech.common.capability.BasicTeslaContainer;

public abstract class TileEntityPoweredMachine extends TileEntityMachine {

    private BasicTeslaContainer teslaContainer;
    public boolean isEnergyConsumer;
    public boolean isEnergyHolder;
    public boolean isEnergyProducer;

    public TileEntityPoweredMachine(String unlocalizedName){
        this(unlocalizedName, 0);
    }

    public TileEntityPoweredMachine(String unlocalizedName, int numInputSlots){
        super(unlocalizedName, numInputSlots);
        teslaContainer = new BasicTeslaContainer();
    }

    @Override
    public void tickUpdate(boolean isServerWorld) {
        if (isServer() && teslaContainer != null) {
            teslaContainer.startNewTick();
        }
    }

    @Override
    public void powerUpdate(){
        super.powerUpdate();
        this.setPoweredState(this.isPowered());
    }



    // Energy

    /**
     * Initializes fields necessary to make a generator that produces, but does not store, energy.
     * @param energyIORate  the rate at which energy is generated and distributed
     */
    public void becomePureEnergyGenerator(long energyIORate) {
        setEnergyIORate(energyIORate);
        isEnergyProducer = true;
    }

    public void becomeBufferedEnergyConsumer(long capacity, long inputRate, long consumptionRate) {
        setEnergyCapacity(capacity);
        setEnergyIORate(inputRate, consumptionRate);
        isEnergyConsumer = true;
        isEnergyHolder = true;
    }

    public boolean hasEnergy() {
        return teslaContainer.getStoredPower() > 0;
    }

    public boolean hasEnergy(long energy) {
        return teslaContainer.getStoredPower() >= energy;
    }

    public long getEnergy() {
        return teslaContainer.getStoredPower();
    }

    public void setEnergy(long energy) {
        teslaContainer.setPower(energy);
    }

    public void setEnergyCapacity(long energy) {
        teslaContainer.setCapacity(energy);
    }

    public void setEnergyIORate(long IORate) {
        teslaContainer.setIORate(IORate);
    }

    public void setEnergyIORate(long inputRate, long outputRate) {
        setEnergyInputRate(inputRate);
        setEnergyOutputRate(outputRate);
    }

    public void setEnergyInputRate(long inputRate) {
        teslaContainer.setInputRate(inputRate);
    }

    public void setEnergyOutputRate(long outputRate) {
        teslaContainer.setOutputRate(outputRate);
    }

    public void spendEnergy(long energy) {
        teslaContainer.takePower(energy);
    }

    public void generateEnergy() {
        teslaContainer.generatePower();
    }

    public void distributeEnergyFromBuffer() {
        teslaContainer.distributePowerFromBuffer(getWorld(), getPos());
    }

    public void generateAndDistributeEnergy() {
        generateEnergy();
        distributeEnergyFromBuffer();
    }



    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
        if (
                capability == TeslaCapabilities.CAPABILITY_CONSUMER && isEnergyConsumer ||
                capability == TeslaCapabilities.CAPABILITY_HOLDER && isEnergyHolder ||
                capability == TeslaCapabilities.CAPABILITY_PRODUCER && isEnergyProducer) {

            return (T) this.teslaContainer;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
        if (
                capability == TeslaCapabilities.CAPABILITY_CONSUMER && isEnergyConsumer ||
                capability == TeslaCapabilities.CAPABILITY_HOLDER && isEnergyHolder ||
                capability == TeslaCapabilities.CAPABILITY_PRODUCER && isEnergyProducer) {

            return true;
        }

        return super.hasCapability(capability, facing);
    }



    public void setPoweredState(boolean powered){
        IBlockState oldState = getWorld().getBlockState(getPos());
        if(oldState.getBlock() instanceof BlockMachine
                && (Boolean)oldState.getValue(BlockMachine.POWERED) != powered ){
            final TileEntity save = this;
            final World w = getWorld();
            final BlockPos pos = this.getPos();
            IBlockState newState = oldState.withProperty(BlockMachine.POWERED, powered);
            w.setBlockState(pos, newState, 3);
            if(save != null){
                w.removeTileEntity(pos);
                save.validate();
                w.setTileEntity(pos, save);
            }
        }
    }

    public abstract boolean isPowered();

    @Override
    public boolean isActive(){
        return super.isActive() && isPowered();
    }


    // Data management
    @Override
    protected void saveTo(NBTTagCompound root) {
        root.setLong("energy", getEnergy());
    }

    @Override
    protected void loadFrom(NBTTagCompound root) {
        if (root.hasKey("energy")) {
            setEnergy(root.getLong("energy"));
            //TurtleTech.logger.info("Received packet. Energy: " + getEnergy());
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);

        NBTTagCompound tag = packet.getNbtCompound();

        if (tag.hasKey("energy")){
            setEnergy(tag.getLong("energy"));
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound root = super.getUpdateTag();
        root.setLong("energy", getEnergy());
        return root;
    }
}
