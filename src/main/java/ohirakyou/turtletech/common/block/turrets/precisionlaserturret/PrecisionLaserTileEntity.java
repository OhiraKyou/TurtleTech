package ohirakyou.turtletech.common.block.turrets.precisionlaserturret;

import cyano.electricadvantage.init.Power;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ohirakyou.turtletech.client.sound.ModSounds;
import ohirakyou.turtletech.common.tileentity.TileEntityTurret;
import ohirakyou.turtletech.config.ConfigLongs;
import ohirakyou.turtletech.util.MathUtils;
import ohirakyou.turtletech.util.SimpleRotation;

import java.util.ArrayList;
import java.util.List;

public class PrecisionLaserTileEntity extends TileEntityTurret {

    public static final int LASER_CHARGE_TIME = 30;
    public static final int LASER_COOLDOWN_TIME = 30;
    public static final int LASER_LIFE = 7;
    public static final float LASER_DAMAGE = 7f;

    public static long energyPerShot, energyCapacity, energyInputPerTick;


    //public Entity target;
    public float chargeLevel = 0;

    private boolean previouslyActive;


    // Rendering
    public static final float IDLE_TICK_DEGREES = 0.75f;
    public static final float AGGRESSIVE_TICK_DEGREES = 4f;
    public static final float POWERING_UP_OR_DOWN_TICK_DEGREES = 2f;
    public static final float POWERED_DOWN_PITCH = -15f;

    public static final float LASER_ROTATION_PER_TICK = 32f;

    public float previousLaserRotation;
    public float currentLaserRotation;

    public float previousLaserLife;
    public float currentLaserLife;

    public double laserLength;


    public PrecisionLaserTileEntity() {
        super(PrecisionLaserTileEntity.class.getSimpleName(), 30f, 15f, 32f);

        loadConfig();

        shotCooldownDuration = LASER_COOLDOWN_TIME;
        shotChargeDuration = LASER_CHARGE_TIME;

        // Start in a powered-down state
        previousYaw = 0;
        currentYaw = 0;
        targetYaw = 0;
        previousPitch = POWERED_DOWN_PITCH;
        currentPitch = POWERED_DOWN_PITCH;
        targetPitch = POWERED_DOWN_PITCH;
    }

    private void loadConfig() {
        energyPerShot = 250L;// ConfigLongs.ENERGY_USE_PRECISION_LASER_TURRET.value;
        energyCapacity = energyPerShot * 2L;
        energyInputPerTick = energyPerShot / LASER_COOLDOWN_TIME * 2L;

        becomeBufferedEnergyConsumer(energyCapacity, energyInputPerTick, energyPerShot);
    }

    @Override
    public final void tickUpdate(boolean isServerWorld) {
        super.tickUpdate(isServerWorld);

        previousLaserRotation = currentLaserRotation;
        currentLaserRotation += LASER_ROTATION_PER_TICK;
        if (previousLaserRotation > 360f) { previousLaserRotation -= 360f; }
        if (currentLaserRotation > 360f) { currentLaserRotation -= 360f; }

        previousLaserLife = currentLaserLife;
        if (currentLaserLife > 0) { currentLaserLife--; }



        if (targetLocked && isActive() && shotReady()){
            fire();
        }

        // Syncs if newly active
        updateActivity();

        if (isClient()) {
            // Saving the previous state here ensures that it's updated even if the barrel doesn't rotate
            previousYaw = currentYaw;
            previousPitch = currentPitch;

            //if (!isFiring()) {rotateBarrel();}
            rotateBarrel();
        }
    }


