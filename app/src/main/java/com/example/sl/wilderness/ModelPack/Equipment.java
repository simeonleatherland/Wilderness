package com.example.sl.wilderness.ModelPack;

public abstract class Equipment extends Item {
    public double getMass() {
        return mass;
    }

    private double mass;


    public Equipment(String inDesc, int inValue, double inMass, int row, int col, boolean held, int id)
    {
        super(inDesc,inValue, row, col, held,id);
        mass = inMass;
    }

    public abstract String getType();

    @Override
    public double getTypeValue() {
        return mass;
    }


}
