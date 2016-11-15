package edu.cs.brandeis.marius.homecooked;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

public class ViewMealActivity extends AppCompatActivity {
    Meal meal;
    TextView viewMealTitle;
    TextView viewMealChef;
    TextView viewMealDateAdded;
    ImageView viewMealImage;
    TextView viewMealDetails;
    TextView viewMealPrice;
    Button viewMealRequestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meal);

        String JSONString = getIntent().getExtras().getString("JSON");

        try {
            meal = new Meal(JSONString);
            setupViews();

        } catch (Exception e) {
            // Display error view here
        }
    }

    private void setupViews() {
        viewMealTitle = (TextView) findViewById(R.id.viewMealTitle);
        viewMealChef = (TextView) findViewById(R.id.viewMealChef);
        viewMealDateAdded = (TextView) findViewById(R.id.viewMealDateAdded);
        viewMealImage = (ImageView) findViewById(R.id.viewMealImage);
        viewMealDetails = (TextView) findViewById(R.id.viewMealDetails);
        viewMealPrice = (TextView) findViewById(R.id.viewMealPrice);
        viewMealRequestBtn = (Button) findViewById(R.id.viewMealRequestBtn);

        viewMealTitle.setText(meal.getTitle());
        viewMealChef.setText("Cooked by " + meal.getChef());
        viewMealDateAdded.setText("Added on " + meal.getDateAdded());
        // viewMealImage =
        viewMealDetails.setText(meal.getDetails());
        viewMealPrice.setText("$" + meal.getPrice());
    }
}
