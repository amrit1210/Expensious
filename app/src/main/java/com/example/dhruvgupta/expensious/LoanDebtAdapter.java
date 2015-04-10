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
 * Created by Shubhz on 4/6/2015.
 */
public class LoanDebtAdapter extends ArrayAdapter {
    Context context1;
    int layout;
    ArrayList<LoanDebtDB> al;
//    String etype=null;

    public LoanDebtAdapter(Context context, int resource,  ArrayList<LoanDebtDB> al1) {
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
        final TextView date=(TextView)convertView.findViewById(R.id.list_transaction_date);
        final TextView note=(TextView)convertView.findViewById(R.id.list_transaction_note);
        final TextView time=(TextView)convertView.findViewById(R.id.list_transaction_time);
        final TextView currency=(TextView)convertView.findViewById(R.id.list_transaction_cur);
        final TextView account=(TextView)convertView.findViewById(R.id.list_transaction_acc);
        final TextView amount=(TextView)convertView.findViewById(R.id.list_transaction_amt);
        DBHelper dbHelper=new DBHelper(getContext().getApplicationContext());

        LoanDebtDB trans_db=al.get(position);
        date.setText(trans_db.l_date);
        note.setText(trans_db.l_note);
        time.setText(trans_db.l_time);
        amount.setText(trans_db.l_balance+"");


        if(trans_db.l_type.equals("Loan"))
        {
            String from_acc=null;
            if(trans_db.l_from_acc!=0) {
                Cursor c = dbHelper.getAccountData(trans_db.l_from_acc);
                c.moveToFirst();
                from_acc = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                acc_currency = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_CURRENCY));
                c.close();
            }
            account.setText(from_acc);
        }
        else if(trans_db.l_type.equals("Debt"))
        {
            String to_acc=null;
            if(trans_db.l_to_acc!=0) {
                Cursor c = dbHelper.getAccountData(trans_db.l_to_acc);
                c.moveToFirst();
                to_acc = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                acc_currency = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_CURRENCY));
                c.close();
            }
            account.setText(to_acc);
        }
        currency.setText(acc_currency);

        return convertView;
    }
}
