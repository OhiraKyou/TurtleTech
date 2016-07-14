package ohirakyou.turtletech.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorContainer;
import ohirakyou.turtletech.common.block.generators.soul.SoulTurbineGeneratorTileEntity;
import ohirakyou.turtletech.data.DataGuiIDs;

public class GUIHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

        if (te != null) {
            switch (ID) {
                case DataGuiIDs.SOUL_TURBINE_GENERATOR:
                    return new SoulTurbineGeneratorContainer(player.inventory, (SoulTurbineGeneratorTileEntity)te);
            }

            if (ID == 0) {
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

        if (te != null) {
            switch (ID) {
                case DataGuiIDs.SOUL_TURBINE_GENERATOR:
                    return new SoulTurbineGeneratorGUI(player.inventory, (SoulTurbineGeneratorTileEntity)te);
            }
        }

        return null;
    }

}
