package com.example.sl.wilderness.Activity;

import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sl.wilderness.Database.WildernessDb;
import com.example.sl.wilderness.Fragments.AreaInfo;
import com.example.sl.wilderness.Fragments.StatusBar;
import com.example.sl.wilderness.ModelPack.Area;
import com.example.sl.wilderness.ModelPack.GameData;
import com.example.sl.wilderness.ModelPack.Player;
import com.example.sl.wilderness.R;

public class Navigation extends AppCompatActivity implements AreaInfo.OnDescriptionClickedListener {

    //this is used as the key and unique number for items
    public static int NUMITEMS;
    public static int PLAYERVERSION;
    GameData map;
    AreaInfo ai_frag;
    StatusBar sb_frag;
    WildernessDb db;

    private Button north, south, east, west, option, restart;

    @Override
    protected void onSaveInstanceState(Bundle savedInstance)
    {
        super.onSaveInstanceState(savedInstance);
        db.insertPlayer(map.getPlayer());
        db.updateGameGrid(map.getGrid());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //get and load the database
        db = new WildernessDb(Navigation.this);
        //load the current player into the classifeld in the wilderness database
        db.load();
        //update the current version of the last player so that the static doenst loose touch with the latest each time the activity is ran
        PLAYERVERSION = db.PLAYERVERSION;
        retrieveGameData();

        //get the fragment mananger
        FragmentManager fm = getSupportFragmentManager();
        //tries to find the fragment by the id of the framelayout in this activity
        ai_frag = (AreaInfo)fm.findFragmentById(R.id.areanav);
        //retrieve the fragment for the status bar if it exists
        sb_frag = (StatusBar)fm.findFragmentById(R.id.statusnav);

        //try find the fragment, if it doesnt exist then create it
        if(ai_frag == null)
        {
            //create the fragment if not exist
            ai_frag = new AreaInfo();
            fm.beginTransaction().add(R.id.areanav, ai_frag).commit();
        }
        if(sb_frag == null) //same as above
        {
            sb_frag = new StatusBar();
            fm.beginTransaction().add(R.id.statusnav , sb_frag).commit();
        }


        //setup all the buttons for the activity, north, south, east or west
        setupViews();
        onClickListeners();


    }

    public void retrieveGameData()
    {
        //get the instance of the map PROBABLY HAVE A METHOD TO GET FROM DATABASE IN FUTURE
        //same for maing character, this is just for testing

        Player mainCharacter = db.getCurrPlayer();
        if(mainCharacter == null)
        {
            mainCharacter = new Player(0, 0, 0,0, 100);
            db.insertPlayer(mainCharacter);
        }
        Area[][] tempGrid = db.getGrid();
        if(tempGrid[0][0] == null) //if theres nothing in the grid
        {
            //create an instance for the gamedata if it hasnt already been created
            map = GameData.getInstance();
            //insert all the areas into the database and each areas items
            db.insertGameGrid(map.getGrid());
            map.setPlayer(mainCharacter);
        }
        else
        {
            //no need to update database since theres no change in stuff
            map = GameData.getInstanceFromDB(tempGrid, mainCharacter);
        }

    }


