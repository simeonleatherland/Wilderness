package com.example.sl.wilderness.ModelPack;

public abstract class Item {
    private String description;
    private int value;

    public Item(){

    }

    public Item(String inDesc, int inValue)
    {
        if(inDesc == null)
        {
            description = "";
        }
        else
        {
            description = inDesc;
        }
        if(inValue < 0)
        {
            value = 0;
        }
        else
        {
            value = inValue;
        }
    }

    public int getValue(){return value;}
    public String getDescription()
    {
        return  description;
    }
}
