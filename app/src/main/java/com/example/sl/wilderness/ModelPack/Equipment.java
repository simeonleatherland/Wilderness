package com.example.sl.wilderness.ModelPack;

public class Equipment extends Item {
    public double getMass() {
        return mass;
    }

    private double mass;

    public Equipment(){}

    public Equipment(String inDesc, int inValue, double inMass)
    {
        super(inDesc,inValue);
        mass = inMass;
    }
}
