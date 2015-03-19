package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amrit on 3/19/2015.
 */
public class PersonAdapter extends ArrayAdapter<PersonDB> {
    Context context1;
    int layout;
    ArrayList<PersonDB>al;
    public PersonAdapter(Context context, int resource, ArrayList<PersonDB>objects) {
        super(context, resource, objects);
        context1=context;
        layout=resource;
        al=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater in=(LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=in.inflate(layout,null);
        final ImageView mimageView=(ImageView)convertView.findViewById(R.id.list_person_color);
        final TextView  mpersonName=(TextView)convertView.findViewById(R.id.list_person_name);
        PersonDB db=al.get(position);
      //  mimageView.setBackground(personDB.p_color.toString());
        mpersonName.setText(db.p_name);
        return convertView;
    }
}
