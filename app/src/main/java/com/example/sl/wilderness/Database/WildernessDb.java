package com.example.sl.wilderness.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sl.wilderness.Activity.Navigation;
import com.example.sl.wilderness.Database.DbSchema.AreaTable;
import com.example.sl.wilderness.Database.DbSchema.PlayerTable;
import com.example.sl.wilderness.Database.DbSchema.ItemTable;
import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.ModelPack.Equipment;
import com.example.sl.wilderness.ModelPack.GameData;
import com.example.sl.wilderness.ModelPack.Item;
import com.example.sl.wilderness.ModelPack.Player;

import java.util.ArrayList;
import java.util.List;

public class WildernessDb {
    private SQLiteDatabase db;

    private Player currPlayer;
    private Area[][] grid;
    public static int PLAYERVERSION;
    public static int NUMITEMSID;

    Context c;

    private List<Item> itemsList;
    private List<Equipment> heldList;

    public WildernessDb(Context context)
    {
        c = context;
        this.db = new DbHelper(
                context.getApplicationContext()).getWritableDatabase();
    }




    //INSERT
    public void insertPlayer(Player p)
    {
        //create the player
        ContentValues cv = new ContentValues();
        //gets and updagtes the player version so when upgrading you can update the correct one
        cv.put(PlayerTable.Cols.ID, p.VERSION);
        cv.put(PlayerTable.Cols.COL, p.getColLocation());
        cv.put(PlayerTable.Cols.ROW, p.getRowLocation());
        cv.put(PlayerTable.Cols.CASH, p.getCash());
        cv.put(PlayerTable.Cols.HEALTH, p.getHealth());
        cv.put(PlayerTable.Cols.MASS, p.getEquipmentMass());

        //go through the list and update the item in the table with its unique number
        //and say that it is held by the player
        for(Item i : p.getEquipment())
        {
            //set row and col to -1 to show it as being held, not at a position
            i.setRow(-1);
            i.setCol(-1);
            //set it held as well
            i.setHeld(true);
            insertItem(i);
        }
        db.insert(PlayerTable.NAME, null, cv);
    }

    public void load()
    {
        grid = new Area[GameData.ROW][GameData.COL];
        currPlayer = retrievePlayer();
        //calculate the highest ID number thats been created so that you dont override items
        //also allows you to update the items specifically
        NUMITEMSID = retrieveItems();
        if(heldList == null)
        {
            currPlayer.setEquipment(heldList);
        }


        grid = getMapGrid();
        if(grid[0][0] == null)
        {
            sortItemsToAreas(grid, itemsList);
        }
    }

    private void sortItemsToAreas(Area[][] grid, List<Item> itemsList) {
        for(Item i : itemsList)
        {
            if(i.getRow() != -1 && i.getCol() !=-1) //if theyre both not held, somehow made it through
            {
                grid[i.getRow()][i.getCol()].insertItem(i); //insert into the grids area list
            }
        }
    }


    public void insertItem(Item i)
    {
        //create the player
        ContentValues cv = new ContentValues();
        cv.put(ItemTable.Cols.ID,i.ID);
        cv.put(ItemTable.Cols.COLinMAP, i.getCol());
        cv.put(ItemTable.Cols.ROWinMAP, i.getRow());
        cv.put(ItemTable.Cols.HELD, false);
        cv.put(ItemTable.Cols.DESCRIPTION, i.getDescription());
        cv.put(ItemTable.Cols.PRICE, i.getValue());
        //type is the type of object it is so that i can recreate
        cv.put(ItemTable.Cols.TYPE, i.getType());
        cv.put(ItemTable.Cols.TYPEVALUE, i.getTypeValue());

        db.insert(ItemTable.NAME, null, cv);

    }


