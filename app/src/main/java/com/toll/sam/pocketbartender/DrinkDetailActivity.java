package com.toll.sam.pocketbartender;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class DrinkDetailActivity extends AppCompatActivity {

    Drink drink;

    TextView nameTextView;
    ArrayList<String> ingredientMeasureList;
    ListView ingredientsListView;
    TextView instructionsTextView;
    ImageView imageView;
    TextView alcoholicTextView;
    DrinkDetailsAPI drinkDetailsAPI;

    private ArrayAdapter<String> ingredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        drink = new Drink();

        // get and set view attributes
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameTextView = findViewById(R.id.nameTextView);
        alcoholicTextView = findViewById(R.id.alcoholicView);
        ingredientsListView = findViewById(R.id.ingredientsList);
        imageView = findViewById(R.id.imageView);
        instructionsTextView = findViewById(R.id.instructionsTextView);
        ingredientMeasureList = new ArrayList<>();

        ingredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientMeasureList);
        ingredientsListView.setAdapter(ingredientsAdapter);

        // get intent MainActivity provides on screen switch
        Intent intent = getIntent();
        if (intent != null) {
            // get place object from intent values
            String drinkId = intent.getStringExtra("id");

            try {
                getDrink(drinkId);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    public void getDrink(String id) throws MalformedURLException {
        drinkDetailsAPI = new DrinkDetailsAPI(this);
        drinkDetailsAPI.fetchDrink(id);
    }

    public void receivedDrink(Drink drink) {
        this.drink = drink;

        nameTextView.setText(drink.getName());
        instructionsTextView.setText(drink.getInstructions());
        imageView.setImageBitmap(drink.getImageReference());
        alcoholicTextView.setText(drink.getAlcoholic());
        ArrayList<String> ingredientList = drink.getIngredients();
        ArrayList<String> measureList = drink.getMeasurements();

        if (measureList.size() == ingredientList.size()) {
            for (int i = 0; i < ingredientList.size(); i++) {
                String combined = measureList.get(i) + " " + ingredientList.get(i);
                ingredientMeasureList.add(combined);
            }
        } else {
            for (int i = 0; i < ingredientList.size(); i++) {
                ingredientMeasureList.add(ingredientList.get(i));
            }
        }
        ingredientsAdapter.notifyDataSetChanged();
    }
}