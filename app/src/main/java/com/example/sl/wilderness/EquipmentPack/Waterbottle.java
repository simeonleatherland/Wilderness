package com.example.sl.wilderness.EquipmentPack;

import com.example.sl.wilderness.ModelPack.Equipment;

public class Waterbottle extends Equipment {
    public static String TYPE = "WATERBOTTLE";

    public Waterbottle(String inDesc, int inValue, double inMass, int row, int col, boolean held, int id)
    {
        super(inDesc,inValue,inMass, row, col, held,id);
    }
    public String getType()
    {
        return "WATERBOTTLE";
    }
}
