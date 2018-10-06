package com.example.sl.wilderness.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.R;


public class AreaInfo extends Fragment {


    View view;
    Area currentArea;
    TextView desc,curr,type,error;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_area_info, container, false);
        //create reference to the view items
        desc = (TextView) view.findViewById(R.id.desc);
        curr = (TextView) view.findViewById(R.id.current);
        type = (TextView) view.findViewById(R.id.town);
        error = (TextView) view.findViewById(R.id.error);
        desc.setText("Description: " + currentArea.getDescription());
        curr.setText("Current Area: Row" + currentArea.getRow()  + " Col" + currentArea.getCol());
        type.setText("Is Town?: " + currentArea.isTown());
        return view;
    }

    public Area getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(Area inArea){this.currentArea = inArea;}

    public void updateInfo()
    {
        desc.setText("Description: " + currentArea.getDescription());
        curr.setText("Current Area: Row" + currentArea.getRow()  + " Col" + currentArea.getCol());
        type.setText("Is Town?: " + currentArea.isTown());
    }

    public void displayError(String inError)
    {

        error.setText(inError);
    }

}
