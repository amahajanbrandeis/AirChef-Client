package edu.cs.brandeis.marius.homecooked;

import java.util.ArrayList;

/**
 * Created by Anirudh on 11/13/2016.
 */

public class Meal {
    private String id;
    private String title;
    private String details;
    private String ingredients;
    private double price;
    private String location;
    private String chef;
    private String dateAdded;

    public Meal(String id, String title, String details, String ingredients, double price, String location, String chef, String dateAdded){
        this.id = id;
        this.title = title;
        this.details = details;
        this.ingredients = ingredients;
        this.price = price;
        this.location = location;
        this.chef = chef;
        this.dateAdded = dateAdded;
    }

    public String getId() { return this.id; }

    public String getTitle() {
        return this.title;
    }

    public String getDetails() { return this.details; }

    public String getIngredients() {
        return this.ingredients;
    }

    public double getPrice(){
        return this.price;
    }

    public String getLocation() {
        return this.location;
    }

    public String getChef() {
        return this.chef;
    }

    public String getDateAdded() { return this.dateAdded; }

    public void setTitle(String val){
        this.title = val;
    }

    public void setIngredients (String val){
        this.ingredients = val;
    }

    public void setPrice (double val){
        this.price = price;
    }

    public void setLocation (String val) {
        this.location = val;
    }
}
