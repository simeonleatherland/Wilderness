package com.example.sl.wilderness.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sl.wilderness.Database.WildernessDb;
import com.example.sl.wilderness.Fragments.StatusBar;
import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.ModelPack.Equipment;
import com.example.sl.wilderness.ModelPack.Food;
import com.example.sl.wilderness.ModelPack.GameData;
import com.example.sl.wilderness.ModelPack.Item;
import com.example.sl.wilderness.ModelPack.Player;
import com.example.sl.wilderness.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SmellOScope extends AppCompatActivity implements StatusBar.StatusBarObserver {

    public static int RESTART_KEY = 15;
    private GameData mapInstance;
    private WildernessDb db;
    private RecyclerView buyRecyclerView;
    private Player currentPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smell_oscope);
        //retrieve -the map and the database from the game data
        mapInstance = GameData.getInstance();
        db = mapInstance.getDatabase();



        //retrieve the current player and the current area
        currentPlayer = mapInstance.getPlayer();
        int row = currentPlayer.getRowLocation();
        int col = currentPlayer.getColLocation();
        Area[][] grid = mapInstance.getGrid();

        List<Item> smellList = calcArrayToDispaly(grid, row, col);
        //get and load the database
        FragmentManager fm = getSupportFragmentManager();
        StatusBar sb_frag = (StatusBar)fm.findFragmentById(R.id.statucoverview);

        if(sb_frag == null)
        {
            sb_frag = new StatusBar();
            fm.beginTransaction().add(R.id.statucoverview, sb_frag).commit();
        }

        sb_frag.setupInitial(currentPlayer);

        Button leaves = (Button)findViewById(R.id.leavebuttons);

        leaves.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         setResult(RESULT_OK, null);
                                         finish();
                                     }
                                 }

        );

        createRecyclerView(smellList);

    }

    private List<Item> calcArrayToDispaly(Area[][] grid, int playerRow, int playerCol) {
        int smellRowSize, smellColSize;//row and col of the area near by that can be seen by the smelloscope

        int left = 0,right=0,up=0,down=0;//this is the count of how big the array is to the left, right and up and down
        //such that i can then generate an array of the size
        //LEFT
        try{
            Area a = grid[playerRow][playerCol-2];
            left = 2; // there is areas 2 spaces to the left of the player
        }
        catch(IndexOutOfBoundsException e)
        {
            try{
                Area a = grid[playerRow][playerCol-1];
                //player is on the edge of the map to the left
                left = 1; // there is areas 1 space to the left of the player
            }
            catch(IndexOutOfBoundsException a)
            {
               left = 0;
            }
        }
        //RIGHT
        try{
            Area a = grid[playerRow][playerCol+2];
            right = 2; // there is areas 2 spaces to the left of the player
        }
        catch(IndexOutOfBoundsException e)
        {
            try{
                Area a = grid[playerRow][playerCol+1];
                //player is on the edge of the map to the left
                right = 1; // there is areas 1 space to the left of the player
            }
            catch(IndexOutOfBoundsException a)
            {
                right = 0;
            }
        }

        //UP
        try{
            Area a = grid[playerRow-2][playerCol];
            up = 2; // there is areas 2 spaces to the left of the player
        }
        catch(IndexOutOfBoundsException e)
        {
            try{
                Area a = grid[playerRow-1][playerCol];
                //player is on the edge of the map to the left
                up = 1; // there is areas 1 space to the left of the player
            }
            catch(IndexOutOfBoundsException a)
            {
               up = 0;
            }
        }
        //down
        try{
            Area a = grid[playerRow+2][playerCol];
            down = 2; // there is areas 2 spaces down of the player
        }
        catch(IndexOutOfBoundsException e)
        {
            try{
                Area a = grid[playerRow+1][playerCol];
                //player is on the edge of the map to the left
                down = 1; // there is areas 1 space to the left of the player

            }
            catch(IndexOutOfBoundsException a)
            {
                down = 0;
            }
        }

        //total them up, plus one for the player itself, size of the new array
        smellRowSize = up+down+1; // size of the rows, how many rows
        smellColSize = left+right+1; //how many columns


        //create the grid
        List<Item> smellGrid = new ArrayList<>();
        //populate the grid
        for(int ii = 0; ii < smellRowSize; ii++)
        {
            for(int jj = 0; jj < smellColSize; jj++)
            {
                //calculate the equivalent row and col of the grid array, normalise them
                int gridRow = (playerRow-up)+ii;
                int gridCol = (playerCol-left) +jj;
                for(Item i : grid[gridRow][gridCol].getItems())
                {
                    //access the grid array and fill up the smell grid to display
                    smellGrid.add(i);
                }



            }
        }
        return smellGrid;
    }


    private void createRecyclerView(List<Item> area) {
        buyRecyclerView = (RecyclerView) findViewById(R.id.rvsmell);
        buyRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));
        
        Adapter a = new Adapter(this, area);
        buyRecyclerView.setAdapter(a);
    }



    private class Adapter extends RecyclerView.Adapter<Holder>
    {
        private List<Item>  data;
        private Activity activity;
        //SOME DATA HERE
        public Adapter(Activity activity, List<Item> data)
        {
            this.activity = activity;
            this.data = data;
        }

        @Override
        public int getItemCount()
        {
            return data.size();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position)
        {
           Item i = data.get(position);
           holder.bind(i);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            return new Holder(layoutInflater, parent);
        }

    }


    private class Holder extends RecyclerView.ViewHolder
    {
        TextView desc, row, col;
        Item data;

        public Holder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.smell_cell, parent, false));
            desc = (TextView) itemView.findViewById(R.id.name);
            row = (TextView) itemView.findViewById(R.id.row);
            col = (TextView) itemView.findViewById(R.id.col);
        }

        public void bind(Item data)
        {
            this.data = data;
            int currRow = data.getRow();
            int currCol = data.getCol();
            int playerRow = currentPlayer.getRowLocation();
            int playerCol = currentPlayer.getColLocation();

            desc.setText(data.getType());
            
            if(playerCol > currCol) // if the item is to the left the player
            {
                col.setText((playerCol-currCol) + " West");
            }
            else if(playerCol < currCol)// if the player is to the right
            {
                col.setText((currCol -  playerCol )+ " East");
            }
            else
            {
                col.setText("0 West");
            }

            if(playerRow > currRow) //if the player is higher than the item
            {
                row.setText((playerRow - currRow) + " North");
            }
            else if(playerRow < currRow)//if the item is north
            {
                row.setText((currRow- playerRow ) + " South");
            }
            else
            {
                row.setText("0 North");

            }

        }
        


    }

    public static Intent getIntent(Context c)
    {
        Intent intent = new Intent(c, SmellOScope.class);
        return intent;

    }

    @Override
    public void restartGame(String text)
    {
        setResult(RESTART_KEY, null);
        finish();
    }

}
