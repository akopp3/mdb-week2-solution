package com.mdb.android.mdb_week_2_solution;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


import static com.mdb.android.mdb_week_2_solution.PokemonAdapter.GRID_MODE;
import static com.mdb.android.mdb_week_2_solution.PokemonAdapter.LIST_MODE;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rv;
    PokemonAdapter adapter;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Pokedex pokedex = new Pokedex();
        final ArrayList<Pokedex.Pokemon> pokemans = pokedex.getPokemon();


        rv = (RecyclerView) findViewById(R.id.searchRecycler);
        adapter = new PokemonAdapter(this, pokemans, GRID_MODE);

        rv.setLayoutManager(new GridLayoutManager(this, 2));

        searchBar = (EditText) findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //copy pokemon array into a temp copy so that the original is untouched ya feel
                ArrayList<Pokedex.Pokemon> pokemonCopy = pokemans;
                ArrayList<Integer> indices = new ArrayList<Integer>();

                //iterate through array and keep the indices of the pokemon that correspond with current charSequence
                for (int j = 0; j < pokemans.size(); j++) {
                    Pokedex.Pokemon pokemon = pokemonCopy.get(j);
                    if (pokemon.name.startsWith(charSequence.toString())) {
                        indices.add(j);
                    }
                }

                adapter.setSearchFilter(indices);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        rv.setAdapter(adapter);

    }
}
