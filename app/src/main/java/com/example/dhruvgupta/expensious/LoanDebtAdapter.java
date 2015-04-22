package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Shubhz on 4/6/2015.
 */
public class LoanDebtAdapter extends ArrayAdapter {
    Context context1;
    int layout;
    DBHelper dbHelper;
    SharedPreferences sp;
    String curCode;
    ArrayList<CurrencyDB> al1;
    ArrayList<LoanDebtDB> al;

    public LoanDebtAdapter(Context context, int resource,  ArrayList<LoanDebtDB> al1) {
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
        final TextView date=(TextView)convertView.findViewById(R.id.list_loan_debt_date);
        final TextView note=(TextView)convertView.findViewById(R.id.list_loan_debt_note);
        final TextView time=(TextView)convertView.findViewById(R.id.list_loan_debt_time);
        final TextView currency=(TextView)convertView.findViewById(R.id.list_loan_debt_cur);
        final TextView account=(TextView)convertView.findViewById(R.id.list_loan_debt_acc);
        final TextView amount=(TextView)convertView.findViewById(R.id.list_loan_debt_amt);
        final TextView ld_type= (TextView)convertView.findViewById(R.id.list_loan_debt_type);

        dbHelper = new DBHelper(getContext());
        sp = getContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

        LoanDebtDB trans_db=al.get(position);
        date.setText(trans_db.l_date);
        note.setText(trans_db.l_note);
        time.setText(trans_db.l_time);
        amount.setText(trans_db.l_balance+"");
        ld_type.setText(trans_db.l_type);

        al1 = new ListOfCurrencies().getAllCurrencies();

        Cursor c1 = dbHelper.getSettingsData(sp.getInt("UID", 0));
        c1.moveToFirst();
        curCode = c1.getString(c1.getColumnIndex(DBHelper.SETTINGS_COL_CUR_CODE));
        c1.close();

        Iterator<CurrencyDB> iterator = al1.iterator();
        while (iterator.hasNext())
        {
            CurrencyDB curDB = iterator.next();
            if (curDB.c_code.equals(curCode))
                currency.setText(curDB.c_symbol);
        }

        if(trans_db.l_type.equals("Loan"))
        {
            String from_acc=null;
            if(trans_db.l_from_acc!=0) {
                Cursor c = dbHelper.getAccountData(trans_db.l_from_acc);
                c.moveToFirst();
                from_acc = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
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
                c.close();
            }
            account.setText(to_acc);
        }

        return convertView;
    }
}
