package com.example.sl.wilderness.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sl.wilderness.R;

public class Navigation extends AppCompatActivity {

    //this is used as the key and unique number for items
    public static int NUMITEMS;
    public static int PLAYERVERSION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

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
