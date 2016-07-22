package ohirakyou.turtletech.common.block.generators.solar;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.tileentity.TileEntityPoweredMachine;
import ohirakyou.turtletech.util.WorldUtils;

public class SolarGeneratorTileEntity extends TileEntityPoweredMachine {
    protected static long clearSolarOutput, rainSolarOutput, thunderSolarOutput;
    protected static int clearOutputTicks, rainOutputTicks, thunderOutputTicks;

    private int ticksBetweenOutput;


    public SolarGeneratorTileEntity() {
        super(SolarGeneratorTileEntity.class.getSimpleName());
        becomePureEnergyGenerator(Long.MAX_VALUE);
        loadConfig();
    }

    protected void loadConfig() {
        // Energy output
        clearSolarOutput = 1;
        rainSolarOutput = 1;
        thunderSolarOutput = 0;

        // Ticks between energy pulses
        clearOutputTicks = 0;
        rainOutputTicks = 1;
        thunderOutputTicks = 0;
    }

    @Override
    public void tickUpdate(boolean isServerWorld) {
        super.tickUpdate(isServerWorld);

        if (isServer()) {
            // Don't bother ticking when there's no energy to be had
            long energyThisTick = getSolarEnergy();

            if (energyThisTick > 0 && exposedToDaylight()){

                if (ticksBetweenOutput >= getTicksToOutput()) {
                    ticksBetweenOutput = 0;

                    // Generate energy
                    generateAndDistributeEnergy(energyThisTick);
                }
                else {
                    ticksBetweenOutput++;
                }
            }
        }
    }

    public boolean exposedToDaylight() {
        World w = getWorld();
        return w.canSeeSky(pos.offset(EnumFacing.UP)) && !w.provider.getHasNoSky() && WorldUtils.isDaytime(w);
    }

    public int getTicksToOutput() {
        World w = getWorld();

        int ticksToOutput;

        // Handle weather
        if (w.isThundering()) {ticksToOutput = thunderOutputTicks;}
        else if (w.isRaining()) {ticksToOutput = rainOutputTicks;}
        else {ticksToOutput = clearOutputTicks;}

        return ticksToOutput;
    }

    public long getSolarEnergy() {
        World w = getWorld();

        long solarOutput;

        // Handle weather
        if (w.isThundering()) {solarOutput = thunderSolarOutput;}
        else if (w.isRaining()) {solarOutput = rainSolarOutput;}
        else {solarOutput = clearSolarOutput;}

        return solarOutput;
    }

    @Override
    public boolean isPowered() {return super.isPowered() && exposedToDaylight();}
}
