package ohirakyou.turtletech.common.material;

import cyano.basemetals.material.MetalMaterial;

public abstract class Materials extends cyano.basemetals.init.Materials {

    public static MetalMaterialComplex cast_iron;

    private static boolean initDone = false;
    public static void init(){
        if(initDone)return;

        // mod metals
        cast_iron = addComplexMaterial("cast_iron", 8, 5.33, 4); // properly formats multi-word name

        initDone = true;
    }

    private static MetalMaterial addMaterial(String name, double hardness, double strength, double magic){
        MetalMaterial m = new MetalMaterial(name,(float)hardness,(float)strength,(float)magic);
        registerMaterial(name, m);
        return m;
    }

    private static MetalMaterialComplex addComplexMaterial(String name, double hardness, double strength, double magic){
        MetalMaterialComplex m = new MetalMaterialComplex(name,(float)hardness,(float)strength,(float)magic);
        registerMaterial(name, m);
        return m;
    }

}