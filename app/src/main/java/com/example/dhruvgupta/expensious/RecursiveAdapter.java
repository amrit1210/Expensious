package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dhruvgupta on 4/7/2015.
 */
public class RecursiveAdapter extends ArrayAdapter {
    Context context1;
    int layout;
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
        String acc_currency=null;
        if(convertView==null)
        {
            LayoutInflater in=(LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= in.inflate(layout,null);
        }
        final TextView s_date=(TextView)convertView.findViewById(R.id.list_recursive_start_date);
        final TextView e_date=(TextView)convertView.findViewById(R.id.list_recursive_end_date);
        final TextView category=(TextView)convertView.findViewById(R.id.list_recursive_cat);
        final TextView note=(TextView)convertView.findViewById(R.id.list_recursive_note);
        final TextView currency=(TextView)convertView.findViewById(R.id.list_recursive_cur);
        final TextView account=(TextView)convertView.findViewById(R.id.list_recurive_acc);
        final TextView amount=(TextView)convertView.findViewById(R.id.list_recursive_amt);
        DBHelper dbHelper=new DBHelper(getContext().getApplicationContext());

        RecursiveDB trans_db=al.get(position);
        s_date.setText(trans_db.rec_start_date);
        category.setText(trans_db.rec_c_id+"");
        note.setText(trans_db.rec_note);
        e_date.setText(trans_db.rec_end_date);
        amount.setText(trans_db.rec_balance+"");


        if(trans_db.rec_type.equals("Expense"))
        {
            String from_acc=null;
            if(trans_db.rec_from_acc!=0) {
                Cursor c = dbHelper.getAccountData(trans_db.rec_from_acc);
                c.moveToFirst();
                from_acc = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                acc_currency = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_CURRENCY));
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
                acc_currency = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_CURRENCY));
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
                acc_currency = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_CURRENCY));
                Cursor c1 = dbHelper.getAccountData(trans_db.rec_to_acc);
                c1.moveToFirst();
                to_acc = c1.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                c.close();
                c1.close();
            }
            account.setText(from_acc + "-> " + to_acc);
        }
        currency.setText(acc_currency);

        return convertView;
    }
}
