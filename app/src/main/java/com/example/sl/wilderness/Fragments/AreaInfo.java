package com.example.sl.wilderness.Fragments;




import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.R;


public class AreaInfo extends Fragment implements View.OnClickListener{


    View view;
    Area currentArea;
    TextView desc,curr,type,error;
    CheckBox checkBox;

    Activity activity;

    String returnDescription = null;



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
        desc.setOnClickListener(this);
        curr = (TextView) view.findViewById(R.id.current);
        type = (TextView) view.findViewById(R.id.town);

        error = (TextView) view.findViewById(R.id.error);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(this);
        if(currentArea.isStarred())
        {
            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }

        desc.setText("Description: " + currentArea.getDescription());
        curr.setText("Current Area: Row" + currentArea.getRow()  + " Col" + currentArea.getCol());
        if(currentArea.isTown())
        {
            type.setText("Town");
        }
        else
        {
            type.setText("Wilderness");
        }


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
        if(currentArea.isTown())
        {
            type.setText("Town");
        }
        else
        {
            type.setText("Wilderness");
        }

        if(currentArea.isStarred())
        {
            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }
    }

    public void displayError(String inError)
    {

        error.setText(inError);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.checkBox:
                if(currentArea.isStarred())
                {
                    currentArea.clearStarred();
                }
                else {
                    currentArea.setStarred();
                }
                checkBox.setChecked(currentArea.isStarred());

                break;
            case R.id.desc:
                try
                {
                    ((OnDescriptionClickedListener) activity).editDescription();

                }
                catch (ClassCastException e)
                {
                    Log.i("Class cast exception", "Something happened");
                }
                break;
        }
    }

    public void setReturnedDescription(String returnDesc) {
        this.returnDescription = returnDesc;
        this.desc.setText("Description: " + returnDesc);
        currentArea.setDescription(returnDesc);
    }


    //INTERFACE SO THAT ACTIVITYT AND FRAGMENT CAN COMMUNICATE
    public interface OnDescriptionClickedListener {
        public void editDescription();
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }







}
