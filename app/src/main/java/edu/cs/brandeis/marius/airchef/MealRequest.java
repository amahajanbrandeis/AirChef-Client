package edu.cs.brandeis.marius.airchef;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marius on 11/30/16.
 */
public class MealRequest {
    private String id;
    private String buyerEmail;
    private String buyerName;
    private String dateAdded;

    public MealRequest(String JSONString) {
        try {
            JSONObject JSONMealRequest = new JSONObject(JSONString);
            this.id = JSONMealRequest.getString("_id");
            this.buyerEmail = JSONMealRequest.getString("buyerEmail");
            this.buyerName = JSONMealRequest.getString("buyerName");
            this.dateAdded = JSONMealRequest.getString("dateAdded");
        } catch (final JSONException e) {

        }
    }

    public MealRequest(JSONObject JSONMealRequest) {
        try {
            this.id = JSONMealRequest.getString("_id");
            this.buyerEmail = JSONMealRequest.getString("buyerEmail");
            this.buyerName = JSONMealRequest.getString("buyerName");
            this.dateAdded = JSONMealRequest.getString("dateAdded");
        } catch (final JSONException e) {

        }
    }

    public String getId() {
        return id;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
