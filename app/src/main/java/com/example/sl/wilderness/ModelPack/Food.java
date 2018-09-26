package com.example.sl.wilderness.ModelPack;

public class Food extends Item {
    private double health;

    public Food(String inDesc, int inValue, double inHealth)
    {
        super(inDesc,inValue);
        //i dont validate this as i assume that the health can be poisonous and reduce health for
        //some possible reason
        health = inHealth;
    }

    public double getHealth()
    {
        return health;
    }
}
