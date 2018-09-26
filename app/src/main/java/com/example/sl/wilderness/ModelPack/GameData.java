package com.example.sl.wilderness.ModelPack;

public class GameData {
    //game data grid that holds all the data thats on the map
    private Area[][] grid;
    //the main current player
    private Player player;
    //static instance of the class to keep consistent
    private static GameData instance;

    //static get method
    public static GameData getInstance()
    {
        if(instance == null)
        {
            //havent decided what im going to do in here if there isnt an instance
            //probs need to create the grid, get the player somehow if it has or hasnt been created
            instance = new GameData();
        }
        return instance;
    }


    public void generateMap()
    {

    }

}
