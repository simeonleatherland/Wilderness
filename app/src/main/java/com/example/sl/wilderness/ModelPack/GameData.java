package com.example.sl.wilderness.ModelPack;

import com.example.sl.wilderness.EquipmentPack.Axe;
import com.example.sl.wilderness.EquipmentPack.Backpack;
import com.example.sl.wilderness.EquipmentPack.Compass;
import com.example.sl.wilderness.EquipmentPack.Gun;
import com.example.sl.wilderness.EquipmentPack.Knife;
import com.example.sl.wilderness.EquipmentPack.Shovel;
import com.example.sl.wilderness.EquipmentPack.Torch;
import com.example.sl.wilderness.EquipmentPack.Waterbottle;

import java.util.ArrayList;

public class GameData {
    //game data grid that holds all the data thats on the map
    private Area[][] grid;
    //the main current player
    private Player player;
    //static instance of the class to keep consistent
    private static GameData instance;

    private int ITEMID;
    public static final int ROW = 3;
    public static final int COL = 3;


    private GameData()
    {
        grid = new Area[ROW][COL];
        ITEMID = 0;

    }

    private GameData(Area[][] areas, Player player, int itemNum)
    {
        grid = areas;
        this.player = player;
        ITEMID = itemNum;
    }

    //static get method
    public static GameData getInstance()
    {
        if(instance == null)
        {
            instance = new GameData();
            instance.generateMap();
        }
        return instance;
    }

    //this sets the instance if the game data is
    public static GameData getInstanceFromDB(Area[][] grid, Player p, int itemNum)
    {
        if(instance == null) //essentially this method should only be called when the game is close
            //which means that instant will always be null, it doesnt make sense to restet the instance if its not so i made a check to make sure of this
        {
            instance = new GameData(grid, p, itemNum);
        }
        return instance;
    }

    public void setPlayer(Player p)
    {
        player = p;
    }

    public Player getPlayer() {
        return player;
    }

    public Area[][] getGrid() {
        return grid;
    }

    public void resetInstance()
    {
        instance = new GameData();
        player = new Player(100, 0, 0,0, 100);
        instance.generateMap();
        ITEMID = 0;
    }


    //this is only a temp fix
    public void generateMap()
    {
        ArrayList<Item> itemList1 = new ArrayList<>();
        itemList1.add(new Food("Apples", 3, 2,0,0,false,incrementID()));
        itemList1.add(new Backpack("Backpack", 7,4,0,0,false,incrementID()));
        itemList1.add(new Knife("Knife", 7,4,0,0,false,incrementID()));
        Area area1 = new Area(true, itemList1, "some area", false, false, 0,0);



        ArrayList<Item> itemList2 = new ArrayList<>();
        itemList2.add(new Food("Watermelon", 2,5,0,1,false,incrementID()));
        itemList2.add(new Shovel("Shovel", 6,5,0,1,false,incrementID()));
        Area area2 = new Area(true, itemList2, "some area", false, false, 0,1);

        ArrayList<Item> itemList3 = new ArrayList<>();
        itemList3.add(new Food("Pineapple", 5,5,0,2,false,incrementID()));
        itemList3.add(new Compass("Compass", 3,5,0,2,false,incrementID()));
        Area area3 = new Area(false, itemList3, "some area", false, false, 0,2);

        ArrayList<Item> itemList4 = new ArrayList<>();
        itemList4.add(new Food("Pear", 6,2,1,0,false,incrementID()));
        itemList4.add(new Knife ("knife", 0,3,1,0,false,incrementID()));
        Area area4 = new Area(true, itemList4, "some area", false, false, 1,0);

        ArrayList<Item> itemList5 = new ArrayList<>();
        itemList5.add(new Food("Banana", 5,4,1,1,false,incrementID()));
        itemList5.add(new Compass ("Nothing", 0,3,1,1,false,incrementID()));
        Area area5 = new Area(false, itemList5, "some area", false, false, 1,1);

        ArrayList<Item> itemList6 = new ArrayList<>();
        itemList6.add(new Food("Tomato", 4,3,1,2,false,incrementID()));
        itemList6.add(new Gun("Gun", 4,3,1,2,false,incrementID()));
        Area area6 = new Area(true, itemList6, "some area", false, false, 1,2);

        ArrayList<Item> itemList7 = new ArrayList<>();
        itemList7.add(new Food("Grapes", 3,4,2,0,false,incrementID()));
        itemList7.add(new Torch("Torch", 3,4,2,0,false,incrementID()));
        Area area7 = new Area(false, itemList7, "some area", false, false, 2,0);

        ArrayList<Item> itemList8 = new ArrayList<>();
        itemList8.add(new Food("Rockmelon", 2,4,2,1,false,incrementID()));
        itemList8.add(new Waterbottle("Waterbottle", 2,8,2,1,false,incrementID()));
        Area area8 = new Area(true, itemList8, "some area", false, false, 2,1);

        ArrayList<Item> itemList9 = new ArrayList<>();
        itemList9.add(new Food("Bread", 3, 3,2,2,false,incrementID()));
        itemList9.add(new Axe("Axe", 9,4,2,2,false, incrementID()));
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
    private int incrementID()
    {
        return ITEMID++;
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
