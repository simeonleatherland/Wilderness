package com.example.sl.wilderness.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

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


    public Item getEquipment()
    {
        Equipment i = null;
        int value = getInt(getColumnIndex(ItemTable.Cols.PRICE));
        long id = getInt(getColumnIndex("_id"));
        String desc = getString(getColumnIndex(ItemTable.Cols.DESCRIPTION));
        String type = getString(getColumnIndex(ItemTable.Cols.TYPE));
        int typevalue = getInt(getColumnIndex(ItemTable.Cols.TYPEVALUE));
        boolean held = getInt(getColumnIndex(ItemTable.Cols.HELD)) > 0;

        int row = getInt(getColumnIndex(ItemTable.Cols.ROWinMAP));
        int col = getInt(getColumnIndex(ItemTable.Cols.COLinMAP));

        if(type.equals(Axe.TYPE))
        {
            i = new Axe(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(Backpack.TYPE))
        {
            i = new Backpack(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(BenKenobi.TYPE))
        {
            i = new BenKenobi(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(Compass.TYPE))
        {
            i = new Compass(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(Gun.TYPE))
        {
            i = new Gun(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(IceScraper.TYPE))
        {
            i = new IceScraper(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(ImprobabilityDrive.TYPE))
        {
            i = new ImprobabilityDrive(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(JadeMonkey.TYPE))
        {
            i = new JadeMonkey(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(Knife.TYPE))
        {
            i = new Knife(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(PortaSmell.TYPE))
        {
            i = new PortaSmell(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(Roadmap.TYPE))
        {
            i = new Roadmap(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(Shovel.TYPE))
        {
            i = new Shovel(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(Torch.TYPE))
        {
            i = new Torch(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(Waterbottle.TYPE))
        {
            i = new Waterbottle(desc, value, typevalue, row, col, held,id);
        }
        else if(type.equals(Food.TYPE))
        {
            return new Food(desc, value, typevalue, row, col, held,id);
        }

        return i;
    }



}
