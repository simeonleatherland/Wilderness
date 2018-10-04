package com.example.sl.wilderness.ModelPack;

import android.util.Log;

import com.example.sl.wilderness.ModelPack.Equipment;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private int rowLocation;
    private int colLocation;
    private int cash;
    private double health;
    private double equipmentMass;
    private List<Equipment> equipment;

    public Player(int cash, double equipmentMass, int row, int col, int health)
    {
        if(cash < 0)
        {
            this.cash = cash;
        }
        else
        {
            this.cash = cash;
        }
        if(equipmentMass < 0)
        {
            this.equipmentMass = equipmentMass;

        }
        else
        {
            this.equipmentMass = equipmentMass;

        }
        equipment = new LinkedList<>();
        health = health;
        if(validateRowCol(row, col))
        {
            rowLocation = row;
            colLocation = col;
        }
        else
        {
            Log.i("BadRowCol", "Please fix row and col as its out of scope");
            throw new IllegalArgumentException("Row and Col are bad");
        }

    }


    public void move(int row, int col, GameData g) throws IllegalArgumentException
    {
        //increase to what the user wants to do... increase, then test to see if it fits
        //assume if passes testGrid you can move to that location
        if(row != 0 && col == 0)
        {
            rowLocation += row;
            if(rowLocation == g.ROW)
            {
                rowLocation--;
                throw new IllegalArgumentException();
            }
            else if(rowLocation < 0)
            {
                rowLocation++;
                throw new IllegalArgumentException();
            }

        }
        else if(row == 0 && col != 0)
        {
            //increase col location
            colLocation+=col;
            //if its greater than the col location or its less than 0
            if(colLocation == g.COL )
            {
                colLocation--;
                throw new IllegalArgumentException();
            }
            else if(colLocation < 0)
            {
                colLocation++;
                throw new IllegalArgumentException();
            }

        }


    }



    private boolean validateRowCol(int row, int col) {
        return true;
    }
    public void setHealth(int changeValue)
    {
        if(health + changeValue < 0)
        {
            health = 0;
        }
        else if(health + changeValue > 100)
        {
            health = 100;
        }
        else {
            health += changeValue;
        }

    }


    public int getRowLocation() {
        return rowLocation;
    }

    public int getColLocation() {
        return colLocation;
    }

    public int getCash() {
        return cash;
    }

    public double getHealth() {
        return health;
    }

    public double getEquipmentMass() {
        return equipmentMass;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }


}
