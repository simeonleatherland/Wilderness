package com.example.sl.wilderness.ModelPack;

import java.util.LinkedList;
import java.util.List;

public class Area {
    private boolean isTown;
    private List<Item> items;
    private String description;
    private boolean starred;
    private boolean explored;

    public Area()
    {
        items = new LinkedList<>();
        description = "";
        starred = false;
        explored = false;
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

    public void clearStarred()
    {
        starred = false;
    }

}
