package com.mdb.android.mdb_week_2_solution;

/**
 * Created by Abhinav on 9/30/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import static com.mdb.android.mdb_week_2_solution.MainActivity.DEBUG;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.CustomViewHolder> {
    public static final int LIST_MODE = 0;
    public static final int GRID_MODE = 1;

    private Context context;
    private ArrayList<Pokedex.Pokemon> pokemonArray;
    // for filters, store the filters as Integer ArrayLists to avoid duplicating the Pokemon
    // a bunch. that's unethical to Pokemon and a waste of data.
    private ArrayList<Integer> filterIndexes;
    private ArrayList<Integer> lowHpIndexes;
    private ArrayList<Integer> medHpIndexes;
    private ArrayList<Integer> highHpIndexes;

    private int mode;

    public PokemonAdapter(Context context, ArrayList<Pokedex.Pokemon> pokemons, int mode) {
        this.context = context;
        this.pokemonArray = pokemons;
        this.mode = mode;
        filterIndexes = new ArrayList<>();
        lowHpIndexes = new ArrayList<>();
        medHpIndexes = new ArrayList<>();
        highHpIndexes = new ArrayList<>();
        // categorize the pokemon by HP at the start
        // by default, all pokemon will be shown regardless of filter buttons
        for (int i = 0; i < pokemons.size(); i++) {
            filterIndexes.add(i);
            if (Integer.parseInt(pokemons.get(i).hp) < 50) {
                lowHpIndexes.add(i);
            } else if (Integer.parseInt(pokemons.get(i).hp) <= 100) {
                medHpIndexes.add(i);
            } else {
                highHpIndexes.add(i);
            }
        }
        Log.i("FilterIndexSize", filterIndexes.size() + "");
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int cardLayoutId;
        switch (mode) {
            // load the card layout based on RecyclerView mode, since a list card would be ugly
            // in grid mode and vice versa
            case LIST_MODE:
                cardLayoutId = R.layout.pokemon_list_card;
                break;
            case GRID_MODE:
                cardLayoutId = R.layout.pokemon_grid_card;
                break;
            default:
                // intentionally crash if the mode is invalid
                cardLayoutId = -1;
                break;
        }
        return new CustomViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(cardLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Pokedex.Pokemon pokemon = pokemonArray.get(filterIndexes.get(position));
        String url = "https://img.pokemondb.net/artwork/" + pokemon.name.toLowerCase() + ".jpg";
        if (DEBUG) {
            Log.d("pika", "Accessing image at " + url);
        }
        // set the Pokemon's info to the right fields
        holder.nameTextView.setText(pokemon.name);
        // the magic of Glide. that was easy
        Glide.with(context)
                .load(url)
                // if the image can't be retrieved, substitute it with a pikachu. pika pii~
                .error(R.drawable.placeholder)
                .into(holder.imageView);
        if (DEBUG) {
            Log.d("pika", "Loaded card for " + pokemon.name);
        }
    }

    @Override
    public int getItemCount() {
        return filterIndexes.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;

        public CustomViewHolder(View view) {
            super(view);
            this.nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PokeDetailActivity.class);
                    intent.putExtra("Name", nameTextView.getText());
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setMode(int mode) {
        this.mode = mode;
    }


    public void setSearchFilter(ArrayList<Integer> search) {
        filterIndexes.clear();
        filterIndexes.addAll(search);
        Collections.sort(filterIndexes);
    }

    public void changeFilters(boolean lowHp, boolean midHp, boolean highHp) {
        filterIndexes.clear();
        if (lowHp) {
            filterIndexes.addAll(lowHpIndexes);
        }
        if (midHp) {
            filterIndexes.addAll(medHpIndexes);
        }
        if (highHp) {
            filterIndexes.addAll(highHpIndexes);
        }
        // sort them by their original ordering so the order doesn't get fucked by filters
        Collections.sort(filterIndexes);
    }
}
