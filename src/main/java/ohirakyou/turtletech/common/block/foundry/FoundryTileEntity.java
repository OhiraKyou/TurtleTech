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

}
