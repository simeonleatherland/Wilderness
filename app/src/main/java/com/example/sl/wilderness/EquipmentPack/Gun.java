package com.example.sl.wilderness.EquipmentPack;

import com.example.sl.wilderness.ModelPack.Equipment;

public class Gun extends Equipment {
    public Gun(String inDesc, int inValue, double inMass, int row, int col, boolean held)
    {
        super(inDesc,inValue,inMass, row, col, held);
    }
    public String getType()
    {
        return "GUN";
    }
}
