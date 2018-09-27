package com.example.sl.wilderness.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sl.wilderness.Database.DbSchema.AreaTable;
import com.example.sl.wilderness.Database.DbSchema.PlayerTable;
import com.example.sl.wilderness.Database.DbSchema.ItemTable;
import com.example.sl.wilderness.ModelPack.Item;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "wilderness.db";

    public DbHelper(Context c)
    {
        super(c, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Creation of the table for area, player and item
        db.execSQL("create table " +  AreaTable.NAME + "(" +
                    "_id integer primary key autoincrement, " +
                        AreaTable.Cols.ID + ", " +
                        AreaTable.Cols.ROW + ", " +
                        AreaTable.Cols.COL + ", " +
                        AreaTable.Cols.TOWN + ", " +
                        AreaTable.Cols.STARRED + ", " +
                        AreaTable.Cols.EXPLORED + ") " );


        db.execSQL("create table " +  PlayerTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                PlayerTable.Cols.ROW + ", " +
                PlayerTable.Cols.COL + ", " +
                PlayerTable.Cols.CASH + ", " +
                PlayerTable.Cols.HEALTH + ", " +
                PlayerTable.Cols.MASS + ") " );

        db.execSQL("create table " +  ItemTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ItemTable.Cols.ID + ", " +
                ItemTable.Cols.ROWinMAP + ", " +
                ItemTable.Cols.COLinMAP + ", " +
                ItemTable.Cols.HELD + ", " +
                ItemTable.Cols.TYPE + ", " +
                ItemTable.Cols.DESCRIPTION + ", " +
                ItemTable.Cols.VALUE + ", " +
                ItemTable.Cols.TYPEVALUE + ") " );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2)
    {
        if(v2 > v1)
        {
            db.needUpgrade(v2);
        }
    }
}
