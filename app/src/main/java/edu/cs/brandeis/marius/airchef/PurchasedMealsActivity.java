package edu.cs.brandeis.marius.airchef;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.ui.StormpathLoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PurchasedMealsActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;
    private String API_URL = "http://airchef-server.herokuapp.com/api/mypurchasedmeals/";
    ArrayList<Meal> mealsList = new ArrayList<Meal>();
    MealsAdapter adapter;
    private String userEmail;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_meals);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userEmail = preferences.getString("email", "");
        userName = preferences.getString("name", "");
        Log.d("userinfo", userEmail + " " + userName);

        new GetMeals().execute();

        setupDrawer();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.purchasedMealsSwipeContainer);

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
                        new PrimaryDrawerItem().withName("Explore Meals").withIdentifier(2),
                        new PrimaryDrawerItem().withName("My Meals").withIdentifier(3),
                        new PrimaryDrawerItem().withName("Purchased Meals").withIdentifier(4).withSelectable(false))
                .addStickyDrawerItems(
                        new PrimaryDrawerItem().withName("Logout").withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(PurchasedMealsActivity.this, myProfileActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(PurchasedMealsActivity.this, ExploreMealsActivity.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(PurchasedMealsActivity.this, MyMealsActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
//                                intent = new Intent(PurchasedMealsActivity.this, PurchasedMealsActivity.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                Stormpath.logout();
                                intent = new Intent(PurchasedMealsActivity.this, StormpathLoginActivity.class);
                            }
                            if (intent != null) {
                                PurchasedMealsActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .withSelectedItem(4)
                .build();
    }

    public void onClickViewMeal(View view) {
        Intent intent = new Intent(PurchasedMealsActivity.this, ViewMealActivity.class);
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
            String jsonStr = sh.makeServiceCall(API_URL + userEmail);

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
            ListView mealListing = (ListView) findViewById(R.id.purchasedMealsListView);
            adapter = new MealsAdapter(PurchasedMealsActivity.this, mealsList);
            mealListing.setAdapter(adapter);
        }
    }
}
