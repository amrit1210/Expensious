package com.example.dhruvgupta.expensious;

/**
 * Created by dhruvgupta on 3/25/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrencyAdapter extends ArrayAdapter<CurrencyDB>
{
    Context context1;
    int layout;
    ArrayList<CurrencyDB> al;

    public CurrencyAdapter(Context context, int resource,ArrayList<CurrencyDB>objects)
    {
        super(context, resource,objects);
        context1=context;
        layout=resource;
        al=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            LayoutInflater in =(LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=in.inflate(layout,null);
        }
        final TextView name=(TextView)convertView.findViewById(R.id.list_currency_name);
        final TextView code=(TextView)convertView.findViewById(R.id.list_currency_code);
        final TextView symbol=(TextView)convertView.findViewById(R.id.list_currency_symbol);

        CurrencyDB db=al.get(position);

        name.setText(db.c_name);
        code.setText(db.c_code);
        symbol.setText(db.c_symbol);

        return convertView;
    }
}

