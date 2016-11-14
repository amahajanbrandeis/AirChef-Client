package edu.cs.brandeis.marius.homecooked;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Meal> expenseList = new ArrayList<Meal>();
    MealsAdapter adapter = new MealsAdapter(this, expenseList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_listings);
        ListView mealListing = (ListView) findViewById(R.id.mealsListView);
        mealListing.setAdapter(adapter);
    }
}
