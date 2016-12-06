package edu.cs.brandeis.marius.airchef;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FilterInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class ExploreMealsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    ArrayList<Meal> mealsList = new ArrayList<Meal>();
    static final String API_URL = "http://airchef-server.herokuapp.com/api/meal/";
    MealsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_listings);
        new GetMeals().execute();

        final Button newMealBtn = (Button) findViewById(R.id.newMealBtn);
        final Context context = this;
        // Save button functionality
        newMealBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, NewMealActivity.class);
                startActivity(intent);
            }
        });

        final Button myProfileBtn = (Button) findViewById(R.id.myProfileBtn);
        myProfileBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, myProfileActivity.class);
                startActivity(intent);
            }
        });

        final EditText searchBar = (EditText) findViewById(R.id.searchMealsEditText);
        searchBar.setText("");
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("") && s != null){
                    // filter list according to charsequence s
                    ArrayList<Meal> tempArr = new ArrayList<Meal>();
                    for (int i = 0; i < mealsList.size(); i++){
                        if (mealsList.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())){
                            tempArr.add(mealsList.get(i));
                        }
                    }
                    ListView mealListing = (ListView) findViewById(R.id.mealsListView);
                    MealsAdapter adapter = new MealsAdapter(ExploreMealsActivity.this, tempArr);
                    mealListing.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Spinner filterSpinner = (Spinner) findViewById(R.id.filterSpinner);
        String[] filters = new String[] {"A-Z", "Z-A", "Price: Low to High", "Price: High to Low"};
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filters);
        filterSpinner.setAdapter(filterAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter =  filterSpinner.getSelectedItem().toString();

                switch(filter){
                    case("A-Z"): {
                        Log.d("test", "a-zzzzz");
                        Collections.sort(mealsList, new Comparator<Meal>() {
                            @Override
                            public int compare(Meal o1, Meal o2) {
                                return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
                            }
                        });
                        break;
                    }
                    case("Z-A"): {
                        Log.d("test", "Z-a");
                        Collections.sort(mealsList, new Comparator<Meal>() {
                            @Override
                            public int compare(Meal o1, Meal o2) {
                                int val = o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
                                if (val < 0) return 1;
                                else if (val > 0) return -1;
                                else return 0;
                            }
                        });
                        break;
                    }
                    case("Price: Low to High"): {
                        Log.d("test", "l-h");
                        Collections.sort(mealsList, new Comparator<Meal>() {
                            @Override
                            public int compare(Meal o1, Meal o2) {
                                if (Float.parseFloat(o1.getPrice()) > Float.parseFloat(o2.getPrice())) return 1;
                                else if ((Float.parseFloat(o1.getPrice()) < Float.parseFloat(o2.getPrice()))) return -1;
                                else return 0;
                            }
                        });
                        break;
                    }
                    case("Price: High to Low"): {
                        Log.d("test", "h-l");
                        Collections.sort(mealsList, new Comparator<Meal>() {
                            @Override
                            public int compare(Meal o1, Meal o2) {
                                if (Float.parseFloat(o1.getPrice()) > Float.parseFloat(o2.getPrice())) return -1;
                                else if ((Float.parseFloat(o1.getPrice()) < Float.parseFloat(o2.getPrice()))) return 1;
                                else return 0;
                            }
                        });
                        break;
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new GetMeals().execute();
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

    private class GetMeals extends AsyncTask<Void, Void, Void> {

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
            adapter = new MealsAdapter(ExploreMealsActivity.this, mealsList);
            mealListing.setAdapter(adapter);
        }
    }
}