    /**
     * Syncs when this becomes active.
     * <p>
     * Energy is not always synced. As a result, machines sometimes look inactive when they have plenty of energy.
     * This function syncs when an important visual update is necessary.
     */
    protected void updateActivity() {
        if (isServer()) {
            if (!previouslyActive && isActive()) {
                //TurtleTech.logger.info("Syncing");
                sync();
            }

            previouslyActive = isActive();
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return super.receiveClientEvent(id, type);
    }

    protected void validateTarget() {
        if (targetLocked){
            Entity e = getWorld().getEntityByID(targetID);
            if (e == null || e.isDead){
                setTarget(null);
                return;
            }

            if (getOpticPosition().distanceTo(e.getPositionVector()) > forwardRange){
                setTarget(null);
                return;
            }

            if (!canSeeEntity(e)){
                setTarget(null);
                return;
            }
        }
    }


    //todo Move turret base position check from the TESR to here.
    /*
    private void updateAttachmentPoint() {
        if (attachmentBlock == null) {
            BlockPos thisPos = getPos();
            BlockPos upPos = MathUtils.getUpPos(thisPos);
            BlockPos downPos = MathUtils.getDownPos(thisPos);

            if (!getWorld().isAirBlock(downPos)) {
                attachmentBlock = getWorld().getBlockState(downPos).getBlock();
            } else if (!getWorld().isAirBlock(upPos)) {
                attachmentBlock = getWorld().getBlockState(upPos).getBlock();
            }
        }
    }*/


    private void rotateBarrel() {
        if (isClient()) {
            // Get target rotation and max degrees to turn
            float degreesPerTick;

            if (isActive()) {
                if (targetLocked) {
                    // Aggressive
                    degreesPerTick = AGGRESSIVE_TICK_DEGREES;
                    Entity target = getWorld().getEntityByID(targetID);

                    if (target != null) {
                        // Face target
                        SimpleRotation simulatedLookAt = simulateLookAt(target);
                        targetPitch = simulatedLookAt.pitch;
                        targetYaw = simulatedLookAt.yaw;
                        //TurtleTech.logger.info("Target locked. Turning to rotate at degrees: " + targetYaw);
                    } else {
                        //TurtleTech.logger.info("Target is null in barrel rotation");
                    }
                } else {
                    // Idle
                    degreesPerTick = IDLE_TICK_DEGREES;

                    targetPitch = 0;

                    // Toggle idle direction
                    if (currentYaw == targetYaw) {
                        if (currentPitch == targetPitch) {
                            if (targetYaw == 0) { targetYaw = hRange; }
                            else { targetYaw = hRange * MathUtils.sign(targetYaw) * -1; }
                        } else {
                            // Waking up from a powered down state. Give it some extra speed
                            degreesPerTick = POWERING_UP_OR_DOWN_TICK_DEGREES;
                        }
                    }
                }
            } else {
                // Pitch down to visually convey a powered down state
                targetYaw = 0;
                targetPitch = POWERED_DOWN_PITCH;
                degreesPerTick = POWERING_UP_OR_DOWN_TICK_DEGREES;
            }

            // Apply rotation
            float yawDifference = targetYaw - currentYaw;
            float yawThisTick =  Math.min(degreesPerTick, Math.abs(yawDifference)) * MathUtils.sign(yawDifference);

            float pitchDifference = targetPitch - currentPitch;
            float pitchThisTick = Math.min(degreesPerTick, Math.abs(pitchDifference)) * MathUtils.sign(pitchDifference);

            currentYaw = MathUtils.clampSymmetrical(yawThisTick + currentYaw, hRange);
            currentPitch = MathUtils.clampSymmetrical(pitchThisTick + currentPitch, vRange);
            //TurtleTech.logger.info("Calculated new turret rotation. Yaw: " + currentYaw + ", pitch: " + currentPitch);

        }
    }


    @Override
    public boolean fire() {
        World w = getWorld();

        if (isActive() && shotReady()) {

            if (isServer()) {
                List<Entity> victims = getLaserAttackVictims();
                Entity e = w.getEntityByID(targetID);

                for (Entity v : victims) {
                    hurtVictim(v);
                }

                if (e == null || e.isDead){
                    setTarget(null);
                }

                spendEnergy(energyPerShot);

                currentLaserLife = LASER_LIFE;

                sync();

                ModSounds.playPrecisionLaserShot(getWorld(), getPos());
            }


            laserLength = getOpticPosition().distanceTo(calculateLaserTerminus());
            shotCooldownRemaining = shotCooldownDuration;

            return true;
        }

        return false;
    }

    private void hurtVictim(Entity e){
        if (e instanceof EntityLivingBase){
            ((EntityLivingBase)e).attackEntityFrom(Power.laser_damage, LASER_DAMAGE);
            BlockPos bp = e.getPosition();

            if (playerOwner != null && !playerOwner.isEmpty()){
                EntityPlayer p = getWorld().getPlayerEntityByName(playerOwner);
                if (p != null){
                    ((EntityLivingBase) e).setRevengeTarget(p);
                }
            }
        }
    }


    private List<Entity> getLaserAttackVictims(){
        final World w = getWorld();
        final Entity e = w.getEntityByID(targetID);
        final Vec3d origin = getOpticPosition();
        final Vec3d dir;

        SimpleRotation simulatedLookAt = simulateLookAt(e);
        float simulatedPitch = simulatedLookAt.pitch;
        float simulatedYaw = simulatedLookAt.yaw;

        if (e != null){
            dir = e.getPositionVector().addVector(0, 0.5 * e.height, 0).subtract(origin).normalize();
        } else {
            dir = (new Vec3d(Math.cos(DEGREES_TO_RADIANS * simulatedYaw),Math.sin(simulatedPitch),Math.sin(simulatedYaw))).normalize();
        }

        final Vec3d terminus = MathUtils.followRayToSolidBlock(getWorld(), origin, dir, forwardRange);
        final double maxDistSqr = origin.squareDistanceTo(terminus);
        final double maxDist = MathHelper.sqrt_double(maxDistSqr);

        List<Entity> potentialVictims = w.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(
                origin.xCoord - maxDist, origin.yCoord - maxDist, origin.zCoord - maxDist,
                origin.xCoord + maxDist, origin.yCoord + maxDist, origin.zCoord + maxDist));
        List<Entity> victims = new ArrayList<>();
        for (Entity v : potentialVictims) {
            if (origin.squareDistanceTo(v.getPositionVector()) < maxDistSqr
                    && rayIntersectsBoundingBox(origin,dir,v.getEntityBoundingBox())){
                victims.add(v);
            }
        }
        return victims;
    }

