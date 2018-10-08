package com.example.sl.wilderness.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.ModelPack.Equipment;
import com.example.sl.wilderness.ModelPack.Food;
import com.example.sl.wilderness.ModelPack.Item;
import com.example.sl.wilderness.ModelPack.Player;
import com.example.sl.wilderness.Database.DbSchema.AreaTable;
import com.example.sl.wilderness.Database.DbSchema.PlayerTable;
import com.example.sl.wilderness.Database.DbSchema.ItemTable;
public class DatabaseCursor extends CursorWrapper {
    public DatabaseCursor(Cursor c)
    {
        super(c);
    }


    public Player getPlayer()
    {
        int cash = getInt(getColumnIndex(PlayerTable.Cols.CASH));
        int row = getInt(getColumnIndex(PlayerTable.Cols.ROW));
        int col = getInt(getColumnIndex(PlayerTable.Cols.COL));
        double mass = getDouble(getColumnIndex(PlayerTable.Cols.MASS));
        int health = getInt(getColumnIndex(PlayerTable.Cols.HEALTH));
        int version = getInt(getColumnIndex(PlayerTable.Cols.ID));

        return new Player(cash, mass, row, col, health,version);
    }

    public Area getArea()
    {
        int row = getInt(getColumnIndex(AreaTable.Cols.ROW));
        int col = getInt(getColumnIndex(AreaTable.Cols.COL));
        boolean town = getInt(getColumnIndex(AreaTable.Cols.TOWN)) > 0;
        boolean starred = getInt(getColumnIndex(AreaTable.Cols.STARRED)) > 0;
        boolean explored = getInt(getColumnIndex(AreaTable.Cols.EXPLORED)) > 0;
        String desc = getString(getColumnIndex(AreaTable.Cols.DESCRIPTION));
        return new Area(town,desc,starred,explored,row,col);
    }

    /*
    public Item getItem()
    {
        Item i = null;
        int value = getInt(getColumnIndex(ItemTable.Cols.PRICE));

        String desc = getString(getColumnIndex(ItemTable.Cols.DESCRIPTION));
        String type = getString(getColumnIndex(ItemTable.Cols.TYPE));
        int typevalue = getInt(getColumnIndex(ItemTable.Cols.TYPEVALUE));
        boolean held = getInt(getColumnIndex(ItemTable.Cols.HELD)) > 0;

        int row = getInt(getColumnIndex(ItemTable.Cols.ROWinMAP));
        int col = getInt(getColumnIndex(ItemTable.Cols.COLinMAP));

        if(type.equals("equipment"))
        {
            i = new Equipment(desc, value, typevalue, row, col, held);
        }
        else if(type.equals("food"))
        {
            i = new Food(desc, value, typevalue, row, col, held);
        }
        return i;
    }

    */

}
