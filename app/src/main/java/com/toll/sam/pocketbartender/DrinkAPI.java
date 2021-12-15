package com.toll.sam.pocketbartender;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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

public class DrinkAPI {
    private String apiKey = "9973533";
    static final String TAG = "DrinkAPITag";

    FindDrinksFragment findDrinksFragment;

    // Parameters: mainActivity
    // Return: none
    // PlacesAPI EVC
    public DrinkAPI(FindDrinksFragment findDrinksFragment) {
        this.findDrinksFragment = findDrinksFragment;
    }

    // Parameters: location, keyword
    // Return: none
    // starts async task to fetch nearby places
    public void fetchDrinks(List<String> ingredients) throws MalformedURLException {
        String drinksURL = "https://www.thecocktaildb.com/api/json/v2/9973533/filter.php?i=" + ingredients.get(0);
        for (int i = 1; i < ingredients.size(); i++) {
            drinksURL += "," + ingredients.get(i);
        }

        FetchDrinksAsyncTask asyncTask = new FetchDrinksAsyncTask();
        asyncTask.execute(drinksURL);
    }

    class FetchDrinksAsyncTask extends AsyncTask<String, Void, List<Drink>> {

        // Parameters: none
        // Return: none
        // shows progress bar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar = findDrinksFragment.getActivity().findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        // Parameters: place
        // Return: none
        // removes progress bar
        @Override
        protected void onPostExecute(List<Drink> drinks) {
            super.onPostExecute(drinks);
            findDrinksFragment.receivedDrinks(drinks);
            ProgressBar progressBar = findDrinksFragment.getActivity().findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }

        // Parameters: (var args) strings
        // Return: place
        // fetches place details from json
        @Override
        protected List<Drink> doInBackground(String... strings) {
            String url =  strings[0];
            List<Drink> drinks = new ArrayList<>();

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
                JSONArray drinksArray = jsonObject.getJSONArray("drinks");

                for (int i = 0; i < drinksArray.length(); i++) {
                    JSONObject newDrinkObject = drinksArray.getJSONObject(i);
                    Drink newDrink = parseDrink(newDrinkObject);
                    drinks.add(newDrink);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return drinks;
        }

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

    }
}
