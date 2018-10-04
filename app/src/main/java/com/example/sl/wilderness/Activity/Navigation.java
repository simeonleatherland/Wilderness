package com.example.sl.wilderness.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sl.wilderness.Fragments.AreaInfo;
import com.example.sl.wilderness.Fragments.StatusBar;
import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.ModelPack.GameData;
import com.example.sl.wilderness.ModelPack.Player;
import com.example.sl.wilderness.R;

public class Navigation extends AppCompatActivity {

    //this is used as the key and unique number for items
    public static int NUMITEMS;
    public static int PLAYERVERSION;
    int currRow, currCol;
    Area currArea;
    Player mainCharacter;
    GameData map;
    AreaInfo ai;
    StatusBar sb;
    private Button north, south, east, west, option, restart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        FragmentManager fm = getSupportFragmentManager();
        //tries to find the fragment by the id of the framelayout in this activity
        ai = (AreaInfo)fm.findFragmentById(R.id.areanav);

        sb = (StatusBar)fm.findFragmentById(R.id.status);

        //try find the fragment, if it doesnt exist then create it
        if(ai == null)
        {
            ai = new AreaInfo();
            fm.beginTransaction().add(R.id.areanav, ai).commit();
        }
        if(sb == null)
        {
            sb = new StatusBar();
            fm.beginTransaction().add(R.id.statusnav , sb).commit();
        }

        map = GameData.getInstance();
        mainCharacter = new Player(0, 0, 0,0, 100);

        setupViews();

        //allows the user to press and update where they are on the map
        north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {

                try
                {
                    mainCharacter.move(-1, 0, map);
                    playerMoves();

                }
                catch (IllegalArgumentException e)
                {
                    tellThemYouCantDoThat("You cant move there as its outside the map");
                }
            }
        });

        south.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                try
                {
                    mainCharacter.move(1, 0, map);
                    playerMoves();

                }
                catch (IllegalArgumentException e)
                {
                    tellThemYouCantDoThat("You cant move there as its outside the map");

                }
            }
        });

        east.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                try
                {
                    mainCharacter.move(0, 1, map);
                    playerMoves();

                }
                catch (IllegalArgumentException e)
                {
                    tellThemYouCantDoThat("You cant move there as its outside the map");
                }
            }
        });
        west.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                try
                {
                    mainCharacter.move(0, -1, map);
                    playerMoves();

                }
                catch (IllegalArgumentException e)
                {
                    tellThemYouCantDoThat("You cant move there as its outside the map");
                }
            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {


                if(currArea.getType())
                {
                    startActivityForResult(Market.getIntent(Navagation.this, mainCharacter, currArea), REQUEST_CODE_MARKET);
                }
                else
                {

                    startActivityForResult(Wilderness.getIntent(Navagation.this, mainCharacter, currArea), REQUEST_CODE_WILDERNESS);
                }

            }
        });


    }

    public void playerMoves()
    {


        currRow = mainCharacter.getRowLocation();
        currCol = mainCharacter.getColLocation();

        currArea = map.getArea(currRow, currCol);

        ai.
        desc.setText("Description: " ); //+ currArea.getDesc());
        currentArea.setText("Current Area: Row" + mainCharacter.getRowLocation()  + " Col" + mainCharacter.getColLocation());
        type.setText("Type: " + currArea.getType());
        tellThemYouCantDoThat("");
        //calculate the health of the player
        calcHealth();
        //update the label of the player
        updateHealth();
        //CODe that restarts the character if their health gets to 0
        if(Double.compare(mainCharacter.getHealth(), 0.0) == 0)
        {
            restart("You died - RIP - game has restarted");
        }

    }


    private void setupViews() {
        north = (Button)findViewById(R.id.north);
        south = (Button)findViewById(R.id.south);
        east = (Button)findViewById(R.id.east);
        west = (Button)findViewById(R.id.west);
        option = (Button)findViewById(R.id.option);
        restart = (Button)findViewById(R.id.restart);




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
