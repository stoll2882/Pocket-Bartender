package com.toll.sam.pocketbartender;

import java.util.ArrayList;

public class Drink {

    private ArrayList<String> ingredients = new ArrayList<>();

    public Drink() { }

    public Drink(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(String ingredient)
    {
        ingredients.add(ingredient);
    }

    public void removeIngredient(String ingredient)
    {
        ingredients.remove(ingredients.indexOf(ingredient));
    }

    public void removeIngredient(int index)
    {
        ingredients.remove(index);
    }

    public String getIngredient(String ingredient)
    {
        return ingredients.get(ingredients.indexOf(ingredient));
    }

    public String getIngredient(int index)
    {
        return ingredients.get(index);
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
