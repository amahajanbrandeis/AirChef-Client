package edu.cs.brandeis.marius.homecooked;

/**
 * Created by Anirudh on 11/13/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryan on 11/13/2016.
 */
public class MealsAdapter extends BaseAdapter {
    private ArrayList<Meal> list;
    Context context;

    public MealsAdapter(Context context, ArrayList<Meal> list){
        super();
        this.list = list;
        this.context = context;

        ArrayList<String> a = new ArrayList<String>();
//        list.add(new Meal("Eggs", a, 8.05, "Yemen", "Frank Eggheart", 5/5));
    }

    public int getCount() {
        return this.list.size();
    }

    public Context getContext(){
        return this.context;
    }

    public Meal getItem(int index){
        return this.list.get(index);
    }

    public long getItemId(int index){
        return index; // why?
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.meal_entry, null);
        }

        TextView mealName = (TextView) view.findViewById(R.id.entryMealNameTextView);
        TextView mealUsername = (TextView) view.findViewById(R.id.entryUserTextView);
        TextView mealTime = (TextView) view.findViewById(R.id.entryTimeTextView);
        TextView mealLocation = (TextView) view.findViewById(R.id.entryLocationTextView);
        TextView mealPrice = (TextView) view.findViewById(R.id.entryPriceTextView);

        Meal entry = getItem(position);
        mealName.setText(entry.getTitle());
        mealUsername.setText(entry.getChef());
        mealTime.setText("TODO");
        mealLocation.setText(entry.getLocation());
        mealPrice.setText(Double.toString(entry.getPrice()));

        return view;
    }
}

