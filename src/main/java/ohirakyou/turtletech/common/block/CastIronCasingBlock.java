package ohirakyou.turtletech.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import ohirakyou.turtletech.common.material.Materials;

public class CastIronCasingBlock extends BlockBase {
    public CastIronCasingBlock() {
        super(Material.IRON);

        setHardness(Materials.cast_iron.hardness);
        setResistance(Materials.cast_iron.getBlastResistance());
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
    }
}
