package ohirakyou.turtletech.common.capability;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic Tesla container that serves as a consumer, producer and holder. Custom
 * implementations do not need to use all three. The INBTSerializable interface is also
 * optional.
 */
public class BasicTeslaContainer implements ITeslaConsumer, ITeslaProducer, ITeslaHolder, INBTSerializable<NBTTagCompound> {

    /** The amount of stored Tesla power. */
    private long stored;

    /** The maximum amount of Tesla power that can be stored. */
    private long capacity;

    /** The maximum amount of Tesla power that can be accepted. */
    private long inputRate;

    /** The maximum amount of Tesla power that can be extracted */
    private long outputRate;

    private long inputThisTick;

    private long outputThisTick;

    /**
     * Consumed before dipping into the buffer.
     * <p>
     * Although time is represented by ticks, each called from objects in a sequence, this excess power simulates
     * power than can be generated and consumed simultaneously.
     * <p>
     * Thus, as long as a generator produces power per tick equal to or greater than the amount that attached machines
     * consume per tick, the generator can power those machines without reducing its stored power. This also means that
     * the amount of power generated per tick remains constant, even when close to or at the capacity limit.
     * <p>
     * For example, a generator with 390 / 400 power, generating 50 power per tick, attached to machines consuming
     * a total of 45 per tick, will have 395 (50 - 45 + 390) power by the end of the tick.
     */
    private long excessPowerThisTick;

    public long getExcessPowerThisTick() {
        return excessPowerThisTick;
    }


    public BasicTeslaContainer() {}

    /**
     * Most useful for creating a pure generator (one with no buffer).
     *
     * @param IORate the max input and output
     */
    public BasicTeslaContainer(long IORate) {
        this(0, IORate);
    }


    /**
     * Most useful for creating a generator with a buffer.
     *
     * @param capacity  the buffer size
     * @param IORate  the max input and output
     */
    public BasicTeslaContainer(long capacity, long IORate) {
        this(capacity, IORate, IORate);
    }

    /**
     * Constructor for setting the basic values. Will not construct with any stored power.
     *
     * @param capacity The maximum amount of Tesla power that the container should hold.
     * @param input The maximum rate of power that can be accepted at a time.
     * @param output The maximum rate of power that can be extracted at a time.
     */
    public BasicTeslaContainer(long capacity, long input, long output) {
        this(0, capacity, input, output);
    }

    /**
     * Constructor for setting all of the base values, including the stored power.
     *
     * @param power The amount of stored power to initialize the container with.
     * @param capacity The maximum amount of Tesla power that the container should hold.
     * @param input The maximum rate of power that can be accepted at a time.
     * @param output The maximum rate of power that can be extracted at a time.
     */
    public BasicTeslaContainer(long power, long capacity, long input, long output) {
        this.stored = power;
        this.capacity = capacity;
        this.inputRate = input;
        this.outputRate = output;
    }

    public void startNewTick() {
        inputThisTick = 0;
        outputThisTick = 0;
        excessPowerThisTick = 0;
    }

    /**
     * Constructor for creating an instance directly from a compound tag. This expects that the
     * compound tag has some of the required data. @See {@link #deserializeNBT(NBTTagCompound)}
     * for precise info on what is expected. This constructor will only set the stored power if
     * it has been written on the compound tag.
     *
     * @param dataTag The NBTCompoundTag to read the important data from.
     */
    public BasicTeslaContainer(NBTTagCompound dataTag) {
        this.deserializeNBT(dataTag);
    }

    @Override
    public long getStoredPower () {
        return this.stored;
    }


    public long getUsablePower() {
        return stored + excessPowerThisTick;
    }



    public long givePower (long Tesla) {
        return givePower(Tesla, false);
    }

    @Override
    public long givePower (long Tesla, boolean simulated) {
        if (Tesla == 0) { return 0; }

        // How much power this could, in theory, get this tick
        final long potentialInput = Math.max(inputRate - inputThisTick, 0);

        // How much power this will get, assuming it all fits
        final long possibleInput = Math.min(potentialInput, Tesla);

        final long freeCapacity = this.capacity - this.stored;

        // The final power, without overflow
        final long acceptedTesla = Math.min(freeCapacity, possibleInput);

        if (!simulated) {
            this.stored += acceptedTesla;
            this.inputThisTick += acceptedTesla;
        }

        return acceptedTesla;
    }



