package com.example.sl.wilderness.ModelPack;

import com.example.sl.wilderness.Activity.Navigation;

public abstract class Item {
    private String description;
    private int value;
    private boolean held;
    //row and col values of where the item is located if it ISNT HELD
    private int row;
    private int col;
    public static int ID;

    public Item(){
        Navigation.getNewItemID();
    }


    public Item(String inDesc, int inValue, int inRow, int inCol, boolean held)
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

    public boolean getHeld() {
        return held;
    }

    public void setHeld(boolean held) {
        this.held = held;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
