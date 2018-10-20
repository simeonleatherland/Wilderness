package com.example.sl.wilderness.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sl.wilderness.Database.WildernessDb;
import com.example.sl.wilderness.EquipmentPack.IceScraper;
import com.example.sl.wilderness.EquipmentPack.JadeMonkey;
import com.example.sl.wilderness.EquipmentPack.Roadmap;
import com.example.sl.wilderness.Fragments.AreaInfo;
import com.example.sl.wilderness.Fragments.StatusBar;
import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.ModelPack.Equipment;
import com.example.sl.wilderness.ModelPack.GameData;
import com.example.sl.wilderness.ModelPack.Item;
import com.example.sl.wilderness.ModelPack.Player;
import com.example.sl.wilderness.R;

import java.util.List;

public class Navigation extends AppCompatActivity implements AreaInfo.OnDescriptionClickedListener, StatusBar.StatusBarObserver {

    public static final int REQUEST_CODE_MARKET = 2;
    public static final int REQUEST_CODE_WILDERNESS = 1;
    public static final int REQUEST_CODE_OVERVIEW = 10;


    GameData map;
    AreaInfo ai_frag;
    StatusBar sb_frag;
    WildernessDb db;

    private Button north, south, east, west, option, restart,overview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //get and load the database
        db = new WildernessDb(Navigation.this);
        //load the current player into the classifeld in the wilderness database
        db.load();
        //update the current version of the last player so that the static doenst loose touch with the latest each time the activity is ran

        map = GameData.unpackInstanceFromDB(db);

        //get the fragment mananger
        FragmentManager fm = getSupportFragmentManager();
        //tries to find the fragment by the id of the framelayout in this activity
        ai_frag = (AreaInfo) fm.findFragmentById(R.id.areanav);
        //retrieve the fragment for the status bar if it exists
        sb_frag = (StatusBar) fm.findFragmentById(R.id.statusnav);

        //try find the fragment, if it doesnt exist then create it
        if (ai_frag == null) {
            //create the fragment if not exist
            ai_frag = new AreaInfo();
            fm.beginTransaction().add(R.id.areanav, ai_frag).commit();
        }
        if (sb_frag == null) //same as above
        {
            sb_frag = new StatusBar();
            fm.beginTransaction().add(R.id.statusnav, sb_frag).commit();
        }
        db.dumpCursor();

