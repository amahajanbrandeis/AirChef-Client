package edu.cs.brandeis.marius.airchef;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExploreMealsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    ArrayList<Meal> mealsList = new ArrayList<Meal>();
    static final String API_URL = "http://airchef-server.herokuapp.com/api/meal/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_listings);
        new GetContacts().execute();

        final Button newMealBtn = (Button) findViewById(R.id.newMealBtn);
        final Context context = this;
        // Save button functionality
        newMealBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, NewMealActivity.class);
                startActivity(intent);
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new GetContacts().execute();
            }
        });
    }

    public void onClickViewMeal(View view) {
        Intent intent = new Intent(ExploreMealsActivity.this, ViewMealActivity.class);
        intent.putExtra("JSON", (String) view.getTag());

        String extra = intent.getExtras().getString("JSON");
        Log.d("extra: ", extra);
        startActivity(intent);
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(API_URL);

            Log.d("API:", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray meals = jsonObj.getJSONArray("meals");

                    mealsList.clear();

                    // looping through all meals
                    for (int i = 0; i < meals.length(); i++) {
                        JSONObject JSONMeal = meals.getJSONObject(i);
                        Meal meal = new Meal(JSONMeal);
//                        String id = mealJSON.getString("_id");
//                        String title = mealJSON.getString("title");
//                        String details = mealJSON.getString("description");
//                        String chef = mealJSON.getString("chef");
//                        String dateAdded = mealJSON.getString("dateAdded");

//                        Meal meal = new Meal(id, title, details, "ingredients", 3.00, "mods", chef, dateAdded, jsonStr);

                        mealsList.add(meal);
                    }
                } catch (final JSONException e) {
                    Log.d("Client", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.d("", "Couldn't get json from server.");

            }

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Stop refresh graphic
            swipeContainer.setRefreshing(false);

            // Set adapter
            ListView mealListing = (ListView) findViewById(R.id.mealsListView);
            MealsAdapter adapter = new MealsAdapter(ExploreMealsActivity.this, mealsList);
            mealListing.setAdapter(adapter);
        }
    }
}
