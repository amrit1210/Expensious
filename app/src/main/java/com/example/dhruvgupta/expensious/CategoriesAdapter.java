package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gaurav on 19-Mar-15.
 */
public class CategoriesAdapter extends ArrayAdapter<CategoryDB_Specific>
{
    Context context1;
    int layout;
    ArrayList<CategoryDB_Specific> al;
    public CategoriesAdapter(Context context, int resource, ArrayList<CategoryDB_Specific>objects)
    {
        super(context, resource, objects);
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

        final ImageView image=(ImageView)convertView.findViewById(R.id.list_category_img);
        final TextView name=(TextView)convertView.findViewById(R.id.list_category_name);

        CategoryDB_Specific db=al.get(position);

        name.setText(db.c_name);
        image.setImageResource(R.drawable.user_48);
        return convertView;
    }
}
