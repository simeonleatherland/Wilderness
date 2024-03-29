package com.example.sl.wilderness.ModelPack;

public class Food extends Item {
    private double health;
    public static final String TYPE = "FOOD";

    //this is used to rebuild
    public Food(String inDesc, int inValue, double inHealth, int row, int col, boolean held, long id)
    {
        super(inDesc,inValue, row, col, held, id);
        //i dont validate this as i assume that the health can be poisonous and reduce health for
        //some possible reason
        health = inHealth;
    }
    public Food(String inDesc, int inValue, double inHealth, int row, int col, boolean held)
    {
        super(inDesc,inValue, row, col, held);
        //i dont validate this as i assume that the health can be poisonous and reduce health for
        //some possible reason
        health = inHealth;
    }



    @Override
    public String getType()
    {
        return "FOOD";
    }

    @Override
    public double getTypeValue() {
        return health;
    }
}
