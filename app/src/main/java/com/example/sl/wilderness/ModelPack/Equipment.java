package com.example.sl.wilderness.ModelPack;

public class Equipment extends Item {
    private double mass;

    public Equipment(){}

    public Equipment(String inDesc, int inValue, double inMass)
    {
        super(inDesc,inValue);
        mass = inMass;
    }
}
