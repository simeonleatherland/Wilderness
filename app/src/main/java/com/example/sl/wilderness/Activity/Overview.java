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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sl.wilderness.Database.WildernessDb;
import com.example.sl.wilderness.Fragments.AreaInfo;
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
    private MapAdapter mapAdapter;
    private AreaInfo ai_frag;

    public static final int RESTART_KEY = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        //unpacks game data and database
        setUpOverview();

        setupFragments();

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

    private void setupFragments() {
        //make the areainfo fragment responsible for the current area
        //setup the status bar fragment
        FragmentManager fm = getSupportFragmentManager();
        sb_frag = (StatusBar)fm.findFragmentById(R.id.statusmarket);

        if(sb_frag == null)
        {
            sb_frag = new StatusBar();
            fm.beginTransaction().add(R.id.statusmarket, sb_frag).commit();
        }
        sb_frag.setupInitial(currentPlayer);

        ai_frag = (AreaInfo) fm.findFragmentById(R.id.overviewlayout);

        //try find the fragment, if it doesnt exist then create it
        if (ai_frag == null) {
            //create the fragment if not exist
            ai_frag = new AreaInfo();
            fm.beginTransaction().add(R.id.overviewlayout, ai_frag).commit();
        }

        ai_frag.setCurrentArea(null);




    }

    private void setUpOverview() {
        //retrieve the map and the database from the game data
        mapInstance = GameData.getInstance();
        //get reference locally for the database
        db = mapInstance.getDatabase();

        //retrieve the current player and the current area
        currentPlayer = mapInstance.getPlayer();

    }


    private void createRecyclerView(Area[][] grid) {

        wholeMapRecyclerView = (RecyclerView) findViewById(R.id.rvoverview);
        wholeMapRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),GameData.ROW, GridLayoutManager.HORIZONTAL, false));
        mapAdapter = new MapAdapter(Overview.this, grid);
        wholeMapRecyclerView.setAdapter(mapAdapter);
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


    private class MapHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image1,image2,image3, image4,image5;
        private Area mapElement;
        //pass it ViewHolders constructors, this is the base class that will then hold
        //on to the fragment selector list view hierachy
        public MapHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.area_cell, parent, false));
            itemView.setOnClickListener(this);
            //sets the recycler views highet at runtime, divide that by the number of cells that can fit
            int size = parent.getMeasuredHeight() / GameData.ROW + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;
            image1 = (ImageView)itemView.findViewById(R.id.imagetopleft);


        }

        @Override
        public void onClick(View view) {

            if(mapElement.isExplored()) //if it hasnt been explored, be black
            {
                //set the current area of the frag
                ai_frag.setCurrentArea(mapElement);
                //update the info
                ai_frag.updateInfo();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "You cant see that area", Toast.LENGTH_SHORT).show();

            }
        }
        public void bind(Area s)
        {
            mapElement = s;
            if(!s.isExplored()) //if it hasnt been explored, be black
            {
                image1.setImageResource(R.drawable.blackquestion);
            }
            else if(!s.isTown()) //if its been explored and it is wilderness
            {
                image1.setImageResource(R.drawable.ic_tree1);
            }
            else if(s.isTown()) // if the place is a town
            {
                image1.setImageResource(R.drawable.ic_building1);
            }
        }
    }

    private class MapAdapter extends RecyclerView.Adapter<MapHolder>{

        Area[][] md;
        private Activity activity;

        public MapAdapter(Activity inActivity, Area[][] mapData)
        {
            this.activity = inActivity;
            md=mapData;
        }

        @Override
        public MapHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            return new MapHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(MapHolder holder, int position) {
            Area s = mapInstance.getArea(position%mapInstance.ROW, position/mapInstance.ROW);
            holder.bind(s);
        }

        @Override
        public int getItemCount()
        {
            //return MapData.get();
            int i = GameData.ROW * GameData.COL;
            return i;
        }

    }


}
