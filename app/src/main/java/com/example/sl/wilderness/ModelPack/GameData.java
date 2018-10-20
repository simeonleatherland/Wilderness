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
import java.util.Random;

public class GameData {
    //game data grid that holds all the data thats on the map
    private Area[][] grid;
    //the main current player
    private Player player;
    //static instance of the class to keep consistent
    private static GameData instance;

    public static final int ROW = 5;
    public static final int COL = 20;

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
            player = new Player(100, 0, 0, 0, 100);
            db.insertPlayer(player);
            setPlayer(player);
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


    public void generateMap() {
        Area area1;
        //boolean to only drop one of each of the winning items
        int num = getRandomNumberInRange(0, 150);
        for (int ii = 0; ii < ROW; ii++) {
            for (int jj = 0; jj < COL; jj++) {
                ArrayList<Item> itemList1 = new ArrayList<>();
                //generate how many items in an area
                int numItems = getRandomNumberInRange(1, 5);
                for (int kk = 0; kk < numItems; kk++) {
                    //generate a number to generate
                    num = getRandomNumberInRange(0, 110);


                    //drop only 9 % of items being a benkenobi
                    if (num >= 0 && num < 10) {
                        itemList1.add(new BenKenobi("ben kenobi", 7, 0, ii, jj, false));
                    }
                    if (num >= 10 && num < 30) {
                        itemList1.add(new Food("Food", 7, 4, ii, jj, false));
                    }
                    if (num >= 20 && num < 50) {
                        itemList1.add(new Axe("axe", 7, 4, ii, jj, false));
                    }
                    if (num >= 50 && num < 70) {
                        itemList1.add(new Backpack("Backpack", 7, 4, ii, jj, false));
                    }
                    if (num >= 70 && num < 90) {
                        itemList1.add(new Shovel("shovel", 7, 4, ii, jj, false));
                    }
                    if (num >= 90 && num <= 110) {
                        itemList1.add(new Knife("Knife", 7, 4, ii, jj, false));
                    }

                }
                
                if (num < 55)//75% chance of a town
                {
                    area1 = new Area(true, itemList1, "some area", false, false, ii, jj);
                } else {
                    area1 = new Area(false, itemList1, "some area", false, false, ii, jj);
                }

                grid[ii][jj] = area1;
            }

        }

        int row, col;
        row = getRandomNumberInRange(0, ROW-1);
        col = getRandomNumberInRange(0, COL-1);
        grid[row][col].getItems().add(new IceScraper("ice scrapper drive", 7, 4, row, col, false));


        row = getRandomNumberInRange(0, ROW-1);
        col = getRandomNumberInRange(0, COL-1);

        grid[row][col].getItems().add(new ImprobabilityDrive("improbability drive", 7, 4, row, col, false));


        row = getRandomNumberInRange(0, ROW-1);
        col = getRandomNumberInRange(0, COL-1);

        grid[row][col].getItems().add(new JadeMonkey("jade monkey drive", 7, 4, row, col, false));


        row = getRandomNumberInRange(0, ROW-1);
        col = getRandomNumberInRange(0, COL-1);

        grid[row][col].getItems().add(new PortaSmell("porta smell drive", 7, 5, row, col, false));

        row = getRandomNumberInRange(0, ROW-1);
        col = getRandomNumberInRange(0, COL-1);

        grid[row][col].getItems().add(new Roadmap("improbability drive", 7, 4, row, col, false));


    }





    public Area getArea(int row, int col)
    {
        int i = row;
        int j = col;
        return grid[i][j];
    }

    public void updateMap(Area a, int row, int col)
    {
        grid[row][col] = a;
    }

    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