    private Vec3d calculateLaserTerminus(){
        float correctedYaw = MathUtils.changeYawRelativity(EnumFacing.EAST, getFacing(), targetYaw);

        Vec3d dir;
        World w = getWorld();
        Entity e = w.getEntityByID(targetID);
        if (e != null){
            dir = e.getPositionVector().addVector(0, 0.5*e.height, 0).subtract(this.getOpticPosition()).normalize();
        } else {
            dir = (new Vec3d(Math.cos(DEGREES_TO_RADIANS * correctedYaw),Math.sin(currentPitch),Math.sin(correctedYaw))).normalize();
        }
        return MathUtils.followRayToSolidBlock(getWorld(), getOpticPosition(), dir, forwardRange);
    }


    final private int renderRange = (int)forwardRange + 1;
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        //todo Find out how often this is used. Possible optimization by expanding only while firing.
        return new AxisAlignedBB(getPos().add(-renderRange, -renderRange, -renderRange), getPos().add(renderRange, renderRange, renderRange));
    }


    @Override
    public boolean isActive(){
        return super.isActive() || isFiring();
    }

    @Override
    public boolean isPowered(){
        return hasEnergy(energyPerShot);
    }

    @Override
    public boolean isFiring() {
        return currentLaserLife > 0;
    }



    // Data management

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);

        NBTTagCompound tag = packet.getNbtCompound();

        if (tag.hasKey("laserlife")){
            currentLaserLife = tag.getFloat("laserlife");
            previousLaserLife = currentLaserLife;
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound root = super.getUpdateTag();

        if (currentLaserLife == LASER_LIFE) {
            root.setFloat("laserlife", currentLaserLife);
        }

        return root;
    }

}
