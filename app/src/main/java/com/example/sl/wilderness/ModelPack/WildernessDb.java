package com.example.sl.wilderness.ModelPack;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.sl.wilderness.Database.DbHelper;
import com.example.sl.wilderness.Database.DbSchema.AreaTable;
import com.example.sl.wilderness.Database.DbSchema.PlayerTable;
import com.example.sl.wilderness.Database.DbSchema.ItemTable;
public class WildernessDb {
    private SQLiteDatabase db;

    public WildernessDb(Context context)
    {
        this.db = new DbHelper(
                context.getApplicationContext()
        ).getWritableDatabase();
    }

    //INSERT
    public void insertPlayer(Player p )
    {
        //create the player
        ContentValues cv = new ContentValues();
        cv.put(PlayerTable.Cols.COL, p.getColLocation());
        cv.put(PlayerTable.Cols.ROW, p.getRowLocation());
        cv.put(PlayerTable.Cols.CASH, p.getCash());
        cv.put(PlayerTable.Cols.HEALTH, p.getHealth());
        cv.put(PlayerTable.Cols.MASS, p.getEquipmentMass());

        //go through the list and update the item in the table with its unique number
        //and say that it is help by the player
        for(Equipment i : p.getEquipment())
        {
            upgradeItemHelp(i);
        }

        db.insert(PlayerTable.NAME, null, cv);

    }

    //THis method is only called when inserting the player, to which it will
    //upgrade the item in the database ASSUMING IT will be there
    public void upgradeItemHelp(Equipment i)
    {
        //NEED TO UPDATE THIS METHOD TO UPGRADE RATHER THAN INSERT
        //create the player
        ContentValues cv = new ContentValues();
        cv.put(ItemTable.Cols.ID, i.ID);
        cv.put(ItemTable.Cols.COLinMAP, -1);
        cv.put(ItemTable.Cols.ROWinMAP, -1);
        cv.put(ItemTable.Cols.HELD, false);
        cv.put(ItemTable.Cols.DESCRIPTION, i.getDescription());
        cv.put(ItemTable.Cols.VALUE, i.getValue());
        cv.put(ItemTable.Cols.TYPE, "equipment");
        cv.put(ItemTable.Cols.TYPEVALUE, i.getMass());
        db.insert(ItemTable.NAME, null, cv);

    }


    //probably not going to be used
    public void insertItem(Item i, int row, int col)
    {
        //create the player
        ContentValues cv = new ContentValues();
        cv.put(ItemTable.Cols.ID, i.ID);
        cv.put(ItemTable.Cols.COLinMAP, col);
        cv.put(ItemTable.Cols.ROWinMAP, row);
        cv.put(ItemTable.Cols.HELD, false);
        cv.put(ItemTable.Cols.DESCRIPTION, i.getDescription());
        cv.put(ItemTable.Cols.VALUE, i.getValue());
        cv.put(ItemTable.Cols.TYPE, "nothing");
        cv.put(ItemTable.Cols.TYPEVALUE, "nothing");

        db.insert(ItemTable.NAME, null, cv);

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
        cv.put(ItemTable.Cols.VALUE, i.getValue());
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
        cv.put(ItemTable.Cols.VALUE, i.getValue());
        cv.put(ItemTable.Cols.TYPE, "food");
        cv.put(ItemTable.Cols.TYPEVALUE, i.getHealth());

        db.insert(ItemTable.NAME, null, cv);

    }

    public void insertArea(Area a)
    {

    }


    //REMOVE
    public void removePlayer(Player p )
    {

    }

    public void removeItem(Item i)
    {

    }

    public void removeArea(Area a)
    {

    }

    //UPDATE
    public void updatePlayer(Player p )
    {

    }

    public void updateItem(Item i)
    {

    }

    public void updateArea(Area a)
    {

    }
}
