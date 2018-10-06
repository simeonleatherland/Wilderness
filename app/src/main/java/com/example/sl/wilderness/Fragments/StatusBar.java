package com.example.sl.wilderness.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sl.wilderness.R;


public class StatusBar extends Fragment {

    private TextView health,cash,equipment;
    private int cashinitial;
    private double healtha, equip;


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
        health.setText("Health: " + healtha);
        cash.setText("Cash: " + cashinitial);


        return v;
    }


    //This is to set classfgields to initally set the health cash and mass to the view
    public void setupInitial(double health, int cash, double equip)
    {
        this.healtha = health;
        this.cashinitial = cash;
        this.equip = equip;
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
        equipment.setText("Equipment Mass: " + inEquip);
    }




}
