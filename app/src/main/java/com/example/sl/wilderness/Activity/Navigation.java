package com.example.sl.wilderness.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sl.wilderness.Fragments.AreaInfo;
import com.example.sl.wilderness.Fragments.StatusBar;
import com.example.sl.wilderness.R;

public class Navigation extends AppCompatActivity {

    //this is used as the key and unique number for items
    public static int NUMITEMS;
    public static int PLAYERVERSION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        FragmentManager fm = getSupportFragmentManager();
        //tries to find the fragment by the id of the framelayout in this activity
        StatusBar sb = (StatusBar)fm.findFragmentById(R.id.status);
        AreaInfo ai = (AreaInfo)fm.findFragmentById(R.id.area);

        //try find the fragment, if it doesnt exist then create it
        if(ai == null)
        {
            ai = new AreaInfo();
            fm.beginTransaction().add(R.id.area, ai).commit();
        }
        if(sb == null)
        {
            sb = new StatusBar();
            fm.beginTransaction().add(R.id.status, sb).commit();
        }


    }


    public static int getVersion(){
        PLAYERVERSION ++;
        return PLAYERVERSION;
    }
    public static int getNonUpdatingVersion(){return PLAYERVERSION;}
    public static int getNewItemID()
    {
        NUMITEMS++;
        return NUMITEMS;
    }
}
