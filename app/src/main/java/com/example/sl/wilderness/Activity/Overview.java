package com.example.sl.wilderness.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.List;

public class Overview extends AppCompatActivity implements StatusBar.StatusBarObserver {

    private WildernessDb db;
    private GameData mapInstance;
    private Player currentPlayer;
    private StatusBar sb_frag;
    private RecyclerView wholeMapRecyclerView;

    public static final int RESTART_KEY = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        //retrieve the map and the database from the game data
        mapInstance = GameData.getInstance();
        //get reference locally for the database
        db = mapInstance.getDatabase();

        //retrieve the current player and the current area
        currentPlayer = mapInstance.getPlayer();



        //setup the status bar fragment
        FragmentManager fm = getSupportFragmentManager();
        sb_frag = (StatusBar)fm.findFragmentById(R.id.statusmarket);

        if(sb_frag == null)
        {
            sb_frag = new StatusBar();
            fm.beginTransaction().add(R.id.statusmarket, sb_frag).commit();
        }

        sb_frag.setupInitial(currentPlayer);

        //create the leave button
        Button leave = (Button)findViewById(R.id.leavebutton);
        leave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                finish();
            }
        });

        //set up the 2d recycler view
        createRecyclerView(mapInstance.getGrid());

    }

    private void createRecyclerView(Area[][] grid) {
        wholeMapRecyclerView = (RecyclerView) findViewById(R.id.buyrv);
        wholeMapRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),GameData.ROW, GridLayoutManager.HORIZONTAL, false));
    }

    public static Intent getIntent(Context c)
    {
        Intent intent = new Intent(c, Overview.class);
        return intent;

    }
    @Override
    public void restartGame(String text)
    {
        Intent returnIntent = new Intent();
        setResult(RESTART_KEY, returnIntent);
        finish();
    }


    private class Adapter extends RecyclerView.Adapter<Overview.Holder>
    {
        private List<Equipment> data;
        private Activity activity;
        //SOME DATA HERE
        public Adapter(Activity activity, List<Equipment> data)
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


    //VIEW HOLDERS FOR BUY AND SELL
    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        Item data;
        public Holder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.area_cell, parent, false));

        }

        public void bind(Item data)
        {
            this.data = data;


        }
        @Override
        public void onClick(View v)
        {


        }
    }

}
