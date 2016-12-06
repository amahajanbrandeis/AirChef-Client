package edu.cs.brandeis.marius.airchef;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MealRequestsAdapter extends BaseAdapter {
    private ArrayList<MealRequest> list;
    Context context;

    public MealRequestsAdapter(Context context, ArrayList<MealRequest> list){
        super();
        this.list = list;
        this.context = context;
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
        Log.d("infl request id", entry.getId());
        view.setTag(entry.getId());

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

