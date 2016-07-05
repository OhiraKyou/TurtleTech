package ohirakyou.turtletech.common.material;

import cyano.basemetals.material.MetalMaterial;
import ohirakyou.turtletech.TurtleTech;

import java.util.Locale;
import java.util.regex.*;

public class MetalMaterialComplex extends MetalMaterial {
    public MetalMaterialComplex(MetalMaterial material) {
        super(material.getName(), material.hardness, material.strength, material.magicAffinity);
    }

    public MetalMaterialComplex(String name, float hardness, float strength, float magic) {
        super(name, hardness, strength, magic);
    }

    @Override
    public String getCapitalizedName() {
        // Make two-word names like "cast_iron" go from "Cast_iron" to "CastIron"
        Matcher m = Pattern.compile("_(\\w)").matcher(super.getCapitalizedName());

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group().toUpperCase(Locale.ENGLISH));
        }
        m.appendTail(sb);

        return sb.toString().replace("_", "");
    }
}
