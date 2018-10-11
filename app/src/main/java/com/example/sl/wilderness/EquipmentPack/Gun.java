package com.example.sl.wilderness.EquipmentPack;

import com.example.sl.wilderness.ModelPack.Equipment;

public class Gun extends Equipment {
    public static String TYPE = "GUN";

    public Gun(String inDesc, int inValue, double inMass, int row, int col, boolean held, int id)
    {
        super(inDesc,inValue,inMass, row, col, held,id);
    }
    public String getType()
    {
        return "GUN";
    }
}
