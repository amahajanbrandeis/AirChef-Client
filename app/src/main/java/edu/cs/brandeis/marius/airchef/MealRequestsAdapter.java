package edu.cs.brandeis.marius.airchef;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
        date.setText(entry.getDateAdded());

        return view;
    }


}

