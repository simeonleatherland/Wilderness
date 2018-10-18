package com.example.sl.wilderness.ModelPack;

import com.example.sl.wilderness.Activity.Wilderness;
import com.example.sl.wilderness.Database.WildernessDb;
import com.example.sl.wilderness.EquipmentPack.Axe;
import com.example.sl.wilderness.EquipmentPack.Backpack;
import com.example.sl.wilderness.EquipmentPack.BenKenobi;
import com.example.sl.wilderness.EquipmentPack.Compass;
import com.example.sl.wilderness.EquipmentPack.Gun;
import com.example.sl.wilderness.EquipmentPack.IceScraper;
import com.example.sl.wilderness.EquipmentPack.ImprobabilityDrive;
import com.example.sl.wilderness.EquipmentPack.JadeMonkey;
import com.example.sl.wilderness.EquipmentPack.Knife;
import com.example.sl.wilderness.EquipmentPack.PortaSmell;
import com.example.sl.wilderness.EquipmentPack.Roadmap;
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

    public static final int ROW = 4;
    public static final int COL = 3;

    private WildernessDb db;


    private GameData()
    {
        grid = new Area[ROW][COL];

    }

    private GameData(WildernessDb db)
    {
        this.db = db;
        grid = new Area[ROW][COL];
        unpackPlayer(db);
        unpackGrid(db);
    }

    public static GameData getInstance()
    {
        if(instance == null)
        {
            throw new IllegalArgumentException("YOU CANT DO THAT SOMETHINGS WRONG");
        }
        return instance;
    }


    //this sets the instance if the game data is
    public static GameData unpackInstanceFromDB(WildernessDb db)
    {
        if(instance == null) //essentially this method should only be called when the game is close
        //which means that instant will always be null, it doesnt make sense to restet the instance if its not so i made a check to make sure of this
        {
            instance = new GameData(db);
        }
        return instance;
    }

    private void unpackGrid(WildernessDb db) {
        Area[][] tempGrid = db.getGrid();
        if (tempGrid[0][0] == null) //if theres nothing in the grid
        {
            //if theres no grid, create one
            generateMap();
            //insert it into the database
            db.insertGameGrid(grid);
        }
        else //there is a grid in the database
        {
            grid = tempGrid;
        }
    }

    private void unpackPlayer(WildernessDb db) {
        Player mainCharacter = db.getCurrPlayer();
        if (mainCharacter == null) {
            mainCharacter = new Player(0, 0, 0, 0, 100);
            db.insertPlayer(mainCharacter);
            setPlayer(mainCharacter);
        }
        else
        {
            setPlayer(mainCharacter);
        }
    }

    public WildernessDb getDatabase(){return db;}

    public void setPlayer(Player p)
    {
        player = p;
    }

    public void setDb(WildernessDb d)
    {
        this.db = d;
    }

    public Player getPlayer() {
        return player;
    }

    public Area[][] getGrid() {
        return grid;
    }

    public GameData resetInstance(WildernessDb db)
    {
        instance = new GameData(db);
        return instance;
    }


    //this is only a temp fix
    public void generateMap()
    {
        ArrayList<Item> itemList1 = new ArrayList<>();
        itemList1.add(new PortaSmell("Apples", 3, 2,0,0,false));
        itemList1.add(new BenKenobi("Backpack", 7,4,0,0,false));
        itemList1.add(new Knife("Knife", 7,4,0,0,false));
        Area area1 = new Area(true, itemList1, "some area", false, false, 0,0);



        ArrayList<Item> itemList2 = new ArrayList<>();
        itemList2.add(new JadeMonkey("Watermelon", 2,5,0,1,false));
        itemList2.add(new Roadmap("Shovel", 6,5,0,1,false));
        itemList2.add(new IceScraper("Shovel", 6,5,0,1,false));

        Area area2 = new Area(true, itemList2, "some area", false, false, 0,1);

        ArrayList<Item> itemList3 = new ArrayList<>();
        itemList3.add(new Food("Pineapple", 5,5,0,2,false));
        itemList3.add(new ImprobabilityDrive("Compass", 3,5,0,2,false));
        Area area3 = new Area(false, itemList3, "some area", false, false, 0,2);

        ArrayList<Item> itemList4 = new ArrayList<>();
        itemList4.add(new Food("Pear", 6,2,1,0,false));
        itemList4.add(new Knife ("knife", 0,3,1,0,false));
        Area area4 = new Area(true, itemList4, "some area", false, false, 1,0);

        ArrayList<Item> itemList5 = new ArrayList<>();
        itemList5.add(new Food("Banana", 5,4,1,1,false));
        itemList5.add(new Compass ("Nothing", 0,3,1,1,false));
        Area area5 = new Area(false, itemList5, "some area", false, false, 1,1);

        ArrayList<Item> itemList6 = new ArrayList<>();
        itemList6.add(new Food("Tomato", 4,3,1,2,false));
        itemList6.add(new Gun("Gun", 4,3,1,2,false));
        Area area6 = new Area(true, itemList6, "some area", false, false, 1,2);

        ArrayList<Item> itemList7 = new ArrayList<>();
        itemList7.add(new Food("Grapes", 3,4,2,0,false));
        itemList7.add(new Torch("Torch", 3,4,2,0,false));
        Area area7 = new Area(false, itemList7, "some area", false, false, 2,0);

        ArrayList<Item> itemList8 = new ArrayList<>();
        itemList8.add(new Food("Rockmelon", 2,4,2,1,false));
        itemList8.add(new Waterbottle("Waterbottle", 2,8,2,1,false));
        Area area8 = new Area(true, itemList8, "some area", false, false, 2,1);

        ArrayList<Item> itemList9 = new ArrayList<>();
        itemList9.add(new Food("Bread", 3, 3,2,2,false));
        itemList9.add(new Axe("Axe", 9,4,2,2,false));
        Area area9 = new Area(false, itemList9, "some area", false, false, 2,2);


        ArrayList<Item> itemList10 = new ArrayList<>();
        itemList10.add(new Food("Bread", 3, 3,3,0,false));
        itemList10.add(new Axe("Axe", 9,4,3,0,false));
        Area area10 = new Area(false, itemList10, "some area", false, false, 3,0);

        ArrayList<Item> itemList11 = new ArrayList<>();
        itemList11.add(new Food("Bread", 3, 3,3,1,false));
        itemList11.add(new Axe("Axe", 9,4,3,1,false));
        Area area11 = new Area(false, itemList11, "some area", false, false, 3,1);

        ArrayList<Item> itemList12 = new ArrayList<>();
        itemList12.add(new Food("Bread", 3, 3,3,2,false));
        itemList12.add(new Axe("Axe", 9,4,3,2,false));
        Area area12 = new Area(false, itemList12, "some area", false, false, 3,2);
        grid[0][0]= area1;

        grid[0][1]= area2;
        grid[0][2]= area3;
        grid[1][0]= area4;
        grid[1][1]= area5;
        grid[1][2]= area6;
        grid[2][0]= area7;
        grid[2][1]= area8;
        grid[2][2]= area9;

        grid[3][0] = area10;
        grid[3][1] = area11;
        grid[3][2] = area12;



    }

    public void randomRegenerate()
    {
        ArrayList<Item> itemList1 = new ArrayList<>();
        itemList1.add(new PortaSmell("portasmell", 3, 2,0,0,false));
        itemList1.add(new BenKenobi("Backpack", 7,4,0,0,false));
        itemList1.add(new Knife("Knife", 7,4,0,0,false));
        Area area1 = new Area(true, itemList1, "some area", false, false, 0,0);



        ArrayList<Item> itemList2 = new ArrayList<>();
        itemList2.add(new Food("Watermelon", 2,5,0,1,false));
        itemList2.add(new Shovel("Shovel", 6,5,0,1,false));
        Area area2 = new Area(true, itemList2, "some area", false, false, 0,1);

        ArrayList<Item> itemList3 = new ArrayList<>();
        itemList3.add(new Food("Pineapple", 5,5,0,2,false));
        itemList3.add(new ImprobabilityDrive("Compass", 3,5,0,2,false));
        Area area3 = new Area(false, itemList3, "some area", false, false, 0,2);

        ArrayList<Item> itemList4 = new ArrayList<>();
        itemList4.add(new Food("Pear", 6,2,1,0,false));
        itemList4.add(new Knife ("knife", 0,3,1,0,false));
        Area area4 = new Area(true, itemList4, "some area", false, false, 1,0);

        ArrayList<Item> itemList5 = new ArrayList<>();
        itemList5.add(new Food("Banana", 5,4,1,1,false));
        itemList5.add(new Compass ("Nothing", 0,3,1,1,false));
        Area area5 = new Area(false, itemList5, "some area", false, false, 1,1);

        ArrayList<Item> itemList6 = new ArrayList<>();
        itemList6.add(new Food("Tomato", 4,3,1,2,false));
        itemList6.add(new Gun("Gun", 4,3,1,2,false));
        Area area6 = new Area(true, itemList6, "some area", false, false, 1,2);

        ArrayList<Item> itemList7 = new ArrayList<>();
        itemList7.add(new Food("Grapes", 3,4,2,0,false));
        itemList7.add(new Torch("Torch", 3,4,2,0,false));
        Area area7 = new Area(false, itemList7, "some area", false, false, 2,0);

        ArrayList<Item> itemList8 = new ArrayList<>();
        itemList8.add(new Food("Rockmelon", 2,4,2,1,false));
        itemList8.add(new Waterbottle("Waterbottle", 2,8,2,1,false));
        Area area8 = new Area(true, itemList8, "some area", false, false, 2,1);

        ArrayList<Item> itemList9 = new ArrayList<>();
        itemList9.add(new Food("Bread", 3, 3,2,2,false));
        itemList9.add(new Axe("Axe", 9,4,2,2,false));
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
        int i = row;
        int j = col;
        return grid[row][col];
    }

    public void updateMap(Area a, int row, int col)
    {
        grid[row][col] = a;
    }



}
