package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gaurav on 19-Mar-15.
 */
public class CategoriesAdapter extends ArrayAdapter<String>
{
    Context context1;
    int layout;
    ArrayList<String> al;

    public CategoriesAdapter(Context context, int resource, ArrayList<String> objects)
    {
        super(context, resource, objects);
        context1=context;
        layout=resource;
        al=objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        int cat_id,subcat_id=0;
        String cat[];
        DBHelper dbHelper=new DBHelper(getContext().getApplicationContext());
        if(convertView==null)
        {
            LayoutInflater in =(LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=in.inflate(layout,null);
        }
        //ListView listView=(ListView)convertView.findViewById(R.id.category_list);
        final CircularImageView image=(CircularImageView)convertView.findViewById(R.id.list_category_img);
        final TextView name=(TextView)convertView.findViewById(R.id.list_category_name);
        final TextView id=(TextView)convertView.findViewById(R.id.list_category_id);

        String c_id=al.get(position);
        Log.i("c_id", c_id);

        if(c_id.contains("."))
        {
            cat= c_id.split("\\.");
           // Log.i("cat[]", cat[0]);
            cat_id = Integer.parseInt(cat[0]);
            subcat_id = Integer.parseInt(cat[1]);
        }
        else
        {
            cat_id = Integer.parseInt(c_id);
        }
        Cursor c=dbHelper.getCategoryData(cat_id);
        c.moveToFirst();
        int u_id=c.getInt(c.getColumnIndex(DBHelper.CATEGORY_COL_C_UID));
        if(subcat_id<=0)
        {
            String c_name=c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME));
            name.setText(c_name);
            try
            {
                String c_img=c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_ICON));
                byte[] decodedString = Base64.decode(c_img.trim(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                image.setImageBitmap(decodedByte);
            }
            catch (Exception e)
            {
                Log.i("Excep:PersonAdapter",e.getMessage());
            }
            id.setText(c_id);
            c.close();
        }
        else if(subcat_id>0)
        {
            Cursor c1=dbHelper.getSubCategoryData(subcat_id,u_id);
            c1.moveToFirst();
            String c_name=c1.getString(c1.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_NAME));
            name.setText(c_name);
            try
            {
                String c_img=c.getString(c.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_ICON));
                byte[] decodedString = Base64.decode(c_img.trim(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                image.setImageBitmap(decodedByte);
            }
            catch (Exception e)
            {
                Log.i("Excep:PersonAdapter",e.getMessage());
            }
            id.setText(c_id);
            c1.close();
        }

        return convertView;
    }
}
