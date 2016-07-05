package ohirakyou.turtletech.common.block.generators.reflex;
import ohirakyou.turtletech.common.tileentity.TileEntityPoweredMachine;

public class RedstoneReflexGeneratorTileEntity extends TileEntityPoweredMachine {

    private static final long ENERGY_IO = 10L;
    private static final int POWERED_STATE_DURATION = 10;

    private boolean previouslyRedstonePowered;
    private int remainingPoweredStateTicks;


    public RedstoneReflexGeneratorTileEntity() {
        super(RedstoneReflexGeneratorTileEntity.class.getSimpleName());
        becomePureEnergyGenerator(ENERGY_IO);
    }


    @Override
    public final void tickUpdate(boolean isServerWorld) {
        super.tickUpdate(isServerWorld);

        if (isServer()) {
            if (!previouslyRedstonePowered && hasRedstoneSignal()) {
                generateAndDistributeEnergy();
                remainingPoweredStateTicks = POWERED_STATE_DURATION;
            }

            int previousPoweredTicks = remainingPoweredStateTicks;
            remainingPoweredStateTicks = Math.max(remainingPoweredStateTicks - 1, 0);

            // Has the generator gone too long without a redstone pulse?
            if (previousPoweredTicks > 0 && remainingPoweredStateTicks == 0) {
                // Tell the client about it so visuals can be updated
                sync();
            }

        }

        previouslyRedstonePowered = hasRedstoneSignal();
    }


    @Override
    public boolean isPowered() {
        return remainingPoweredStateTicks > 0;
    }

}
