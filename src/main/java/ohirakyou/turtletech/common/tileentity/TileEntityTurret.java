package ohirakyou.turtletech.common.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import ohirakyou.turtletech.util.MathUtils;
import ohirakyou.turtletech.util.SimpleRotation;

import java.util.List;

public abstract class TileEntityTurret extends TileEntityPoweredMachine {
    public static float opticOffset = 1f;

    // Targeting range
    public float hRange;
    public float vRange;
    public float forwardRange;

    private Vec3d opticPosition = null;

    /** Ticks between acquiring a target and firing */
    protected int shotChargeDuration;
    protected int shotCurrentCharge;

    /** Minimum ticks between shots */
    protected int shotCooldownDuration;
    protected int shotCooldownRemaining;
    protected int shotPreviousCooldownRemaining;

    public boolean targetLocked = false;
    public int targetID = Integer.MIN_VALUE;

    protected String teamIdentity = null;
    protected String playerOwner = null;


    public float previousYaw;
    public float previousPitch;
    public float currentYaw;
    public float currentPitch;
    public float targetYaw;
    public float targetPitch;


    public TileEntityTurret(String unlocalizedName, float hRange, float vRange, float forwardRange) {
        super(TileEntityTurret.class.getSimpleName());

        this.hRange = hRange;
        this.vRange = vRange;
        this.forwardRange = forwardRange;
    }

    @Override
    public void tickUpdate(boolean isServerWorld) {
        super.tickUpdate(isServerWorld);

        coolDown();
        acquireTarget();
    }

    public void coolDown() {
        shotPreviousCooldownRemaining = shotCooldownRemaining;

        if (shotCooldownRemaining > 0) {
            shotCooldownRemaining --;
        }
    }


    public void chargeShot() {
        if (shotCurrentCharge > 0) {
            shotCurrentCharge--;
        }
    }


    public void setOwner(EntityPlayer owner){
        this.setTeam(owner.getTeam());
        playerOwner = owner.getName();
    }

    public void setTeam(Team t){
        if(t == null) {
            teamIdentity = null;
        } else {
            teamIdentity = t.getRegisteredName();
        }
    }

    /**
     * Calculates progress toward a fully charged state.
     *
     * @return a value between 0 and 1 indicating charge level
     */
    public float getShotChargeLevel() {
        return shotCurrentCharge / shotChargeDuration;
    }

    public boolean shotReady() {
        return shotCooldownRemaining <= 0 && shotCurrentCharge <= 0;
    }

