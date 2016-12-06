package edu.cs.brandeis.marius.airchef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.ui.StormpathLoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FilterInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
public class ExploreMealsActivity extends Activity {

    private SwipeRefreshLayout swipeContainer;
    ArrayList<Meal> mealsList = new ArrayList<Meal>();
    ArrayList<Meal> tempArr;
    static final String API_URL = "http://airchef-server.herokuapp.com/api/meal/";
    MealsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_listings);
        new GetMeals().execute();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setupDrawer();

        final Button newMealBtn = (Button) findViewById(R.id.newMealBtn);
        final Context context = this;

        newMealBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, NewMealActivity.class);
                startActivity(intent);
            }
        });

        tempArr = mealsList;
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
                    tempArr = new ArrayList<Meal>();
                    for (int i = 0; i < mealsList.size(); i++){
                        if (mealsList.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())){
                            tempArr.add(mealsList.get(i));
                        }
                    }
                    ListView mealListing = (ListView) findViewById(R.id.mealsListView);
                    MealsAdapter adapter = new MealsAdapter(ExploreMealsActivity.this, tempArr);
                    mealListing.setAdapter(adapter);
                } else {
                    tempArr = mealsList;
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

//                if (tempArr.isEmpty()) {
//                    tempArr = mealsList;
//                }

                switch(filter){
                    case("A-Z"): {
                        Log.d("test", "a-zzzzz");
                        Collections.sort(tempArr, new Comparator<Meal>() {
                            @Override
                            public int compare(Meal o1, Meal o2) {
                                return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
                            }
                        });
                        break;
                    }
                    case("Z-A"): {
                        Log.d("test", "Z-a");
                        Collections.sort(tempArr, new Comparator<Meal>() {
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
                        Collections.sort(tempArr, new Comparator<Meal>() {
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
                        Collections.sort(tempArr, new Comparator<Meal>() {
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
                ListView mealListing = (ListView) findViewById(R.id.mealsListView);
                MealsAdapter adapter = new MealsAdapter(ExploreMealsActivity.this, tempArr);
                mealListing.setAdapter(adapter);

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

    private void setupDrawer() {
        new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("My Profile").withIdentifier(1),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Explore Meals").withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName("My Meals").withIdentifier(3),
                        new PrimaryDrawerItem().withName("Purchased Meals").withIdentifier(4))
                .addStickyDrawerItems(
                        new PrimaryDrawerItem().withName("Logout").withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(ExploreMealsActivity.this, myProfileActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
//                                intent = new Intent(ExploreMealsActivity.this, ActionBarActivity.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(ExploreMealsActivity.this, MyMealsActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(ExploreMealsActivity.this, PurchasedMealsActivity.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                Stormpath.logout();
                                intent = new Intent(ExploreMealsActivity.this, StormpathLoginActivity.class);
                            }
                            if (intent != null) {
                                ExploreMealsActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .withSelectedItem(2)
                .build();
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
            adapter = new MealsAdapter(ExploreMealsActivity.this, tempArr);
            mealListing.setAdapter(adapter);
        }
    }
}
