package com.toll.sam.pocketbartender;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Drink {

    private String id;
    private String name;
    private ArrayList<String> ingredients = new ArrayList<>();
    private ArrayList<String> measurements = new ArrayList<>();
    private String instructions;
    private String alcoholic;
    private Bitmap imageReference;

    public Drink()
    {
        this.name = "";
        ingredients = null;
    }

    public Drink(String id, String name, Bitmap imageReference) {
        this.id = id;
        this.name = name;
        this.imageReference = imageReference;
    }

    public Drink(String id, String name, ArrayList<String> ingredients, ArrayList<String> measurements, String instructions, String alcoholic, Bitmap imageReference) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.measurements = measurements;
        this.instructions = instructions;
        this.alcoholic = alcoholic;
        this.imageReference = imageReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(String alcoholic) {
        this.alcoholic = alcoholic;
    }

    public Bitmap getImageReference() {
        return imageReference;
    }

    public void setImageReference(Bitmap imageReference) {
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
