package com.example.sl.wilderness.ModelPack;

public abstract class Equipment extends Item {
    public double getMass() {
        return mass;
    }

    private double mass;

    public Equipment(){}

    public Equipment(String inDesc, int inValue, double inMass, int row, int col, boolean held)
    {
        super(inDesc,inValue, row, col, held);
        mass = inMass;
    }

    public abstract String getType();

    @Override
    public double getTypeValue() {
        return mass;
    }


}
