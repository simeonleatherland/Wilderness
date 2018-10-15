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

import java.util.List;

public class Wilderness extends AppCompatActivity implements StatusBar.StatusBarObserver{

    private StatusBar sb_frag;
    private WildernessDb db;
    private RecyclerView buyRecyclerView;
    private RecyclerView sellRecyclerView;
    private SellAdapter sellAdapter;
    private BuyAdapter buyAdapter;
    private Player currentPlayer;
    private GameData mapInstance;
    private Area currArea;
    public static final int RESTART_KEY = 11;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilderness);

        //retrieve the map and the database from the game data
        mapInstance = GameData.getInstance();
        db = mapInstance.getDatabase();


        //retrieve the current player and the current area
        currentPlayer = mapInstance.getPlayer();
        int row = currentPlayer.getRowLocation();
        int col = currentPlayer.getColLocation();
        currArea = mapInstance.getArea(row, col);

        //get and load the database
        FragmentManager fm = getSupportFragmentManager();
        sb_frag = (StatusBar)fm.findFragmentById(R.id.statusmarket);

        if(sb_frag == null)
        {
            sb_frag = new StatusBar();
            fm.beginTransaction().add(R.id.statusmarket, sb_frag).commit();
        }

        sb_frag.setupInitial(currentPlayer);
        Button leave = (Button)findViewById(R.id.leavebutton);
        leave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                finish();
            }
        });

        createRecyclerView(currentPlayer.getEquipment(), currArea.getItems());

    }


    private void createRecyclerView(List<Equipment> playerItems, List<Item> areaItems) {
        buyRecyclerView = (RecyclerView) findViewById(R.id.buyrv);
        buyRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));

        sellRecyclerView = (RecyclerView) findViewById(R.id.sellrv);
        sellRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));

        sellAdapter = new SellAdapter(this, playerItems);
        buyAdapter = new BuyAdapter(this, areaItems);
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
        Item data;
        public BuyHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.buy_cell, parent, false));
            cost = (TextView) itemView.findViewById(R.id.cost);
            description = (TextView) itemView.findViewById(R.id.desc);
            buy = (Button)itemView.findViewById(R.id.buybutton);
            buy.setOnClickListener(this);
            buy.setText("PICKUP");
        }

        public void bind(Item data)
        {
            this.data = data;
            cost.setText("Cost: free");
            description.setText("Type: " + data.getType());

        }
        @Override
        public void onClick(View v)
        {
            try
            {
                if(data.getType().equals("FOOD"))
                {
                    //try purchase the food, throw exception if they have no cash
                    currentPlayer.pickupFood((Food)data);
                    //update health and cash of the player after buying food
                    sb_frag.updateHealth(currentPlayer.getHealth());

                    //remove the item from the area food or equipment
                    currArea.getItems().remove(data);
                    db.removeItem(data); //remove it once the food has been consumed
                }
                else
                {
                    //purhcase equipment if they can
                    currentPlayer.pickupEquipment((Equipment)data);
                    sb_frag.updateEquipmentMass(currentPlayer.getEquipmentMass());

                    //set the data to be held and the row and column
                    data.setHeld(true);
                    data.setRow(-1);
                    data.setCol(-1);

                    //remove the item from the area food or equipment
                    currArea.getItems().remove(data);
                }


                //update the area and the player in the database... NEED TO FIX THIS
                db.updateArea(currArea);
                //update the player as the player will now have an extra item
                db.updatePlayer(currentPlayer);

                //tell the adapters that shit changed
                buyAdapter.notifyDataSetChanged();
                sellAdapter.notifyDataSetChanged();
                db.dumpCursor();
            }
            catch(IllegalArgumentException i)
            {
                Toast.makeText(Wilderness.this, "You cant pick this up", Toast.LENGTH_SHORT).show();
            }
            catch(IllegalStateException e)
            {
                //MAKE THE PLAYER DIEDf her don't know how to do this yet
            }



        }
    }
    private class SellHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView cost, description;
        Button sell, use;
        Item data;

        public SellHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.sell_cell, parent, false));
            cost = (TextView) itemView.findViewById(R.id.sellcost);
            description = (TextView) itemView.findViewById(R.id.selldesc);
            sell = (Button)itemView.findViewById(R.id.sellbutton);
            use = (Button)itemView.findViewById(R.id.usebutton);
            sell.setOnClickListener(this);
            sell.setText("DROP");
            cost.setText("");
        }

        public void bind(Item data)
        {
            this.data = data;
            description.setText("Type: " + data.getType());

        }
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.sellbutton: //sell stuff
                    try
                    {
                        //sell equipment at an increased cost
                        currentPlayer.dropEquipment((Equipment)data);
                        sb_frag.updateCash(currentPlayer.getCash());
                        sb_frag.updateEquipmentMass(currentPlayer.getEquipmentMass());

                        //add the item to the area
                        currArea.getItems().add(data);

                        //set the data to no longer held and the row and column
                        data.setHeld(false);
                        data.setRow(currArea.getRow());
                        data.setCol(currArea.getCol());

                        //update the area and the player in the database... NEED TO FIX THIS
                        db.updateArea(currArea);
                        db.updatePlayer(currentPlayer);

                        //tell the adapters that shit changed
                        buyAdapter.notifyDataSetChanged();
                        sellAdapter.notifyDataSetChanged();
                        db.dumpCursor();

                    }
                    catch(IllegalArgumentException i)
                    {
                        Toast.makeText(Wilderness.this, "You cant sell that", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.usebutton: //use an item
                    //do some stuff here to use the item
                    break;

            }


        }
    }

    public static Intent getIntent(Context c)
    {
        Intent intent = new Intent(c, Wilderness.class);
        return intent;

    }

    @Override
    public void restartGame(String text)
    {
        Intent returnIntent = new Intent();
        setResult(RESTART_KEY, returnIntent);
        finish();
    }
    
}

