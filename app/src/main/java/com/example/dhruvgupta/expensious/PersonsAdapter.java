package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

/**
 * Created by Amrit on 3/19/2015.
 */
public class PersonsAdapter extends ArrayAdapter<PersonDB>
{
    Context context1;
    int layout;
    ArrayList<PersonDB> al;

    public PersonsAdapter(Context context, int resource, ArrayList<PersonDB> objects)
    {
        super(context, resource, objects);
        context1=context;
        layout=resource;
        al=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater in = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = in.inflate(layout, null);
        }

        final CircularImageView mImageView=(CircularImageView)convertView.findViewById(R.id.list_person_color);
        final TextView  mPersonName=(TextView)convertView.findViewById(R.id.list_person_name);

        PersonDB db=al.get(position);
        Log.i("Position",position+"");

        try {
            byte[] decodedString = Base64.decode(db.p_color.trim(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            mImageView.setImageBitmap(decodedByte);
            mPersonName.setText(db.p_name);
        }
        catch (Exception e)
        {
            Log.i("Excep:PersonAdapter",e.getMessage());
        }
        return convertView;
    }
}
