package com.example.sl.wilderness.Database;

public class DbSchema {
    public static class PlayerTable
    {
        public static final String NAME = "player";
        public static class Cols
        {
            public static final String ID = "player_id"; //allows multiple of the same types at the same row and col in the map
            public static final String ROW = "row_location";
            public static final String COL = "col_location";
            public static final String CASH = "cash";
            public static final String HEALTH = "health";
            public static final String MASS = "mass";

        }
    }
    public static class AreaTable
    {
        public static final String NAME = "areas";
        public static class Cols
        {
            public static final String ID = "item_id";
            //row and column location of where it is stored in the grid
            public static final String ROW = "row_location";
            public static final String COL = "col_location";
            public static final String TOWN = "is_town";
            public static final String STARRED = "starred";
            public static final String EXPLORED = "explored";
            public static final String DESCRIPTION = "description";



        }
    }
    public static class ItemTable
    {
        public static final String NAME = "items";
        public static class Cols
        {
           // public static final String _id = "_id"; //allows multiple of the same types at the same row and col in the map
            public static final String PRICE = "price";

            public static final String DESCRIPTION = "description";
            public static final String TYPE = "item_type"; //equipment or food
            public static final String TYPEVALUE = "type_value_massORhealth";

            //is the player currently holding this
            public static final String HELD = "is_help";

            //if it isnt held then set the row and col location of where it is held in the game map
            public static final String ROWinMAP = "row_location";
            public static final String COLinMAP = "col_location";



        }
    }
}
