package edu.cs.brandeis.marius.airchef;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anirudh on 11/13/2016.
 */

public class Meal {
    private String id;
    private String title;
    private String details;
    private String price;
    private String location;
    private String chef;
    private String dateAdded;
    private String JSON;

    public Meal(String id, String title, String details, String price, String location, String chef, String dateAdded, String JSON){
        this.id = id;
        this.title = title;
        this.details = details;
        this.price = price;
        this.location = location;
        this.chef = chef;
        this.dateAdded = dateAdded;
        this.JSON = JSON;
    }

    public Meal(String JSONString) {
        try {
            JSONObject JSONMeal = new JSONObject(JSONString);
            this.id = JSONMeal.getString("_id");
            this.title = JSONMeal.getString("title");
            this.details = JSONMeal.getString("description");
            this.price = JSONMeal.getString("price");
            this.location = JSONMeal.getString("location");
            this.chef = JSONMeal.getString("chef");;
            this.dateAdded = JSONMeal.getString("dateAdded");;
            this.JSON = JSONMeal.toString();
        } catch (final JSONException e) {

        }
    }

    public Meal(JSONObject JSONMeal) {
        try {
            this.id = JSONMeal.getString("_id");
            this.title = JSONMeal.getString("title");
            this.details = JSONMeal.getString("description");
            this.price = JSONMeal.getString("price");
            this.location = JSONMeal.getString("location");
            this.chef = JSONMeal.getString("chef");;
            this.dateAdded = JSONMeal.getString("dateAdded");;
            this.JSON = JSONMeal.toString();
        } catch (final JSONException e) {

        }
    }

    public String getId() { return this.id; }

    public String getTitle() {
        return this.title;
    }

    public String getDetails() { return this.details; }

    public String getPrice(){
        return this.price;
    }

    public String getLocation() {
        return this.location;
    }

    public String getChef() {
        return this.chef;
    }

    public String getDateAdded() { return this.dateAdded; }

    public String getJSON() { return this.JSON; }

    public void setTitle(String val){
        this.title = val;
    }

    public void setPrice (double val){
        this.price = price;
    }

    public void setLocation (String val) {
        this.location = val;
    }
}
