package edu.cs.brandeis.marius.airchef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewMealActivity extends AppCompatActivity {
    static final String API_REQUEST_URL = "http://airchef-server.herokuapp.com/api/mealrequest";
    static final String REQUEST_TAG = "NewMealActivity";
    RequestQueue queue;

    Meal meal;
    boolean requested;
    String JSONString;
    String userName;
    String email;
    TextView viewMealTitle;
    TextView viewMealChef;
    TextView viewMealDateAdded;
    ImageView viewMealImage;
    TextView viewMealDetails;
    TextView viewMealPrice;
    TextView requestsLabel;
    SwipeRefreshLayout mealRequestsSwipeContainer;
    ArrayList<MealRequest> mealRequestsList = new ArrayList<MealRequest>();
    Button viewMealRequestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meal);

        Intent thisIntent = getIntent();
        Bundle extras = thisIntent.getExtras();
        JSONString = extras.getString("JSON");
        Log.d("app:", JSONString);

        meal = new Meal(JSONString);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userName = preferences.getString("name", "");
        email = preferences.getString("email", "");
        requested = preferences.getString(meal.getId(), "").equals("meal");
        Log.d("app:", "Requested: " + requested);

        queue = Volley.newRequestQueue(ViewMealActivity.this);
        setupViews();

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
        try {
            String dateString = meal.getDateAdded();
            Date dateObj = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(dateString.replaceAll("Z$", "+0000"));
            viewMealDateAdded.setText("Added on " + (new SimpleDateFormat("MMM dd hh:mm a")).format(dateObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewMealDetails.setText(meal.getDetails());
        viewMealPrice.setText("$" + meal.getPrice());

        // If current user is the chef, we display the requests for this meal
        if (userName.equals(meal.getChef())) {
            requestsLabel = (TextView) findViewById(R.id.requestsLabel);
            requestsLabel.setVisibility(View.VISIBLE);
            mealRequestsSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.mealRequestsSwipeContainer);

            // Setup refresh listener which triggers new data loading
            mealRequestsSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    fetchMealRequests();
                }
            });

            fetchMealRequests();

            viewMealRequestBtn.setText("End Listing");
            viewMealRequestBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("app:", "End listing");
                    deleteMeal();
                }
            });
        } else {
            // User has already requested this meal
            if (requested) {
                viewMealRequestBtn.setVisibility(View.INVISIBLE);

                // ,,, or has not, so allow them to request
            } else {
                viewMealRequestBtn.setText("Request Meal");
                viewMealRequestBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.d("app:", "Request meal");
                        requestMeal();
                    }
                });
            }
        }
        Log.d("App:", "views should be set");
    }

    private void fetchMealRequests() {
        String fetchMealRequestsURL = API_REQUEST_URL + "?mealid=" + meal.getId() + "&purchased=false";
        Log.d("this meal id:", fetchMealRequestsURL);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, fetchMealRequestsURL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        String jsonStr = response.toString();
                        Log.d("Response", jsonStr);

                        try {
                            JSONObject jsonObj = new JSONObject(jsonStr);
                            JSONArray mealRequests = jsonObj.getJSONArray("mealRequests");

                            mealRequestsList.clear();

                            // looping through all meal requests
                            for (int i = 0; i < mealRequests.length(); i++) {
                                JSONObject JSONMealRequest = mealRequests.getJSONObject(i);
                                MealRequest mealRequest = new MealRequest(JSONMealRequest);
                                mealRequestsList.add(mealRequest);
                            }

                            // Stop refresh graphic
                            mealRequestsSwipeContainer.setRefreshing(false);

                            // Set adapter
                            ListView mealRequestListing = (ListView) findViewById(R.id.mealRequestsListView);
                            MealRequestsAdapter adapter = new MealRequestsAdapter(ViewMealActivity.this, mealRequestsList);
                            mealRequestListing.setAdapter(adapter);
                        }  catch (final Exception e) {
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

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) ;
        queue.add(getRequest);
    }

    private void deleteMeal() {

    }

    private void requestMeal() {
        Log.d("app", "api url is " + API_REQUEST_URL);

        JSONObject JSONRequest = new JSONObject();
        try {
            JSONObject JSONMeal = new JSONObject(JSONString);
            JSONRequest.put("meal", JSONMeal);
            JSONRequest.put("buyerEmail", email);
            JSONRequest.put("buyerName", userName);
        } catch (Exception e) {}

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
                API_REQUEST_URL,
                JSONRequest,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject JSON) {
                        // handle response
                        String response = JSON.toString();
                        Log.d("app:", response);

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ViewMealActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(meal.getId(), "meal");
                        editor.apply();

                        Context context = getApplicationContext();
                        CharSequence text = "Meal requested!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        viewMealRequestBtn.setVisibility(View.INVISIBLE);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error
                        Log.d("app:", "Error: " + error.toString());
                    }
                });
        queue.add(postRequest);
    }

    public void onClickAcceptRequest(View view) {
        String requestID = (String) view.getTag();
        Log.d("request id: ", requestID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(REQUEST_TAG);
        }
    }
}
