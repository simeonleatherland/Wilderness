package com.example.sl.wilderness.EquipmentPack;

import com.example.sl.wilderness.ModelPack.Equipment;

public class Shovel extends Equipment {
    public static String TYPE = "SHOVEL";

    public Shovel(String inDesc, int inValue, double inMass, int row, int col, boolean held, long id)
    {
        super(inDesc,inValue,inMass, row, col, held,id);
    }
    public Shovel(String inDesc, int inValue, double inMass, int row, int col, boolean held)
    {
        super(inDesc,inValue,inMass, row, col, held);
    }
    public String getType()
    {
        return "SHOVEL";
    }
}
