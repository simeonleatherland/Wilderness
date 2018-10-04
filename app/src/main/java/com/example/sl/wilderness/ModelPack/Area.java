package com.example.sl.wilderness.ModelPack;

import java.util.LinkedList;
import java.util.List;

public class Area {


    private boolean isTown;
    private List<Item> items;
    private String description;
    private boolean starred;
    private boolean explored;
    private int row;
    private int col;

    public Area(boolean isTown,List<Item> list,  String description, boolean starred, boolean explored, int row, int col) {
        this.isTown = isTown;
        this.description = description;
        this.starred = starred;
        this.explored = explored;
        this.row = row;
        this.col = col;
        if(list != null)
        {
            items = list;

        }
    }

    public Area(boolean isTown, String description, boolean starred, boolean explored, int row, int col) {
        this.isTown = isTown;
        this.description = description;
        this.starred = starred;
        this.explored = explored;
        this.row = row;
        this.col = col;
        items = new LinkedList<>();
    }




    public Area()
    {
        items = new LinkedList<>();
        description = "";
        starred = false;
        explored = false;
    }

    public boolean isStarred() {
        return starred;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored()
    {
        explored = true;
    }
    //not sure if this is needed or appropriate, however itll probably be used to clear all areas
    //if the game restarts
    public void clearExplored()
    {
        explored = false;
    }

    public void setStarred()
    {
        starred = true;
    }


    public boolean isTown() {
        return isTown;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getDescription() {
        return description;
    }

    public void insertItem(Item items) {
        this.items.add(items);
    }
}
