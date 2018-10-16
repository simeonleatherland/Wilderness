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

import java.util.LinkedList;
import java.util.List;

public class SmellOScope extends AppCompatActivity implements StatusBar.StatusBarObserver {

    public static int RESTART_KEY = 15;
    private GameData mapInstance;
    private WildernessDb db;
    private RecyclerView buyRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smell_oscope);
        //retrieve the map and the database from the game data
        mapInstance = GameData.getInstance();
        db = mapInstance.getDatabase();


        //retrieve the current player and the current area
        Player currentPlayer = mapInstance.getPlayer();
        int row = currentPlayer.getRowLocation();
        int col = currentPlayer.getColLocation();
        Area[][] grid = mapInstance.getGrid();


        Area[][] smellGrid = calcArrayToDispaly(grid, row, col);
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

                                     }
                                 }

        );

        createRecyclerView(currentPlayer.getEquipment(),smellGrid);

    }

    private Area[][] calcArrayToDispaly(Area[][] grid, int playerRow, int playerCol) {
        int smellRow, smellCol;//row and col of the area near by that can be seen by the smelloscope

        int left = 0,right=0,up=0,down=0;//this is the count of how big the array is to the left, right and up and down
        //such that i can then generate an array of the size
        //LEFT
        try{
            Area a = grid[playerRow-2][playerCol];
            left = 2; // there is areas 2 spaces to the left of the player
        }
        catch(IndexOutOfBoundsException e)
        {
            try{
                Area a = grid[playerRow-1][playerCol];
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
            Area a = grid[playerRow+2][playerCol];
            right = 2; // there is areas 2 spaces to the left of the player
        }
        catch(IndexOutOfBoundsException e)
        {
            try{
                Area a = grid[playerRow+1][playerCol];
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
            Area a = grid[playerRow][playerCol-2];
            up = 2; // there is areas 2 spaces to the left of the player
        }
        catch(IndexOutOfBoundsException e)
        {
            try{
                Area a = grid[playerRow][playerCol-1];
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
            Area a = grid[playerRow][playerCol+2];
            down = 2; // there is areas 2 spaces to the left of the player
        }
        catch(IndexOutOfBoundsException e)
        {
            try{
                Area a = grid[playerRow][playerCol+2];
                //player is on the edge of the map to the left
                down = 1; // there is areas 1 space to the left of the player

            }
            catch(IndexOutOfBoundsException a)
            {
                down = 0;
            }
        }

        //total them up, plus one for the player itself
        smellRow = left+right+1;
        smellCol= up+down+1;

        //create the grid
        Area[][] smellGrid = new Area[smellRow][smellCol];
        //populate the grid
        for(int ii = 0; ii < smellRow; ii++)
        {
            for(int jj = 0; jj < smellCol; jj++)
            {
                //calculate the equivilent row and col of the grid array
                int gridRow = (playerRow-left)+ii; //NEED TO USE RIGHT IN HERE
                int gridCol = (playerCol-up) +jj;
                //access the grid array and fill up the smell grid to display
                smellGrid[ii][jj] = grid[gridRow][gridCol];
            }
        }
        return smellGrid;
    }


    private void createRecyclerView(List<Equipment> playerItems, Area[][] area) {
        buyRecyclerView = (RecyclerView) findViewById(R.id.rvsmell);
        buyRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));
        
        Adapter a = new Adapter(this, area);
        buyRecyclerView.setAdapter(a);
    }



    private class Adapter extends RecyclerView.Adapter<Holder>
    {
        private Area[][]  data;
        private Activity activity;
        //SOME DATA HERE
        public Adapter(Activity activity, Area[][] data)
        {
            this.activity = activity;
            this.data = data;
        }

        @Override
        public int getItemCount()
        {
            return data.length * data[0].length;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position)
        {
           Item i = data[position%data.length][position/data.length].getItems().get(0);
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
            int row = 0;
            int col = 0;
            desc.setText(data.getType());
            /*row.setText(row + "east");
            row.setText(row + "west");
            col.setText(col + "north");
            col.setText(col + "south");*/

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
