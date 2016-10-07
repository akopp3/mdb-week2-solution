package com.mdb.android.mdb_week_2_solution;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PokeDetailActivity extends AppCompatActivity {

    ImageView pokePic;
    TextView species, number, attack, hp, defense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_detail);
        Intent intent = getIntent();
        final String pokeName = intent.getStringExtra("Name");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(pokeName);
        setSupportActionBar(toolbar);
        Pokedex pokedex = new Pokedex();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, pokeName); // query contains search string
                startActivity(intent);
            }
        });


        pokePic = (ImageView) findViewById(R.id.pokeProfile);
        species = (TextView) findViewById(R.id.pokeData);
        number = (TextView) findViewById(R.id.pokeNum);
        attack = (TextView) findViewById(R.id.attack);
        hp = (TextView) findViewById(R.id.hp);
        defense = (TextView) findViewById(R.id.defense);

        ArrayList<Pokedex.Pokemon> pokemans = pokedex.getPokemon();

        String url = "https://img.pokemondb.net/artwork/" + pokeName.toLowerCase() + ".jpg";
        Glide.with(getApplicationContext()).load(url).error(R.drawable.placeholder).into(pokePic);

        //Iterating through array to find respective pokemon based on name passed through intent extra
        for (int i = 0; i < pokemans.size(); i++) {
            if (pokemans.get(i).name.equalsIgnoreCase(pokeName)) {
                //Typically, you don't want to concat text in setText method because of Android advises against it, for multilanguage support/translation
                species.setText("Species: " + pokemans.get(i).species);
                number.setText("PokeDex #: " + pokemans.get(i).number);
                attack.setText(pokemans.get(i).attack);
                defense.setText(pokemans.get(i).defense);
                hp.setText(pokemans.get(i).hp);
            }
        }


    }
}
