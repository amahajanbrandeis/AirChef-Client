package edu.cs.brandeis.marius.homecooked;

import java.util.ArrayList;

/**
 * Created by Anirudh on 11/13/2016.
 */

public class Meal {
    private String name;
    private ArrayList<String> ingredients;
    private double price;
    private String location;
    private String userName;
    private int userScore;

    public Meal(String name, ArrayList<String> ingredients, double price, String location, String userName, int userScore){
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.location = location;
        this.userName = userName;
        this.userScore = userScore;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getIngredients() {
        return this.ingredients;
    }

    public double getPrice(){
        return this.price;
    }

    public String getLocation() {
        return this.location;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getUserScore() {
        return this.userScore;
    }

    public void setName(String val){
        this.name = val;
    }

    public void setIngredients (ArrayList<String> val){
        this.ingredients = val;
    }

    public void setPrice (double val){
        this.price = price;
    }

    public void setLocation (String val) {
        this.location = val;
    }
}
