package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dhruvgupta on 4/7/2015.
 */
public class RecursiveAdapter extends ArrayAdapter {
    Context context1;
    int layout;
    String curCode;
    DBHelper dbHelper;
    SharedPreferences sp;
    ArrayList<CurrencyDB> al1;
    ArrayList<RecursiveDB> al;
//    String etype=null;

    public RecursiveAdapter(Context context, int resource,  ArrayList<RecursiveDB> al1) {
        super(context, resource, al1);
        context1=context;
        layout=resource;
        al=al1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater in=(LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= in.inflate(layout,null);
        }

        final CircularImageView image = (CircularImageView) convertView.findViewById(R.id.list_recursive_img);
        final TextView s_date=(TextView)convertView.findViewById(R.id.list_recursive_start_date);
        final TextView e_date=(TextView)convertView.findViewById(R.id.list_recursive_end_date);
        final TextView category=(TextView)convertView.findViewById(R.id.list_recursive_cat);
        final TextView note=(TextView)convertView.findViewById(R.id.list_recursive_note);
        final TextView currency=(TextView)convertView.findViewById(R.id.list_recursive_cur);
        final TextView account=(TextView)convertView.findViewById(R.id.list_recurive_acc);
        final TextView amount=(TextView)convertView.findViewById(R.id.list_recursive_amt);

        dbHelper = new DBHelper(getContext());
        sp = getContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

        RecursiveDB trans_db=al.get(position);
        s_date.setText(trans_db.rec_start_date);
        int c_id= trans_db.rec_c_id;
        int sub_id= trans_db.rec_sub_id;
        if(c_id!=0)
        {
            Cursor c4=dbHelper.getCategoryData(c_id,sp.getInt("UID",0));
            c4.moveToFirst();
            String cat=c4.getString(c4.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME));
            Log.i("cat:recursive", cat);
            try
            {
                String c_img=c4.getString(c4.getColumnIndex(DBHelper.CATEGORY_COL_C_ICON));
                byte[] decodedString = Base64.decode(c_img.trim(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                image.setImageBitmap(decodedByte);

                Cursor c5 = dbHelper.getPersonData(trans_db.rec_p_id, sp.getInt("UID", 0));
                c5.moveToFirst();

                String colorCode = c5.getString(c5.getColumnIndex(DBHelper.PERSON_COL_COLOR_CODE));
                image.setBorderColor(Color.parseColor(colorCode));
            }
            catch (Exception e)
            {
                Log.i("Excep:PersonAdapter", e.getMessage());
            }
            c4.close();
            if(sub_id!=0)
            {
                Cursor c5=dbHelper.getSubCategoryData(sub_id,sp.getInt("UID",0));
                c5.moveToFirst();
                String subcat=c5.getString(c5.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_NAME));
                c5.close();
                category.setText(cat+" : "+subcat);

            }
            else
            {
                category.setText(cat);
            }

        }

        note.setText(trans_db.rec_note);
        e_date.setText(trans_db.rec_end_date);
        amount.setText(trans_db.rec_balance+"");

        al1 = new ListOfCurrencies().getAllCurrencies();

        Cursor c2 = dbHelper.getSettingsData(sp.getInt("UID", 0));
        c2.moveToFirst();
        curCode = c2.getString(c2.getColumnIndex(DBHelper.SETTINGS_COL_CUR_CODE));
        c2.close();

        Iterator<CurrencyDB> iterator = al1.iterator();
        while (iterator.hasNext())
        {
            CurrencyDB curDB = iterator.next();
            if (curDB.c_code.equals(curCode))
                currency.setText(curDB.c_symbol);
        }

        if(trans_db.rec_type.equals("Expense"))
        {
            String from_acc=null;
            if(trans_db.rec_from_acc!=0) {
                Cursor c = dbHelper.getAccountData(trans_db.rec_from_acc);
                c.moveToFirst();
                from_acc = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                c.close();
            }
            account.setText(from_acc);
        }
        else if(trans_db.rec_type.equals("Income"))
        {
            String to_acc=null;
            if(trans_db.rec_to_acc!=0) {
                Cursor c = dbHelper.getAccountData(trans_db.rec_to_acc);
                c.moveToFirst();
                to_acc = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                c.close();
            }
            account.setText(to_acc);
        }
        else if(trans_db.rec_type.equals("Transfer"))
        {
            String from_acc=null,to_acc=null;
            if(trans_db.rec_from_acc!=0 && trans_db.rec_to_acc!=0) {
                Cursor c = dbHelper.getAccountData(trans_db.rec_from_acc);
                c.moveToFirst();
                from_acc = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                Cursor c1 = dbHelper.getAccountData(trans_db.rec_to_acc);
                c1.moveToFirst();
                to_acc = c1.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                c.close();
                c1.close();
            }
            account.setText(from_acc + "-> " + to_acc);
            category.setText(null);
        }

        return convertView;
    }
}
