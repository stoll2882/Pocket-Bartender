package com.toll.sam.pocketbartender;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DrinkDetailAPI {
    private String apiKey = "9973533";
    static final String TAG = "DrinkDetailAPITag";

    DrinkDetailActivity drinkDetailActivity;

    // Parameters: mainActivity
    // Return: none
    // PlacesAPI EVC
    public DrinkDetailAPI(DrinkDetailActivity drinkDetailActivity) {
        this.drinkDetailActivity = drinkDetailActivity;
    }

    // Parameters: location, keyword
    // Return: none
    // starts async task to fetch nearby places
    public void fetchDrink(String name) throws MalformedURLException {
        String drinksURL = "www.thecocktaildb.com/api/json/v1/1/search.php?s=" + name;

        DrinkDetailAPI.FetchSingleDrinkAsyncTask asyncTask = new DrinkDetailAPI.FetchSingleDrinkAsyncTask();
        asyncTask.execute(drinksURL);
    }

    class FetchSingleDrinkAsyncTask extends AsyncTask<String, Void, Drink> {

        // Parameters: none
        // Return: none
        // shows progress bar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar = drinkDetailActivity.findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.VISIBLE);
        }

        // Parameters: place
        // Return: none
        // removes progress bar
        @Override
        protected void onPostExecute(Drink drink) {
            super.onPostExecute(drink);
            drinkDetailActivity.receivedDrink(drink);
            ProgressBar progressBar = drinkDetailActivity.findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.GONE);
        }

        // Parameters: (var args) strings
        // Return: place
        // fetches place details from json
        @Override
        protected Drink doInBackground(String... strings) {
            String url =  strings[0];
            Drink drink = new Drink();

            try {
                URL urlObject = new URL(url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlObject.openConnection();

                String jsonResult = "";
                InputStream in = httpsURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    jsonResult += (char) data;
                    data = reader.read();
                }

                Log.d(TAG, "DETAILS: " + jsonResult);

                JSONObject jsonObject = new JSONObject(jsonResult);
                String name = jsonObject.getString("strDrink");
                String instructions = jsonObject.getString("strInstructions");
                String alcoholic = jsonObject.getString("strAlcoholic");
                ArrayList<String> ingredients = new ArrayList<>();
                int i = 1;
                while (!jsonObject.getString("strIngredient" + i).equals("null"))
                {
                    ingredients.add(jsonObject.getString("strIngredient" + i));
                }

                drink.setName(name);
                drink.setIngredients(ingredients);
                drink.setInstructions(instructions);
                drink.setAlcoholic(alcoholic);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return drink;
        }
        /*
        // Parameters: singlePlaceObject (json)
        // Return: place
        // parses place info from json object
        private Drink parseDrink(JSONObject singleDrinkObject) {
            Drink drink = null;
            Log.d(TAG, "parseDrink: " + singleDrinkObject);
            try {
                String id = singleDrinkObject.getString("idDrink");
                String name = singleDrinkObject.getString("strDrink");
                String photoLink = singleDrinkObject.getString("strDrinkThumb");

                InputStream in = new java.net.URL(photoLink).openStream();
                Bitmap image = BitmapFactory.decodeStream(in);

                drink = new Drink(id, name, image);
            }
            catch (JSONException | MalformedURLException e) {
                Log.d(TAG, "ERROR parsing places!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return drink;
        }
        */
    }
}