    protected void acquireTarget() {
        World w = getWorld();

        boolean isActive = isActive();

        if (isServer()) {
            if(isActive){

                if(targetLocked) {
                    chargeShot();
                } else {

                    double x = this.getOpticPosition().xCoord;
                    double y = this.getOpticPosition().yCoord;
                    double z = this.getOpticPosition().zCoord;

                    List<Entity> entities = w.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(
                            x-forwardRange, y-forwardRange, z-forwardRange, x+forwardRange, y+forwardRange, z+forwardRange));
                    final double maxRangeSquared = forwardRange * forwardRange;

                    for (Entity e : entities) {
                        if (e.getPositionVector().squareDistanceTo(getOpticPosition()) > maxRangeSquared) continue;

                        if (e.isCreatureType(EnumCreatureType.MONSTER, false)) {
                            if (canSeeEntity(e)) {
                                setTarget(e);
                                break;
                            }
                        }
                    }
                }

                validateTarget();

            } else {
                // Lose target when inactive
                if(targetLocked){
                    setTarget(null);
                }
            }
        }
    }



    public boolean canSeeEntity(Entity e){
        if (e == null) {return false;}
        if (!targetInRange(e)) {return false;}

        Vec3d direction = e.getPositionVector().subtract(getOpticPosition()).normalize();

        RayTraceResult collision = getWorld().rayTraceBlocks(
                getOpticPosition(direction), e.getPositionVector().addVector(0, e.height * 0.5, 0), true, true, false
        );

        if (collision != null && collision.typeOfHit == RayTraceResult.Type.BLOCK) {
            return false;
        }

        return true;
    }

    private boolean targetInRange(Entity e) {
        if (e == null) return false;

        SimpleRotation simulatedLookAt = simulateLookAt(e);

        return (MathUtils.clampSymmetrical(simulatedLookAt.pitch, vRange) == simulatedLookAt.pitch) &&
                (MathUtils.clampSymmetrical(simulatedLookAt.yaw, hRange) == simulatedLookAt.yaw);
    }

    protected SimpleRotation simulateLookAt(Entity e) {
        Vec3d coords = e.getPositionVector().addVector(0, e.height * 0.5, 0);

        Vec3d pos = getOpticPosition();
        double x = pos.xCoord;
        double y = pos.yCoord;
        double z = pos.zCoord;
        double dist = MathUtils.distance(x, y, z, coords.xCoord, coords.yCoord, coords.zCoord);
        float simulatedYaw = MathUtils.RADIANS_TO_DEGREES * atan2(coords.zCoord - z, coords.xCoord - x);
        float simulatedPitch = MathUtils.RADIANS_TO_DEGREES * asin((coords.yCoord - y) / dist);

        simulatedYaw = MathUtils.changeYawRelativity(getFacing(), EnumFacing.EAST, simulatedYaw);

        return new SimpleRotation(simulatedPitch, 0,  MathUtils.wrapAngle180(simulatedYaw));
    }

    private float atan2(double dy, double dx){
        return (float)Math.atan2(dy, dx);
    }

    private float asin(double d){
        return (float)Math.asin(d);
    }

    public Vec3d getOpticPosition(){
        if (opticPosition == null) { setOpticPosition(); }
        return opticPosition;
    }

    /**
     * Gets the optic position, offset towards a direction.
     * <p>
     * The direction should normally be the direction the barrel is pointed toward.
     *
     * @param direction  scaled to create an offset
     * @return
     */
    public Vec3d getOpticPosition(Vec3d direction){
        return getOpticPosition().add(direction.scale(opticOffset));
    }

    @Override
    public void setPos(BlockPos p){
        super.setPos(p);
        setOpticPosition();
    }

    public final void setOpticPosition(){
        BlockPos p = getPos();
        opticPosition = new Vec3d(p.getX() + 0.5f, p.getY() + 0.5f, p.getZ() + 0.5f);
    }

    public void setTarget(Entity e){
        if(e == null){
            this.targetID = Integer.MIN_VALUE;
            this.targetLocked = false;
            shotCurrentCharge = 0;
        } else {
            this.targetID = e.getEntityId();
            this.targetLocked = true;
            shotCurrentCharge = shotChargeDuration;
        }
        this.sync();
    }


    public static boolean rayIntersectsBoundingBox(Vec3d rayOrigin, Vec3d rayDirection, AxisAlignedBB box){
        if(box == null) {
            return false;
        }
        // algorithm from http://gamedev.stackexchange.com/questions/18436/most-efficient-aabb-vs-ray-collision-algorithms
        Vec3d inverse = new Vec3d(1.0 / rayDirection.xCoord, 1.0 / rayDirection.yCoord, 1.0 / rayDirection.zCoord);
        double t1 = (box.minX - rayOrigin.xCoord)*inverse.xCoord;
        double t2 = (box.maxX- rayOrigin.xCoord)*inverse.xCoord;
        double t3 = (box.minY - rayOrigin.yCoord)*inverse.yCoord;
        double t4 = (box.maxY - rayOrigin.yCoord)*inverse.yCoord;
        double t5 = (box.minZ - rayOrigin.zCoord)*inverse.zCoord;
        double t6 = (box.maxZ - rayOrigin.zCoord)*inverse.zCoord;

        double tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
        double tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));

        // if tmax < 0, ray (line) is intersecting AABB, but whole AABB is behind us
        if (tmax < 0) {return false;}

        // if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax) {return false;}

        return true;
    }

    private static double max(double a, double b){
        return Math.max(a, b);
    }
    private static double min(double a, double b){
        return Math.min(a, b);
    }



    /**
     * Can be used to fire a projectile, perform an action, or make a check.
     * @return  true if turret successfully fired
     */
    public abstract boolean fire();


    @Override
    public boolean isActive(){
        return super.isActive();
    }

    public abstract boolean isFiring();

    protected abstract void validateTarget();


    // Data management

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound root) {
        super.writeToNBT(root);

        root.setBoolean("lock", targetLocked);
        root.setString("team", this.teamIdentity == null ? "" : this.teamIdentity);
        root.setString("player", this.playerOwner == null ? "" : this.playerOwner);

        return root;
    }

    @Override
    public void readFromNBT(NBTTagCompound root) {
        super.readFromNBT(root);

        if (root.hasKey("lock")) {
            targetLocked = root.getBoolean("lock");
        }

        if (root.hasKey("targetID")) {
            this.targetID = root.getInteger("targetID");
        }

        if (root.hasKey("charge")) {
            this.shotCurrentCharge = root.getByte("charge");
        }

        if (root.hasKey("team")) {
            String teamName = root.getString("team");
            if (teamName.isEmpty()){
                this.teamIdentity = null;
            }else{
                this.teamIdentity = teamName;
            }
        }

        if (root.hasKey("player")) {
            playerOwner = root.getString("player");
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound root = super.getUpdateTag();

        root.setBoolean("lock", targetLocked);
        if (targetLocked && targetID != Integer.MIN_VALUE && getWorld().getEntityByID(targetID) != null) {
            root.setInteger("targetID", this.targetID);
            root.setByte("charge", (byte)shotCurrentCharge);
        }

        return root;
    }
}
