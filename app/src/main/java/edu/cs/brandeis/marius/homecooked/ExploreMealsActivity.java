package edu.cs.brandeis.marius.homecooked;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ExploreMealsActivity extends AppCompatActivity {

    ArrayList<Meal> expenseList = new ArrayList<Meal>();
    MealsAdapter adapter = new MealsAdapter(this, expenseList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_listings);
        ListView mealListing = (ListView) findViewById(R.id.mealsListView);
        mealListing.setAdapter(adapter);

        final Button newMealBtn = (Button) findViewById(R.id.newMealBtn);
        final Context context = this;
        // Save button functionality
        newMealBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(context, NewMealActivity.class));
            }
        });
    }
}
