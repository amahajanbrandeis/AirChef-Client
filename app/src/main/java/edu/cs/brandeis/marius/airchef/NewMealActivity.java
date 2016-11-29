package edu.cs.brandeis.marius.airchef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stormpath.sdk.Stormpath;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NewMealActivity extends AppCompatActivity {

    static final String API_URL = "http://airchef-server.herokuapp.com/api/meal/";
    static final String REQUEST_TAG = "NewMealActivity";
    String userEmail;
    String userName;
    RequestQueue queue;
    EditText mealNameText;
    EditText mealDescriptionText;
    EditText mealLocationText;
    EditText mealPriceText;
    TextView errorText;
    Button submitMealBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        mealNameText = (EditText) findViewById(R.id.mealNameText);
        mealDescriptionText = (EditText) findViewById(R.id.mealDescriptionText);
        mealLocationText = (EditText) findViewById(R.id.mealLocationText);
        mealPriceText = (EditText) findViewById(R.id.mealPriceText);
        errorText = (TextView) findViewById(R.id.errorText);
        submitMealBtn = (Button) findViewById(R.id.submitMealBtn);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userEmail = preferences.getString("email", "");
        userName = preferences.getString("name", "");

        final Context context = this;
        queue = Volley.newRequestQueue(this);
        submitMealBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (formsFilled()) {
                    postMeal();
                } else {
                    errorText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // Checks if forms are all filled
    private boolean formsFilled() {
        return !((mealNameText.getText().toString().trim().length() == 0) ||
                (mealDescriptionText.getText().toString().trim().length() == 0) ||
                (mealLocationText.getText().toString().trim().length() == 0) ||
                (mealPriceText.getText().toString().trim().length() == 0));
    }

    private void postMeal() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // handle response
                        Log.d("app:", response);
                        Intent intent = new Intent(NewMealActivity.this, ExploreMealsActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error
                        Log.d("app:", "Error: " + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("title", mealNameText.getText().toString().trim());
                params.put("description", mealDescriptionText.getText().toString().trim());
                params.put("location", mealLocationText.getText().toString().trim());
                params.put("price", mealPriceText.getText().toString().trim());
                params.put("chef", userName);

                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(REQUEST_TAG);
        }
    }
}
