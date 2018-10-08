package com.example.sl.wilderness.ModelPack;

public class Food extends Item {
    private double health;

    //this is used to rebuild
    public Food(String inDesc, int inValue, double inHealth, int row, int col, boolean held)
    {
        super(inDesc,inValue, row, col, held);
        //i dont validate this as i assume that the health can be poisonous and reduce health for
        //some possible reason
        health = inHealth;
    }


    public double getHealth()
    {
        return health;
    }

    @Override
    public String getType()
    {
        return "FOOD";
    }
}
