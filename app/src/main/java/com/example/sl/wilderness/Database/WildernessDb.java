package com.example.sl.wilderness.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sl.wilderness.Activity.Navigation;
import com.example.sl.wilderness.Database.DbHelper;
import com.example.sl.wilderness.Database.DbSchema.AreaTable;
import com.example.sl.wilderness.Database.DbSchema.PlayerTable;
import com.example.sl.wilderness.Database.DbSchema.ItemTable;
import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.ModelPack.Equipment;
import com.example.sl.wilderness.ModelPack.Food;
import com.example.sl.wilderness.ModelPack.GameData;
import com.example.sl.wilderness.ModelPack.Item;
import com.example.sl.wilderness.ModelPack.Player;

import java.util.ArrayList;
import java.util.List;

public class WildernessDb {
    private SQLiteDatabase db;

    private Player currPlayer;
    private List<Item> itemsList;
    private Area[][] grid;
    public static int PLAYERVERSION;

    public WildernessDb(Context context)
    {
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
        for(Equipment i : p.getEquipment())
        {
            insertEquipment(i,-1,-1);
        }

        db.insert(PlayerTable.NAME, null, cv);

    }

    public void load()
    {
       // grid = new Area[GameData.ROW][GameData.COL];
        currPlayer = getCurrPlayer();
    }


    public void insertEquipment(Equipment i, int row, int col)
    {
        //create the player
        ContentValues cv = new ContentValues();
        cv.put(ItemTable.Cols.ID, i.ID);
        cv.put(ItemTable.Cols.COLinMAP, col);
        cv.put(ItemTable.Cols.ROWinMAP, row);
        cv.put(ItemTable.Cols.HELD, false);
        cv.put(ItemTable.Cols.DESCRIPTION, i.getDescription());
        cv.put(ItemTable.Cols.PRICE, i.getValue());

        cv.put(ItemTable.Cols.TYPE, "equipment");
        cv.put(ItemTable.Cols.TYPEVALUE, i.getMass());

        db.insert(ItemTable.NAME, null, cv);

    }
    public void insertFood(Food i, int row, int col)
    {
        //create the player
        ContentValues cv = new ContentValues();
        cv.put(ItemTable.Cols.ID, i.ID);
        cv.put(ItemTable.Cols.COLinMAP, col);
        cv.put(ItemTable.Cols.ROWinMAP, row);
        cv.put(ItemTable.Cols.HELD, false);
        cv.put(ItemTable.Cols.DESCRIPTION, i.getDescription());
        cv.put(ItemTable.Cols.PRICE, i.getValue());
        cv.put(ItemTable.Cols.TYPE, "food");
        cv.put(ItemTable.Cols.TYPEVALUE, i.getHealth());

        db.insert(ItemTable.NAME, null, cv);

    }

    public void insertArea(Area a)
    {
        ContentValues cv = new ContentValues();
        String id = a.getRow() + "," + a.getCol();
        cv.put(AreaTable.Cols.ID, id);
        cv.put(AreaTable.Cols.ROW, a.getRow());
        cv.put(AreaTable.Cols.COL, a.getCol());
        cv.put(AreaTable.Cols.TOWN, a.isTown());
        cv.put(AreaTable.Cols.STARRED, a.isStarred());
        cv.put(AreaTable.Cols.EXPLORED, a.isExplored());
        cv.put(AreaTable.Cols.DESCRIPTION, a.getDescription());
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

    public void removeArea(Area a, int row, int col)
    {
        String[] wherevalue = {};
        String id = row + "," + col;
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
        for(Equipment i : p.getEquipment())
        {
            insertEquipment(i,-1,-1);
        }

        db.update(PlayerTable.NAME, cv, PlayerTable.Cols.ID + " = " + Navigation.getNonUpdatingVersion(), whereValues);
    }


    //THis method is only called when inserting the player, to which it will
    //upgrade the item in the database ASSUMING IT will be there
    public void updateItem(Item i, int row, int col)
    {
        //NEED TO UPDATE THIS METHOD TO UPGRADE RATHER THAN INSERT
        ContentValues cv = new ContentValues();
        cv.put(ItemTable.Cols.ID, i.ID);
        cv.put(ItemTable.Cols.COLinMAP, row);
        cv.put(ItemTable.Cols.ROWinMAP, col);
        cv.put(ItemTable.Cols.HELD, false);
        cv.put(ItemTable.Cols.DESCRIPTION, i.getDescription());
        cv.put(ItemTable.Cols.PRICE, i.getValue());
        String[] whereValue = {};
        db.update(ItemTable.NAME,cv, ItemTable.Cols.ID +" = " + i.ID, whereValue);

    }

    public void updateArea(Area a, int row, int col)
    {
        ContentValues cv = new ContentValues();
        String id = row + "," + col;
        String[] wherevalue = {};
        cv.put(AreaTable.Cols.ID, id);
        cv.put(AreaTable.Cols.ROW, row);
        cv.put(AreaTable.Cols.COL, col);
        cv.put(AreaTable.Cols.TOWN, a.isTown());
        cv.put(AreaTable.Cols.STARRED, a.isStarred());
        cv.put(AreaTable.Cols.EXPLORED, a.isExplored());
        cv.put(AreaTable.Cols.DESCRIPTION, a.getDescription());


        db.update(AreaTable.NAME, cv, AreaTable.Cols.ID + " = " + id, wherevalue);
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

    public Player getCurrPlayer()
    {
        List<Player> p = new ArrayList<>();
        Player latestPlayer;
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
        PLAYERVERSION = p.get(p.size()-1).VERSION; // this is the current player version number
        return p.get(p.size()-1);

    }
}
