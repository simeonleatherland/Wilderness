package com.example.sl.wilderness.ModelPack;

import android.util.Log;

import com.example.sl.wilderness.Activity.Navigation;
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
    public static long VERSION;

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
        this.health = health;
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
    public Player(int cash, double equipmentMass, int row, int col, int health, long inVersion)
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
        this.health = health;
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
        //get the version of the player
        VERSION = inVersion;

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
    //set health checks the change value, this is usally
    public void setHealth(double changeValue) throws IllegalStateException
    {

        if(health + changeValue < 0)
        {
            health = 0;
            //set health to 0, throw illegal state such player has to die
            throw new IllegalStateException("PLAYER DIED");

        }
        else if(health + changeValue > 100)
        {
            health = 100;
        }
        else {
            health += changeValue;
        }

    }

    //calculates the health of the player as it moves, and returns in
    public double calcHealth()
    {
        health = (int)(Math.max(00, health - 5.0 - (equipmentMass / 2.0)));
        return health;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public void setRowLocation(int rowLocation) {
        this.rowLocation = rowLocation;
    }

    public void setColLocation(int colLocation) {
        this.colLocation = colLocation;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }


    public void setEquipmentMass(double equipmentMass) {

        this.equipmentMass = equipmentMass;
        if(this.equipmentMass < 0)
        {
            this.equipmentMass = 0;
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

    public void purchaseEquipment(Equipment e)
    {
        //see if they can purchase it with the cash they have
        if(getCash() - e.getValue() >=0 )
        {
            //set their cash down if they can afford
            setCash(getCash() - e.getValue());
            //add the item to the equipment list
            equipment.add(e);
            //increase equipt mass by that
            setEquipmentMass(getEquipmentMass()+ e.getTypeValue());
        }
        else
        {
            throw new IllegalArgumentException("cant afford that");
        }
    }

    public void purchaseFood(Food e) throws IllegalStateException
    {
        //see if they can purchase it with the cash they have
        if(getCash() - e.getValue() >=0 )
        {
            //set their cash down if they can afford
            setCash(getCash() - e.getValue());
            //increase their health by the food
            setHealth(e.getTypeValue()); //note type value is the health for food, mass for equipment
        }
        else
        {
            throw new IllegalArgumentException("cant afford that");
        }
    }

    public void sellEquipment(Equipment e) throws IllegalArgumentException
    {
        if(e != null)
        {
            //set the cash to 0.75 of the value from the spec sheet
            setCash((int)(e.getValue()*0.75 + cash));
            //remove from the list of the plyaer
            getEquipment().remove(e);
            setEquipmentMass(getEquipmentMass() - e.getTypeValue());

        }
        else
        {
            throw new IllegalArgumentException("cant sell that item for some reason");
        }

    }

    public void pickupEquipment(Equipment e)
    {
        if(e != null)
        {
            //add the item to the equipment list
            equipment.add(e);
            //increase equipt mass by that
            setEquipmentMass(getEquipmentMass()+ e.getTypeValue());
        }
        else
        {
            throw new IllegalArgumentException("null object");
        }

    }

    public void pickupFood(Food e) throws IllegalStateException //throw excpetion if they die
    {
        if(e != null)
        {
            //increase their health by the food
            setHealth(e.getTypeValue()); //note type value is the health for food, mass for equipment
        }
        else
        {
            throw new IllegalArgumentException("null object");
        }

    }

    public void dropEquipment(Equipment e) throws IllegalArgumentException
    {
        if(e != null)
        {
            //remove from the list of the player
            getEquipment().remove(e);
            setEquipmentMass(getEquipmentMass()- e.getTypeValue());

        }
        else
        {
            throw new IllegalArgumentException("null object");
        }

    }



    public void setVERSION(long version)
    {
        VERSION = version;
    }
}
