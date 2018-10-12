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

import com.example.sl.wilderness.Database.WildernessDb;
import com.example.sl.wilderness.Fragments.StatusBar;
import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.ModelPack.Equipment;
import com.example.sl.wilderness.ModelPack.GameData;
import com.example.sl.wilderness.ModelPack.Item;
import com.example.sl.wilderness.ModelPack.Player;
import com.example.sl.wilderness.R;


import java.util.List;

public class Market extends AppCompatActivity {

    private StatusBar sb_frag;
    private WildernessDb db;
    private RecyclerView buyRecyclerView;
    private RecyclerView sellRecyclerView;
    private SellAdapter sellAdapter;
    private BuyAdapter buyAdapter;

    private GameData mapInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        mapInstance = GameData.getInstance();

        //get and load the database
        FragmentManager fm = getSupportFragmentManager();
        sb_frag = (StatusBar)fm.findFragmentById(R.id.statusmarket);

        if(sb_frag == null)
        {
            sb_frag = new StatusBar();
            fm.beginTransaction().add(R.id.statusmarket, sb_frag).commit();
        }

        Player currentPlayer = mapInstance.getPlayer();
        int row = currentPlayer.getRowLocation();
        int col = currentPlayer.getColLocation();
        Area currArea = mapInstance.getArea(row, col);
        createRecyclerView(currentPlayer.getEquipment(), currArea.getItems());

    }


    private void createRecyclerView(List<Equipment> playerItems, List<Item> areaItems) {
        buyRecyclerView = (RecyclerView) findViewById(R.id.buyrv);
        buyRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));

        sellRecyclerView = (RecyclerView) findViewById(R.id.sellrv);
        sellRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));

        sellAdapter = new SellAdapter(Market.this, playerItems);
        buyAdapter = new BuyAdapter(Market.this, areaItems);
        buyRecyclerView.setAdapter(buyAdapter);
        sellRecyclerView.setAdapter(sellAdapter);
    }


    //ADAPTERS FOR BUY AND SELL
    private class BuyAdapter extends RecyclerView.Adapter<BuyHolder>
    {
        private List<Item> data;
        private Activity activity;
        //SOME DATA HERE
        public BuyAdapter(Activity activity, List<Item> data)
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
        public void onBindViewHolder(BuyHolder holder, int position)
        {
            Item i = data.get(position);
            holder.bind(i);
        }

        @Override
        public BuyHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            return new BuyHolder(layoutInflater, parent);
        }

    }
    private class SellAdapter extends RecyclerView.Adapter<SellHolder>
    {
        private List<Equipment> data;
        private Activity activity;
        //SOME DATA HERE
        public SellAdapter(Activity activity, List<Equipment> data)
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
        public void onBindViewHolder(SellHolder holder, int position)
        {
            Item i = data.get(position);
            holder.bind(i);
        }

        @Override
        public SellHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            return new SellHolder(layoutInflater, parent);
        }

    }


    //VIEW HOLDERS FOR BUY AND SELL
    private class BuyHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView cost, description;
        Button buy;
        public BuyHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.buy_cell, parent, false));
            cost = (TextView) itemView.findViewById(R.id.cost);
            description = (TextView) itemView.findViewById(R.id.desc);
            buy = (Button)itemView.findViewById(R.id.buybutton);
            buy.setOnClickListener(this);
        }

        public void bind(Item data)
        {
            cost.setText("Cost: " + data.getValue());
            description.setText("Type: " + data.getType());

        }
        @Override
        public void onClick(View v)
        {

        }
    }
    private class SellHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView cost, description;
        Button sell;
        public SellHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.sell_cell, parent, false));
            cost = (TextView) itemView.findViewById(R.id.sellcost);
            description = (TextView) itemView.findViewById(R.id.selldesc);
            sell = (Button)itemView.findViewById(R.id.sellbutton);
            sell.setOnClickListener(this);
        }

        public void bind(Item data)
        {
            cost.setText("Cost: " + data.getValue());
            description.setText("Type: " + data.getType());

        }
        @Override
        public void onClick(View v)
        {

        }
    }

    public static Intent getIntent(Context c)
    {
        Intent intent = new Intent(c, Market.class);
        return intent;

    }}