    private void onClickListeners() {
        //ON CLICK LISTENERS FOR ALL THE BUTTONS ON THE SCREEN
        //allows the user to press and update where they are on the map
        north.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {

                try
                {
                    map.getPlayer().move(-1, 0, map);
                    playerMoves();

                }
                catch (IllegalArgumentException e)
                {
                    tellThemYouCantDoThat("You cant move there as \n its outside the map");
                }
            }
        });

        south.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                try
                {
                    map.getPlayer().move(1, 0, map);
                    playerMoves();

                }
                catch (IllegalArgumentException e)
                {
                    tellThemYouCantDoThat("You cant move there as \n its outside the map");

                }
            }
        });

        east.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                try
                {
                    map.getPlayer().move(0, 1, map);
                    playerMoves();

                }
                catch (IllegalArgumentException e)
                {
                    tellThemYouCantDoThat("You cant move there as \n its outside the map");
                }
            }
        });
        west.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                try
                {
                    map.getPlayer().move(0, -1, map);
                    playerMoves();

                }
                catch (IllegalArgumentException e)
                {
                    tellThemYouCantDoThat("You cant move there as \n its outside the map");
                }
            }
        });
    }

    @Override
    public void editDescription()
    {
        //needed something off the stack so that i could set it from an inner class
        final String[] desc = new String[1];
        desc[0] = null;


        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation.this);
        //inflate the layout
        View mView = getLayoutInflater().inflate(R.layout.description_dialog, null);

        final EditText description = (EditText)mView.findViewById(R.id.write);
        Button save = (Button)mView.findViewById(R.id.save);
        Button cancel = (Button)mView.findViewById(R.id.cancel);



        builder.setView(mView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    // desc = description.getText().toString();
                    Toast.makeText(Navigation.this, "Description Canceled", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!description.getText().toString().isEmpty())
                {
                   // desc = description.getText().toString();
                    Toast.makeText(Navigation.this, "Saved Description", Toast.LENGTH_SHORT).show();
                    ai_frag.setReturnedDescription(description.getText().toString());

                }
                else
                {
                    Toast.makeText(Navigation.this, "No Description changed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });


    }






    public void tellThemYouCantDoThat(String errorMessage)
    {
        ai_frag.displayError(errorMessage);
    }

    public void playerMoves()
    {
        //update the current row and col of the player
        int currRow = map.getPlayer().getRowLocation();
        int currCol = map.getPlayer().getColLocation();

        //get the current area object at the users location
        //make the areainfo fragment responsible for the current area
        ai_frag.setCurrentArea(map.getArea(currRow, currCol));

        //make area info update the area info that its responsible for
        ai_frag.updateInfo();
        tellThemYouCantDoThat("");



        //calculate the health of the player and update the view of it
        sb_frag.updateHealth(map.getPlayer().calcHealth());

        //Codee that restarts the character if their health gets to 0
        if(Double.compare(map.getPlayer().getHealth(), 0.0) == 0)
        {
            //restart("You died - RIP - game has restarted");
        }

        db.updatePlayer(map.getPlayer());
    }

    /*
    private void checkIfTheyWon(Player inMainCharacter)
    {
        List<Equipment> copyOfList = inMainCharacter.getEquipment();
        List<String> listOfItemAsStrings = new LinkedList<>();
        for(Equipment o : copyOfList)
        {
            listOfItemAsStrings.add(o.getDesc());
        }
        if(listOfItemAsStrings.contains("Backpack") && listOfItemAsStrings.contains("Knife") && listOfItemAsStrings.contains("Shovel"))
        {
            restart("You have WON the game -> the game has now restarted");
        }
    }

    */
/*

    private void restart(String reasonForRestart)
    {
        tellThemYouCantDoThat(reasonForRestart);
        mainCharacter.reset();
        updateHealth();
        updateCash();
        updateEquimentMass();
        map.restartMap();

        //restarts labels to the origin position of the map

        currRow = mainCharacter.getRowLocation();
        currCol = mainCharacter.getColLocation();

        currArea = map.getArea(currRow, currCol);

        desc.setText("Description: "); //+ currArea.getDesc());
        currentArea.setText("Current Area: Row" + mainCharacter.getRowLocation()  + " Col" + mainCharacter.getColLocation());
        type.setText("Description: " + currArea.getType());

        //restarts the map details to what it was

    }
*/


    private void setupViews() {
        north = (Button)findViewById(R.id.north);
        south = (Button)findViewById(R.id.south);
        east = (Button)findViewById(R.id.east);
        west = (Button)findViewById(R.id.west);
        option = (Button)findViewById(R.id.option);
        restart = (Button)findViewById(R.id.restart);



        //proably will be from the data base, but will be done later on
        int currRow = map.getPlayer().getRowLocation();
        int currCol = map.getPlayer().getColLocation();
        //get the current area

        //set the current area
        ai_frag.setCurrentArea(map.getArea(currRow, currCol));

        sb_frag.setupInitial(map.getPlayer().getHealth(), map.getPlayer().getCash(), map.getPlayer().getEquipmentMass());
        /*
        ai_frag.updateInfo();
        //update the user on whats happeneing with the game
        tellThemYouCantDoThat("Game has started");

        //Update the status bar fragment of their views
        sb_frag.updateCash(mainCharacter.getCash());
        sb_frag.updateHealth(mainCharacter.getHealth());
        sb_frag.updateEquipmentMass(mainCharacter.getEquipmentMass());
        */

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
