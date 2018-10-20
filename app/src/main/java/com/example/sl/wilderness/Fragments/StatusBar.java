package com.example.sl.wilderness.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sl.wilderness.ModelPack.GameData;
import com.example.sl.wilderness.ModelPack.Player;
import com.example.sl.wilderness.R;


public class StatusBar extends Fragment implements View.OnClickListener{

    private TextView health,cash,equipment;
    private Button restart;
    private int cashinitial;
    private double healtha, equip;
    Activity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status_bar, container, false);
        // Inflate the layout for this fragment
        health = (TextView) v.findViewById(R.id.health);
        cash = (TextView) v.findViewById(R.id.cash);
        equipment = (TextView) v.findViewById(R.id.equipmentmass);
        restart = (Button) v.findViewById(R.id.restart);
        restart.setOnClickListener(this);

        GameData map = GameData.getInstance();
        updateAll(map.getPlayer());

        return v;
    }




    //This is to set classfgields to initally set the health cash and mass to the view
    public void setupInitial(Player p)
    {
        this.healtha = p.getHealth();
        this.cashinitial = p.getCash();
        this.equip = p.getEquipmentMass();
    }

    public void updateAll(Player p)
    {
        setupInitial(p);
        health.setText("Health: " + healtha);
        cash.setText("Cash: " + cashinitial);
        equipment.setText("Mass: " + equip);
    }

    public void updateHealth(double inHealth)
    {
        health.setText("Health: " + inHealth);
    }
    public void updateCash(int inCash)
    {
        cash.setText("Cash: " + inCash);
    }
    public void updateEquipmentMass(double inEquip)
    {
        equipment.setText("Mass: " + inEquip);
    }


    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.restart:
                ((StatusBarObserver)activity).restartGame("GAME RESTARTING");
                break;
        }
    }

    //This interface allows whoever has the fragment to override this method in the interface
    public interface StatusBarObserver
    {
        void restartGame(String text);
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }




}
