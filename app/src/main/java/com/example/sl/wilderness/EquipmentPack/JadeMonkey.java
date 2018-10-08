package com.example.sl.wilderness.EquipmentPack;

import com.example.sl.wilderness.ModelPack.Equipment;

public class JadeMonkey extends Equipment {
    public JadeMonkey(String inDesc, int inValue, double inMass, int row, int col, boolean held)
    {
        super(inDesc,inValue,inMass, row, col, held);
    }
    public String getType()
    {
        return "JADEMOKEY";
    }
}
