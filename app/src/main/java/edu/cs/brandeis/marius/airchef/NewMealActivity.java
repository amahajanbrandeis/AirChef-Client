package edu.cs.brandeis.marius.airchef;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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
    RequestQueue queue;
    EditText mealNameText;
    EditText mealDescriptionText;
    EditText mealLocationText;
    EditText mealPriceText;
    Button submitMealBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        mealNameText = (EditText) findViewById(R.id.mealNameText);
        mealDescriptionText = (EditText) findViewById(R.id.mealDescriptionText);
        mealLocationText = (EditText) findViewById(R.id.mealLocationText);
        mealPriceText = (EditText) findViewById(R.id.mealPriceText);
        submitMealBtn = (Button) findViewById(R.id.submitMealBtn);

        final Context context = this;
        queue = Volley.newRequestQueue(this);
        submitMealBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (formsFilled()) {
                    postMeal();
                } else {

                }
            }
        });
    }

    // Checks if forms are all filled
    private boolean formsFilled() {
        return true;
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

                params.put("title", mealNameText.getText().toString());
                params.put("description", mealDescriptionText.getText().toString());
                params.put("location", mealLocationText.getText().toString());
                params.put("price", mealPriceText.getText().toString());
                params.put("chef", "testChef");

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

//    public class NewMealTask extends AsyncTask<Void, Void, String> {
//
//
//        private Exception exception;
//
//        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        protected String doInBackground(Void... urls) {
////            String email = emailText.getText().toString();
//            // Do some validation here
//            String postResponse;
//            StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL,
//                    new Response.Listener<String>()
//                    {
//                        @Override
//                        public void onResponse(String response) {
//                            // response
//                            postResponse = response;
//                        }
//                    },
//                    new Response.ErrorListener()
//                    {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // error
//                            postResponse = error.toString();
//                        }
//                    }
//            ) {
//                @Override
//                protected Map<String, String> getParams()
//                {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("name", "Alif");
//                    params.put("domain", "http://itsalif.info");
//
//                    return params;
//                }
//            };
//            queue.add(postRequest);
//        }
//
//        protected void onPostExecute(String response) {
//            if(response == null) {
//                response = "THERE WAS AN ERROR";
//            }
//            progressBar.setVisibility(View.GONE);
//            Log.d("Response from API:", response);
//        }
//    }
}
