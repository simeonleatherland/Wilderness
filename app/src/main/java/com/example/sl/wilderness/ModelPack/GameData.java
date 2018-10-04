package com.example.sl.wilderness.ModelPack;

import java.util.ArrayList;

public class GameData {
    //game data grid that holds all the data thats on the map
    private Area[][] grid;
    //the main current player
    private Player player;
    //static instance of the class to keep consistent
    private static GameData instance;

    public static final int ROW = 3;
    public static final int COL = 3;

    //static get method
    public static GameData getInstance()
    {
        if(instance == null)
        {
            //havent decided what im going to do in here if there isnt an instance
            //probs need to create the grid, get the player somehow if it has or hasnt been created
            instance = new GameData();
            instance.generateMap();
        }
        return instance;
    }


    public void generateMap()
    {
        ArrayList<Item> itemList1 = new ArrayList<>();
        itemList1.add(new Food("Apples", 3, 2,0,0,false));
        itemList1.add(new Equipment ("Backpack", 7,4,0,0,false));
        itemList1.add(new Equipment ("Knife", 7,4,0,0,false));
        Area area1 = new Area(true, itemList1, "some area", false, false, 0,0);

        ArrayList<Item> itemList2 = new ArrayList<>();
        itemList2.add(new Food("Watermelon", 2,5,0,1,false));
        itemList2.add(new Equipment ("Shovel", 6,5,0,1,false));
        Area area2 = new Area(true, itemList2, "some area", false, false, 0,1);

        ArrayList<Item> itemList3 = new ArrayList<>();
        itemList3.add(new Food("Pineapple", 5,5,0,2,false));
        itemList3.add(new Equipment("Compass", 3,5,0,2,false));
        Area area3 = new Area(false, itemList3, "some area", false, false, 0,2);

        ArrayList<Item> itemList4 = new ArrayList<>();
        itemList4.add(new Food("Pear", 6,2,1,0,false));
        itemList4.add(new Equipment ("Nothing", 0,3,1,0,false));
        Area area4 = new Area(true, itemList4, "some area", false, false, 1,0);

        ArrayList<Item> itemList5 = new ArrayList<>();
        itemList5.add(new Food("Banana", 5,4,1,1,false));
        itemList5.add(new Equipment ("Nothing", 0,3,1,1,false));
        Area area5 = new Area(false, itemList5, "some area", false, false, 1,1);

        ArrayList<Item> itemList6 = new ArrayList<>();
        itemList6.add(new Food("Tomato", 4,3,1,2,false));
        itemList6.add(new Equipment ("Chair", 4,3,1,2,false));
        Area area6 = new Area(true, itemList6, "some area", false, false, 1,2);

        ArrayList<Item> itemList7 = new ArrayList<>();
        itemList7.add(new Food("Grapes", 3,4,2,0,false));
        itemList7.add(new Equipment ("Torch", 3,4,2,0,false));
        Area area7 = new Area(false, itemList7, "some area", false, false, 2,0);

        ArrayList<Item> itemList8 = new ArrayList<>();
        itemList8.add(new Food("Rockmelon", 2,4,2,1,false));
        itemList8.add(new Equipment ("Waterbottle", 2,8,2,1,false));
        Area area8 = new Area(true, itemList8, "some area", false, false, 2,1);

        ArrayList<Item> itemList9 = new ArrayList<>();
        itemList9.add(new Food("Bread", 3, 3,2,2,false));
        itemList9.add(new Equipment ("Nothing", 9,4,2,2,false));
        Area area9 = new Area(false, itemList9, "some area", false, false, 2,2);

        grid[0][0]= area1;

        grid[0][1]= area2;
        grid[0][2]= area3;
        grid[1][0]= area4;
        grid[1][1]= area5;
        grid[1][2]= area6;
        grid[2][0]= area7;
        grid[2][1]= area8;
        grid[2][2]= area9;
    }

    public Area getArea(int row, int col)
    {
        return grid[row][col];
    }

    public void updateMap(Area a, int row, int col)
    {
        grid[row][col] = a;
    }



}