    public long takePower (long Tesla) {
        return takePower(Tesla, false);
    }

    @Override
    public long takePower (long Tesla, boolean simulated) {
        if (Tesla == 0) { return 0; }

        final long potentialOutput = Math.max(outputRate - outputThisTick, 0);
        final long possibleOutput = Math.min(potentialOutput, Tesla);

        final long removedPower = Math.min(this.stored + excessPowerThisTick, possibleOutput);
        final long removedStoredPower = Math.max(removedPower - excessPowerThisTick, 0);
        final long removedExcessPower = removedPower - removedStoredPower;

        if (!simulated) {
            this.stored -= removedStoredPower;
            this.excessPowerThisTick -=  removedExcessPower;
            this.outputThisTick += removedPower;
        }

        return removedPower;
    }



    public long generatePower() {
        return generatePower(inputRate);
    }

    public long generatePower(long Tesla) {
        return generatePower(Tesla, false);
    }

    public long generatePower(long Tesla, boolean simulated) {
        final long freeCapacity = this.capacity - this.stored;

        // The final power, without overflow
        final long acceptedTesla = Math.min(freeCapacity, Tesla);
        final long excessTesla = Tesla - acceptedTesla;

        if (!simulated) {
            this.stored += acceptedTesla;
            this.excessPowerThisTick += excessTesla;
        }

        return Tesla;
    }



    @Override
    public long getCapacity () {
        return this.capacity;
    }

    @Override
    public NBTTagCompound serializeNBT () {
        final NBTTagCompound dataTag = new NBTTagCompound();
        dataTag.setLong("TeslaPower", this.stored);
        dataTag.setLong("TeslaCapacity", this.capacity);
        dataTag.setLong("TeslaInput", this.inputRate);
        dataTag.setLong("TeslaOutput", this.outputRate);

        return dataTag;
    }

    @Override
    public void deserializeNBT (NBTTagCompound nbt) {
        this.stored = nbt.getLong("TeslaPower");

        if (nbt.hasKey("TeslaCapacity"))
            this.capacity = nbt.getLong("TeslaCapacity");

        if (nbt.hasKey("TeslaInput"))
            this.inputRate = nbt.getLong("TeslaInput");

        if (nbt.hasKey("TeslaOutput"))
            this.outputRate = nbt.getLong("TeslaOutput");

        if (this.stored > this.capacity)
            this.stored = this.capacity;
    }

    /**
     * Sets the capacity of the the container. If the existing stored power is more than the
     * new capacity, the stored power will be decreased to match the new capacity.
     *
     * @param capacity The new capacity for the container.
     * @return The instance of the container being updated.
     */
    public BasicTeslaContainer setCapacity (long capacity) {
        this.capacity = capacity;

        if (this.stored > capacity)
            this.stored = capacity;

        return this;
    }

    public BasicTeslaContainer setIORate(long IORate) {
        setInputRate(IORate);
        setOutputRate(IORate);
        return this;
    }

    /**
     * Gets the maximum amount of Tesla power that can be accepted by the container.
     *
     * @return The amount of Tesla power that can be accepted at any time.
     */
    public long getInputRate () {
        return this.inputRate;
    }

    /**
     * Sets the maximum amount of Tesla power that can be accepted by the container.
     *
     * @param rate The amount of Tesla power to accept at a time.
     * @return The instance of the container being updated.
     */
    public BasicTeslaContainer setInputRate (long rate) {
        this.inputRate = rate;
        return this;
    }

    /**
     * Gets the maximum amount of Tesla power that can be pulled from the container.
     *
     * @return The amount of Tesla power that can be extracted at any time.
     */
    public long getOutputRate () {
        return this.outputRate;
    }