    public void insertArea(Area a)
    {
        ContentValues cv = new ContentValues();
        String id = ((a.getRow()* 100) + a.getCol())+ "";
        cv.put(AreaTable.Cols.ID, id);
        cv.put(AreaTable.Cols.ROW, a.getRow());
        cv.put(AreaTable.Cols.COL, a.getCol());
        cv.put(AreaTable.Cols.TOWN, a.isTown());
        cv.put(AreaTable.Cols.STARRED, a.isStarred());
        cv.put(AreaTable.Cols.EXPLORED, a.isExplored());
        cv.put(AreaTable.Cols.DESCRIPTION, a.getDescription());
        for(Item i : a.getItems())
        {
            i.setRow(a.getRow());
            i.setCol(a.getCol());
            i.setHeld(false);
            insertItem(i);
        }
        db.insert(AreaTable.NAME, null, cv);
    }


    //REMOVE
    public void removePlayer(Player p )
    {
        String[] wherevalue = {};
        db.delete(PlayerTable.NAME, PlayerTable.Cols.ID +" = " + Navigation.getNonUpdatingVersion(), wherevalue);

    }

    public void removeItem(Item i)
    {
        String[] wherevalue = {};
        db.delete(ItemTable.NAME, ItemTable.Cols.ID +" = " + i.ID, wherevalue);

    }

    public void removeArea(Area a)
    {
        String[] wherevalue = {};
        String id = a.getRow() + "," + a.getCol();
        db.delete(AreaTable.NAME, AreaTable.Cols.ID +" = " + id, wherevalue);

    }

    //UPDATE
    /* update the player , however also update the version of the player just incase you want to revert
    your decision to update the player later
     */
    public void updatePlayer(Player p)
    {
        String[] whereValues = {};
        ContentValues cv = new ContentValues();
        cv.put(PlayerTable.Cols.ID, Navigation.getNonUpdatingVersion());
        cv.put(PlayerTable.Cols.COL, p.getColLocation());
        cv.put(PlayerTable.Cols.ROW, p.getRowLocation());
        cv.put(PlayerTable.Cols.CASH, p.getCash());
        cv.put(PlayerTable.Cols.HEALTH, p.getHealth());
        cv.put(PlayerTable.Cols.MASS, p.getEquipmentMass());

        //go through the list and update the item in the table with its unique number
        //and say that it is held by the player
        for(Item i : p.getEquipment())
        {
            //set row and col to -1 to show it as being held, not at a position
            i.setRow(-1);
            i.setCol(-1);
            //set it held as well
            i.setHeld(true);
            updateItem(i);
        }
        db.update(PlayerTable.NAME, cv, PlayerTable.Cols.ID + " = " + Navigation.getNonUpdatingVersion(), whereValues);
    }


    //THis method is only called when inserting the player, to which it will
    //upgrade the item in the database ASSUMING IT will be there
    public void updateItem(Item i)
    {
        //NEED TO UPDATE THIS METHOD TO UPGRADE RATHER THAN INSERT
        ContentValues cv = new ContentValues();
        cv.put(ItemTable.Cols.ID, i.ID);
        cv.put(ItemTable.Cols.COLinMAP, i.getCol());
        cv.put(ItemTable.Cols.ROWinMAP, i.getRow());
        cv.put(ItemTable.Cols.HELD, false);
        cv.put(ItemTable.Cols.DESCRIPTION, i.getDescription());
        cv.put(ItemTable.Cols.PRICE, i.getValue());
        String[] whereValue = {i.ID + ""};
        db.update(ItemTable.NAME,cv, ItemTable.Cols.ID +" = ?", whereValue);
    }

    public void updateArea(Area a)
    {
        ContentValues cv = new ContentValues();
        String id = ((a.getRow()* 100) + a.getCol())+ "";
        String[] wherevalue = {id};
        cv.put(AreaTable.Cols.ID, id);
        cv.put(AreaTable.Cols.ROW, a.getRow());
        cv.put(AreaTable.Cols.COL, a.getCol());
        cv.put(AreaTable.Cols.TOWN, a.isTown());
        cv.put(AreaTable.Cols.STARRED, a.isStarred());
        cv.put(AreaTable.Cols.EXPLORED, a.isExplored());
        cv.put(AreaTable.Cols.DESCRIPTION, a.getDescription());
        for(Item i : a.getItems())
        {
            i.setRow(a.getRow());
            i.setCol(a.getCol());
            i.setHeld(false);
            updateItem(i);
        }

        db.update(AreaTable.NAME, cv, AreaTable.Cols.ID + " = ?" , wherevalue);
    }

