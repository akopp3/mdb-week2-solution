package com.mdb.android.mdb_week_2_solution;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import java.util.ArrayList;

import static com.mdb.android.mdb_week_2_solution.PokemonAdapter.GRID_MODE;
import static com.mdb.android.mdb_week_2_solution.PokemonAdapter.LIST_MODE;


public class MainActivity extends AppCompatActivity {
    // a single flag to toggle on debugging log messages
    static final boolean DEBUG = true;

    // the toggle buttons for HP filtering
    ToggleButton loToggleButton, medToggleButton, hiToggleButton;
    // switches between grid mode and list mode
    Button modeToggleButton;
    FloatingActionButton searchButton;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    LinearLayoutManager mLinearLayoutManager;
    PokemonAdapter mPokemonAdapter;
    int activeLayoutMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiateVariables();
        // defaults to list layout
        setLinearLayout();
    }

    private void instantiateVariables() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Pokedex pokedex = new Pokedex();
        // loads the data from the stored JSON
        ArrayList<Pokedex.Pokemon> pokemans = pokedex.getPokemon();
        if (DEBUG) {
            Log.d("pika", "Number of pokemon found: " + pokemans.size());
        }

        //  activeLayoutMode actually doesn't matter here, since it's not instantiated.
        // we'll set it later with a set method.
        mPokemonAdapter = new PokemonAdapter(this, pokemans, activeLayoutMode);
        mRecyclerView.setAdapter(mPokemonAdapter);

        // the second parameter is the number of columns in our grid, so 2
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mLinearLayoutManager = new LinearLayoutManager(this);

        searchButton = (FloatingActionButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("MODE", activeLayoutMode);
                startActivity(intent);
            }
        });

        ///modeToggleButton = (Button) findViewById(R.id.modeToggleButton);
        loToggleButton = (ToggleButton) findViewById(R.id.loToggleButton);
        medToggleButton = (ToggleButton) findViewById(R.id.medToggleButton);
        hiToggleButton = (ToggleButton) findViewById(R.id.hiToggleButton);
        // we want all the pokemon to be shown by default
        loToggleButton.setChecked(true);
        medToggleButton.setChecked(true);
        hiToggleButton.setChecked(true);
        View.OnClickListener toggleListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPokemonAdapter.changeFilters(loToggleButton.isChecked(),
                        medToggleButton.isChecked(),
                        hiToggleButton.isChecked());
                // these two calls make sure the RecyclerView refreshes properly
                mPokemonAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mPokemonAdapter);
            }
        };
        loToggleButton.setOnClickListener(toggleListener);
        medToggleButton.setOnClickListener(toggleListener);
        hiToggleButton.setOnClickListener(toggleListener);
    }

    // group all the functionality together for layout changes to make them cleaner to use and write
    private void setLinearLayout() {
        activeLayoutMode = LIST_MODE;
        mPokemonAdapter.setMode(LIST_MODE);
//        modeToggleButton.setText("list mode");
//        modeToggleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setGridLayout();
//            }
//        });
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        // these two calls make sure the RecyclerView refreshes properly
        mRecyclerView.setAdapter(mPokemonAdapter);
        mPokemonAdapter.notifyDataSetChanged();
    }

    private void setGridLayout() {
        activeLayoutMode = GRID_MODE;
        mPokemonAdapter.setMode(GRID_MODE);
//        modeToggleButton.setText("grid mode");
//        modeToggleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setLinearLayout();
//            }
//        });
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        // these two calls make sure the RecyclerView refreshes properly
        mRecyclerView.setAdapter(mPokemonAdapter);
        mPokemonAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toggle:

                if (activeLayoutMode == LIST_MODE) {
                    //change your view and sort it by Alphabet
                    item.setIcon(getResources().getDrawable(R.drawable.list_24dp));
                    item.setTitle("List Layout");
                    setGridLayout();
                } else {
                    //change your view and sort it by Date of Birth
                    item.setIcon(getResources().getDrawable(R.drawable.grids_24dp));
                    item.setTitle("Grid Layout");
                    setLinearLayout();
                }
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

}

