package ohirakyou.turtletech.common.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;


public abstract class TileEntitySimple extends TileEntity implements ITickable {

    private final String unlocalizedName;

    private final int powerUpdateInterval = 8;

    private NBTTagCompound oldSyncCompound = new NBTTagCompound();


    public TileEntitySimple(String unlocalizedName){
        this.unlocalizedName = unlocalizedName;
    }

    public String getUnlocalizedName(){
        return unlocalizedName;
    }


    /** Tells Minecraft to synchronize this tile entity */
    protected final void sync() {
        this.markDirty();
        SPacketUpdateTileEntity packet = this.getUpdatePacket();

        if (packet == null) return;

        for (EntityPlayer player : getWorld().playerEntities){
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP)player;
                playerMP.connection.sendPacket(packet);
            }
        }
    }

    /** Sends a one-time update, such as resetting the life of client-side visual effects. */
    protected final void sendSyncTag(NBTTagCompound updateTag) {
        SPacketUpdateTileEntity packet =  new SPacketUpdateTileEntity(getPos(), 0, updateTag);

        for (EntityPlayer player : getWorld().playerEntities){
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP)player;
                playerMP.connection.sendPacket(packet);
            }
        }
    }

    /**
     * Gets NBT data to be automatically synced at a regular interval.
     * <p>
     * The client often only needs a mostly full data sync, provided by getUpdateTag, when loading a chunk.
     * After that, the client can infer data and be slightly wrong without significant consequence.
     * This sync should be used for major state changes, leaving the rest to be inferred by the client.
     * <p>
     * For example, a furnace or crusher might need to notify clients about its progress when they load in.
     * However, after that, the client can simply keep track of the progress by itself without actually
     * performing any action once it thinks the progress is finished until the server says it's OK to do so.
     * In this case, a significant state change to be automatically synced here might be an inventory slot update
     * once the server recognizes that the process has been truly finished.
     *
     * @return the NBT compound to be checked for inequality and then, if necessary, synced
     */
    private NBTTagCompound getSyncTag() {
        return writeSyncNBT(getBaseUpdateTag());
    }

    /**
     * Override this to sync data saved to the returned NBT compound.
     *
     * @param root  the NBT compound to be synced
     * @return the compound, with data to be synced written to it
     */
    protected NBTTagCompound writeSyncNBT(NBTTagCompound root) {
        return root;
    }


    /**
     * Gets a tag with coordinates as a base for one-time updates.
     *
     * The information written identifies the receiving entity.
     * Without this information, which includes coordinates, syncing will fail.
     *
     * @return a basic template tag from TileEntity that contains entity-identifying information for syncing
     */
    protected final NBTTagCompound getBaseUpdateTag() {
        return super.getUpdateTag();
    }

    /**
     * Override to keep the tile entity from being deleted each time the blockstate is updated
     * @param world world instance
     * @param pos coordinate
     * @param oldState State before change
     * @param newSate State after change
     * @return true if this TileEntity should be invalidated (deleted and recreated), false otherwise
     */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public final void update() {
        this.tickUpdate(this.isServer());
        if(this.isServer() && ((this.getWorld().getTotalWorldTime() + this.getTickOffset()) % powerUpdateInterval == 0)){
            this.staggeredServerUpdate();
        }
    }

    /** Used to sync data like energy and perform other state changes at a regular interval. */
    public void staggeredServerUpdate() {
        NBTTagCompound newSyncCompound = getSyncTag();

        // Automatically detect when a sync is needed
        if (!(oldSyncCompound.equals(newSyncCompound))) {
            sendSyncTag(newSyncCompound);
            oldSyncCompound = (NBTTagCompound)newSyncCompound.copy();
        }
    }

    /**
     * Returns a number that is used to spread the power updates in a chunk
     * across multiple ticks. Also keeps adjacent conductors from updating in
     * the same tick.
     * @return
     */
    private final int getTickOffset(){
        BlockPos coord = this.getPos();
        int x = coord.getX();
        int y = coord.getY();
        int z = coord.getZ();
        return ((z & 1) << 2) | ((x & 1) << 1) | ((y & 1) );
    }


    /** Called once every world tick, for machine logic. */
    public abstract void tickUpdate(boolean isServerWorld);


    /**
     * Returns false if this code is executing on the client and true if this
     * code is executing on the server
     * @return true if on server world, false otherwise
     */
    public boolean isServer(){
        return !this.isClient();
    }
    /**
     * Returns true if this code is executing on the client and false if this
     * code is executing on the server
     * @return false if on server world, true if on client
     */
    public boolean isClient(){
        return this.getWorld().isRemote;
    }

    public boolean isNether() {
        return this.getWorld().provider instanceof WorldProviderHell;
    }

    public IBlockState getDownState() {
        return getWorld().getBlockState(getPos().down());
    }

    public Block getDownBlock() {
        return getDownState().getBlock();
    }

    public IBlockState getUpState() {
        return getWorld().getBlockState(getPos().up());
    }

    public Block getUpBlock() {
        return getUpState().getBlock();
    }

}
