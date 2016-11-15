package edu.cs.brandeis.marius.homecooked;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class NewMealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        final Button submitMealBtn = (Button) findViewById(R.id.submitMealBtn);
        final Context context = this;

        submitMealBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Log", "submitting new meal now");
            }
        });
    }
}