    /**
     * Sets the maximum amount of Tesla power that can be pulled from the container.
     *
     * @param rate The amount of Tesla power that can be extracted.
     * @return The instance of the container being updated.
     */
    public BasicTeslaContainer setOutputRate (long rate) {
        this.outputRate = rate;
        return this;
    }

    /**
     * Sets both the input and output rates of the container at the same time. Both rates will
     * be the same.
     *
     * @param rate The input/output rate for the Tesla container.
     * @return The instance of the container being updated.
     */
    public BasicTeslaContainer setTransferRate (long rate) {
        this.setInputRate(rate);
        this.setOutputRate(rate);
        return this;
    }

    public BasicTeslaContainer setPower (long power) {
        this.stored = power;
        return this;
    }


    public <T> List<T> getConnectedCapabilities (Capability<T> capability, World world, BlockPos pos) {
        final List<T> capabilities = new ArrayList<T>();

        for (final EnumFacing side : EnumFacing.values()) {

            final TileEntity tile = world.getTileEntity(pos.offset(side));

            if (tile != null && !tile.isInvalid() && tile.hasCapability(capability, side.getOpposite())) {
                final T newCapability = tile.getCapability(capability, side.getOpposite());

                if (newCapability != null) {
                    capabilities.add(newCapability);
                }
            }
        }

        return capabilities;
    }


    /**
     * Distributes power first from the excess power this tick, followed by the buffer.
     *
     * @param world
     * @param pos
     * @return
     */
    public long distributePowerFromBuffer (World world, BlockPos pos) {
        if (getUsablePower() <= 0) {return 0;}

        long potentialOutput = Math.max(outputRate - outputThisTick, 0);
        final long possibleOutput = Math.min(potentialOutput, getUsablePower());

        final long spentPower = distributePowerEvenlyToAllFaces(world, pos, possibleOutput);
        takePower(spentPower);

        return spentPower;
    }

    public long distributePowerEvenlyToAllFaces (World world, BlockPos pos) {
        return distributePowerEvenlyToAllFaces(world, pos, false);
    }

    public long distributePowerEvenlyToAllFaces (World world, BlockPos pos, boolean simulated) {
        long amount = Math.max(outputRate - outputThisTick, 0);

        return distributePowerEvenlyToAllFaces(world, pos, amount, simulated);
    }

    public long distributePowerEvenlyToAllFaces (World world, BlockPos pos, long amount) {
        return distributePowerEvenlyToAllFaces(world, pos, amount, false);
    }

    public long distributePowerEvenlyToAllFaces (World world, BlockPos pos, long amount, boolean simulated) {
        if (amount == 0) return 0;

        List<ITeslaConsumer> connectedCapabilities = getConnectedCapabilities(TeslaCapabilities.CAPABILITY_CONSUMER, world, pos);
        long consumedPower = 0L;

        if (connectedCapabilities.size() > 0) {
            long amountPerSide = amount / connectedCapabilities.size();

            for (final ITeslaConsumer consumer : connectedCapabilities) {
                consumedPower += consumer.givePower(amountPerSide, simulated);
            }
        }

        return consumedPower;
    }




    public long fillBufferFromAllFaces (World world, BlockPos pos) {
        // How much power this could, in theory, get this tick
        final long potentialInput = Math.max(inputRate - inputThisTick, 0);

        final long freeCapacity = this.capacity - this.stored;

        // How much power this will get, assuming it all fits
        final long possibleInput = Math.min(potentialInput, freeCapacity);

        // The final power, without overflow
        final long acceptedTesla = consumePowerFromAllFaces(world, pos, possibleInput);

        givePower(acceptedTesla);

        return acceptedTesla;
    }

    public long consumePowerFromAllFaces (World world, BlockPos pos, long amount) {
        return consumePowerFromAllFaces(world, pos, amount, false);
    }

    public long consumePowerFromAllFaces (World world, BlockPos pos, long amount, boolean simulated) {
        long receivedPower = 0L;

        for (final ITeslaProducer producer : getConnectedCapabilities(TeslaCapabilities.CAPABILITY_PRODUCER, world, pos)) {
            receivedPower += producer.takePower(amount, simulated);
        }

        return receivedPower;
    }
}