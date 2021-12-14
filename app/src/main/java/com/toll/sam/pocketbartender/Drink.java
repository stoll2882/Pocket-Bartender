package com.toll.sam.pocketbartender;

import java.util.ArrayList;

public class Drink {

    private String name;
    private ArrayList<String> ingredients = new ArrayList<>();
    private String instructions;
    private String alcoholic;
    private String imageReference;

    public Drink()
    {
        this.name = "";
        ingredients = null;
    }

    public Drink(String name)
    {
        this.name = name;
        this.ingredients = null;
    }

    public Drink(String name, ArrayList<String> ingredients) {
        this.ingredients = ingredients;
        this.name = name;
    }

    public Drink(String name, ArrayList<String> ingredients, String instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Drink(String name, ArrayList<String> ingredients, String instructions, String alcoholic) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.alcoholic = alcoholic;
    }

    public Drink(String name, ArrayList<String> ingredients, String instructions, String alcoholic, String imageReference) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.alcoholic = alcoholic;
        this.imageReference = imageReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
