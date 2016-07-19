package ohirakyou.turtletech.common.block.generators.solar;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.tileentity.TileEntityPoweredMachine;

public class SolarGeneratorTileEntity extends TileEntityPoweredMachine {
    private static final float PI = (float)Math.PI;
    private static final float SUM_OF_SINE_TICKS = 24000 / PI;

    public static float solarPerDay, thunderModifier, rainModifier;

    public static final float MAX_SOLAR_OUTPUT = PI * solarPerDay / SUM_OF_SINE_TICKS;


    public SolarGeneratorTileEntity() {
        super(SolarGeneratorTileEntity.class.getSimpleName());
        becomePureEnergyGenerator(Long.MAX_VALUE);
        loadConfig();
    }

    protected void loadConfig() {
        solarPerDay = 12500;
        thunderModifier = 0.2f;
        rainModifier = 0.35f;
    }

    @Override
    public void tickUpdate(boolean isServerWorld) {
        super.tickUpdate(isServerWorld);

        if (isServer()) {
            if (exposedToSky()){
                // Generate energy
                long energy = (long)Math.max(0, MAX_SOLAR_OUTPUT * getLight());
                generateAndDistributeEnergy(energy);
            }
        }
    }

    public boolean exposedToSky() {
        World w = getWorld();
        return w.canSeeSky(pos) && !w.provider.getHasNoSky();
    }

    public float getLight() {
        World w = getWorld();

        float light = MathHelper.sin((float)w.getWorldTime() * PI / 12000f); // (worldTime * pi / 12000)

        // Handle weather
        if (w.isThundering()) {light *= thunderModifier;}
        else if (w.isRaining()) {light *= rainModifier;}

        return light;
    }

    @Override
    public boolean isPowered() {
        return super.isPowered() && exposedToSky() && getLight() > 0;
    }
}
