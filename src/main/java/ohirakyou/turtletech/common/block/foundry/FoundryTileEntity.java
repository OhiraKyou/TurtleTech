package ohirakyou.turtletech.common.block.foundry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ohirakyou.turtletech.common.tileentity.TileEntityMachine;

public class FoundryTileEntity extends TileEntityMachine {

    private final int[] dataArray = new int[0];


    public FoundryTileEntity() {
        super(FoundryTileEntity.class.getSimpleName());
    }


    private int iTest = 0;
    @Override
    public final void tickUpdate(boolean isServerWorld) {
        World w = getWorld();

        if (isActive()) {
            if (iTest >= 20) {
                iTest = 0;
                //System.out.println("Foundry active!");
            }
        }
        iTest++;
    }


    @Override
    public boolean isActive(){
        return true;
    }



    // Data management

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

    @Override
    protected void saveTo(NBTTagCompound root) {
    }

    @Override
    protected void loadFrom(NBTTagCompound root) {
    }

}