        //setup all the buttons for the activity, north, south, east or west
        setupViews();
        onClickListeners();
        initialMove();
    }




    private void onClickListeners() {
        //ON CLICK LISTENERS FOR ALL THE BUTTONS ON THE SCREEN
        //allows the user to press and update where they are on the map
        north.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    map.getPlayer().move(-1, 0, map);
                    playerMoves();

                } catch (IllegalArgumentException e) {
                    tellThemYouCantDoThat("You cant move there as \n its outside the map");
                }
            }
        });

        south.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    map.getPlayer().move(1, 0, map);
                    playerMoves();

                } catch (IllegalArgumentException e) {
                    tellThemYouCantDoThat("You cant move there as \n its outside the map");

                }
            }
        });

        east.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    map.getPlayer().move(0, 1, map);
                    playerMoves();

                } catch (IllegalArgumentException e) {
                    tellThemYouCantDoThat("You cant move there as \n its outside the map");
                }
            }
        });
        west.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    map.getPlayer().move(0, -1, map);
                    playerMoves();

                } catch (IllegalArgumentException e) {
                    tellThemYouCantDoThat("You cant move there as \n its outside the map");
                }
            }
        });
        option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ai_frag.getCurrentArea().isTown()) {
                    startActivityForResult(Market.getIntent(Navigation.this), REQUEST_CODE_MARKET);
                } else {
                    startActivityForResult(Wilderness.getIntent(Navigation.this), REQUEST_CODE_WILDERNESS);
                }
            }
        });
        overview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(Overview.getIntent(Navigation.this), REQUEST_CODE_OVERVIEW);

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent)
    {
        //this is if the user selects the restart button on the menu
        //use a result code of 10 just for the sake of it
        if(resultCode == Market.RESTART_KEY && requestCode == REQUEST_CODE_MARKET)
        {
            restartGame("Game will restart now");
        }
        else if(resultCode == Wilderness.RESTART_KEY && requestCode == REQUEST_CODE_WILDERNESS)
        {
            restartGame("Game will restart now");
        }
        else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_WILDERNESS)
        {
            sb_frag.updateCash(map.getPlayer().getCash());
            sb_frag.updateEquipmentMass(map.getPlayer().getEquipmentMass());
            sb_frag.updateHealth(map.getPlayer().getHealth());
            checkIfTheyWon(map.getPlayer());
        }
        else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_MARKET)
        {
            sb_frag.updateCash(map.getPlayer().getCash());
            sb_frag.updateEquipmentMass(map.getPlayer().getEquipmentMass());
            sb_frag.updateHealth(map.getPlayer().getHealth());
            checkIfTheyWon(map.getPlayer());

        }
        else if(resultCode == Overview.RESTART_KEY && requestCode == REQUEST_CODE_OVERVIEW)
        {
            restartGame("Game will restart now");
        }
        else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_OVERVIEW)
        {
            ai_frag.updateInfo();
        }
    }

    @Override
    public void editDescription() {
        //needed something off the stack so that i could set it from an inner class
        final String[] desc = new String[1];
        desc[0] = null;


        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation.this);
        //inflate the layout
        View mView = getLayoutInflater().inflate(R.layout.description_dialog, null);

        final EditText description = (EditText) mView.findViewById(R.id.write);
        Button save = (Button) mView.findViewById(R.id.save);
        Button cancel = (Button) mView.findViewById(R.id.cancel);


        builder.setView(mView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // desc = description.getText().toString();
                Toast.makeText(Navigation.this, "Description Canceled", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!description.getText().toString().isEmpty()) {
                    // desc = description.getText().toString();
                    Toast.makeText(Navigation.this, "Saved Description", Toast.LENGTH_SHORT).show();
                    ai_frag.setReturnedDescription(description.getText().toString());

                } else {
                    Toast.makeText(Navigation.this, "No Description changed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });


    }


    public void tellThemYouCantDoThat(String errorMessage) {
        ai_frag.displayError(errorMessage);
    }


    public void playerMoves() {
        //update the current row and col of the player
        int currRow = map.getPlayer().getRowLocation();
        int currCol = map.getPlayer().getColLocation();
        Area currentArea = map.getArea(currRow, currCol);
        //get the current area object at the users location
        //make the areainfo fragment responsible for the current area
        ai_frag.setCurrentArea(currentArea);

        //make area info update the area info that its responsible for
        ai_frag.updateInfo();
        tellThemYouCantDoThat("");


        //calculate the health of the player and update the view of it
        sb_frag.updateHealth(map.getPlayer().calcHealth());

        //Codee that restarts the character if their health gets to 0
        if (Double.compare(map.getPlayer().getHealth(), 0.0) == 0) {
            restartGame("YOU DIED - GAME RESTARTING");
        }
        //update the area in database to be visited and the player
        currentArea.setExplored();
        db.updateArea(currentArea);

        db.updatePlayer(map.getPlayer());
        db.dumpCursor();
    }

    public void resetAreaFragData() {
        //update the current row and col of the player
        int currRow = map.getPlayer().getRowLocation();
        int currCol = map.getPlayer().getColLocation();

        //get the current area object at the users location
        //make the areainfo fragment responsible for the current area
        ai_frag.setCurrentArea(map.getArea(currRow, currCol));

        //make area info update the area info that its responsible for
        ai_frag.updateInfo();
        tellThemYouCantDoThat("");

        db.updatePlayer(map.getPlayer());
    }

    public void checkIfTheyWon(Player inMainCharacter)
    {
        List<Equipment> m = inMainCharacter.getEquipment();
        boolean jadeMonkey = false, roadmap = false, iceScrapper= false;

        for(Equipment e: m)
        {
            if(e.getType().equals(JadeMonkey.TYPE))
            {
                jadeMonkey = true;
            }
            else if(e.getType().equals(IceScraper.TYPE))
            {
                iceScrapper = true;
            }
            else if(e.getType().equals(Roadmap.TYPE))
            {
                roadmap = true;
            }
        }

        if(jadeMonkey && roadmap && iceScrapper)
        {
            Toast.makeText(this, "YOU WON THE GAME", Toast.LENGTH_SHORT).show();
        }

    }



    private void setupViews() {
        north = (Button) findViewById(R.id.north);
        south = (Button) findViewById(R.id.south);
        east = (Button) findViewById(R.id.east);
        west = (Button) findViewById(R.id.west);
        option = (Button) findViewById(R.id.option);
        restart = (Button) findViewById(R.id.restart);
        overview = (Button) findViewById(R.id.overview);

    }

    //SAME AS PLAYER MOVE BUT WITHOUT HEALTH DEGENERATION
    public void initialMove()
    {

        //update the current row and col of the player
        int currRow = map.getPlayer().getRowLocation();
        int currCol = map.getPlayer().getColLocation();
        Area currentArea = map.getArea(currRow, currCol);

        //update the area in database to be visited and the player
        currentArea.setExplored();
        db.updateArea(currentArea);

        db.updatePlayer(map.getPlayer());
        db.dumpCursor();

    }

    @Override
    public void updateAreaInDB(Area area) {
        db.updateArea(area);
    }


    @Override
    public void restartGame(String text) {
        if (db.clearDatabase()) {

            //create a new database
            db = new WildernessDb(Navigation.this);
            //load classfields into database if they exist
            db.load();

            //reset map instance with database
            map = map.resetInstance(db);
            //set the players
            Player currentPlayer = map.getPlayer();

            //set the initial cash
            currentPlayer.setCash(100);
            //get the current area
            Area current = map.getArea(currentPlayer.getRowLocation(),currentPlayer.getColLocation());
            //set it to be explored
            current.setExplored();
            //reset the map data and the player in the STATUS BAR FRAG
            //update the UI with the new reseted values
            sb_frag.updateAll(currentPlayer);
            //update it in the database
            db.updateArea(current);
            db.updatePlayer(currentPlayer);

            //reset area data in the AREA FRAG\
            resetAreaFragData();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            db.dumpCursor();

        }


    }



}