    private DatabaseCursor queryPlayerTable(String where, String[] whereArgs)
    {
        Cursor cursor = db.query(
                PlayerTable.NAME,
                null, //columns, null selects all columsn
                where,
                whereArgs,
                null, //group by
                null, //having
                null
        );
        return new DatabaseCursor(cursor);
    }
    private DatabaseCursor queryItemTable(String where, String[] whereArgs)
    {
        Cursor cursor = db.query(
                ItemTable.NAME,
                null, //columns, null selects all columsn
                where,
                whereArgs,
                null, //group by
                null, //having
                null
        );
        return new DatabaseCursor(cursor);
    }

    public void updateGameGrid(Area[][] area)
    {
        for(int ii = 0; ii < GameData.ROW; ii++)
        {
            for(int jj = 0; jj < GameData.COL; jj++)
            {
                //incase the areas are swapped im going to double check and override the updated grid
                area[ii][jj].setRow(ii);
                area[ii][jj].setCol(jj);
                //insert the area into the database
                updateArea(area[ii][jj]);
                for(Item i : area[ii][jj].getItems())
                {
                    i.setRow(ii);
                    i.setCol(jj);
                    i.setHeld(false);
                    updateItem(i);
                }
            }
        }
    }

    /* initially inserting the game grid
        the main difference between this and update is that update will UPDATE the areas and items
     */

    public void insertGameGrid(Area[][] area)
    {
        for(int ii = 0; ii < GameData.ROW; ii++)
        {
            for(int jj = 0; jj < GameData.COL; jj++)
            {
                //incase the areas are swapped im going to double check and override the updated grid
                area[ii][jj].setRow(ii);
                area[ii][jj].setCol(jj);
                //insert the area into the database
                insertArea(area[ii][jj]);
                for(Item i : area[ii][jj].getItems())
                {
                    i.setRow(ii);
                    i.setCol(jj);
                    i.setHeld(false);
                    insertItem(i);
                }
            }
        }
    }

    public Player retrievePlayer()
    {
        List<Player> p = new ArrayList<>();
        DatabaseCursor cursor = queryPlayerTable(null,null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                p.add(cursor.getPlayer());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        if(p.size() == 0)
        {
            return null;
        }
        else
        {
            PLAYERVERSION = p.get(p.size()-1).VERSION; // this is the current player version number
            return p.get(p.size()-1);
        }

    }

    public int retrieveItems()
    {
        List<Equipment> held = new ArrayList<>();
        List<Item> inArea = new ArrayList<>();
        DatabaseCursor cursor = queryItemTable(null,null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Item i = cursor.getEquipment();
                if(i != null)
                {
                    if(i.getHeld())
                    {
                        held.add((Equipment)i);
                    }
                    else
                    {
                        inArea.add(i);
                    }
                }
                cursor.moveToNext();

            }
        } finally {
            cursor.close();
        }
        itemsList = inArea;
        heldList = held;
        int maxNum = 0;
        for(Item i : held)
        {
            if(i.ID > maxNum)
            {
                maxNum = i.ID;
            }
        }
        for(Item i : inArea)
        {
            if(i.ID > maxNum)
            {
                maxNum = i.ID;
            }
        }
        return maxNum;
    }


    public Area[][] getMapGrid() {
        Area[][] grid = new Area[GameData.ROW][GameData.COL];
        DatabaseCursor cursor = queryAreaTable(null,null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Area temp = cursor.getArea();
                grid[temp.getRow()][temp.getCol()] = temp;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return grid;
    }

    private DatabaseCursor queryAreaTable(String where, String[] whereArgs)
    {
        Cursor cursor = db.query(
                AreaTable.NAME,
                null, //columns, null selects all columsn
                where,
                whereArgs,
                null, //group by
                null, //having
                null
        );
        return new DatabaseCursor(cursor);
    }

    public boolean clearDatabase()
    {
        return DbHelper.onDelete(c);
    }


    public Player getCurrPlayer()
    {
        return currPlayer;
    }

    public Area[][] getGrid()
    {
        return grid;
    }
}
