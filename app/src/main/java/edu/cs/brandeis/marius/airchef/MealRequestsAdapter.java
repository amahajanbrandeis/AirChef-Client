package edu.cs.brandeis.marius.airchef;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MealRequestsAdapter extends BaseAdapter {
    private ArrayList<MealRequest> list;
    Context context;
    private final String API_POST_REQUEST_URL = "http://airchef-server.herokuapp.com/api/mealrequest/";
    RequestQueue queue;


    public MealRequestsAdapter(Context context, ArrayList<MealRequest> list){
        super();
        this.list = list;
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public int getCount() {
        return this.list.size();
    }

    public Context getContext(){
        return this.context;
    }

    public MealRequest getItem(int index){
        return this.list.get(index);
    }

    public long getItemId(int index){ return index; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.mealrequest_entry, null);
        }

        TextView buyerName = (TextView) view.findViewById(R.id.requestEntryBuyerName);
        TextView date = (TextView) view.findViewById(R.id.requestEntryDate);

        MealRequest entry = getItem(position);
        buyerName.setText(entry.getBuyerName());
        view.setTag(entry.getId());
        final String requestId = entry.getId();
        Button acceptRequestBtn = (Button) view.findViewById(R.id.acceptRequestBtn);

        Log.d("querying", API_POST_REQUEST_URL + requestId);
        acceptRequestBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                JsonObjectRequest acceptRequest = new JsonObjectRequest(Request.Method.POST,
                        API_POST_REQUEST_URL + requestId,
                        new JSONObject(),
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject JSON) {
                                // handle response
                                String response = JSON.toString();

                                Log.d("response:", response);

                                CharSequence text = "Meal request accepted";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                list.remove(position);
                                notifyDataSetChanged();
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
                queue.add(acceptRequest);
//                //or some other task
            }
        });

        try {
            String dateString = entry.getDateAdded();
            Date dateObj = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(dateString.replaceAll("Z$", "+0000"));
            date.setText((new SimpleDateFormat("MMM dd hh:mm a")).format(dateObj));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


}

