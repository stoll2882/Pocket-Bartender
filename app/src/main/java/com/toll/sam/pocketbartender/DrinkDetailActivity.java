package com.toll.sam.pocketbartender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.List;

public class DrinkDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        Intent dataIn = getIntent();
        String name = dataIn.getStringExtra("name");
        DrinkDetailAPI drinkDetailAPI = new DrinkDetailAPI(this);
        try {
            drinkDetailAPI.fetchDrink(name);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void receivedDrink(Drink drink) {
        TextView title = findViewById(R.id.drinkTitleView);
        TextView instructions = findViewById(R.id.instrucitonsView);
        //do list view things here

        title.setText(drink.getName() + " (" + drink.getAlcoholic() + ")");
        instructions.setText(drink.getInstructions());

    }
}