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
 * Created by Amrit on 3/17/2015.
 */
public class AccountsAdapter extends ArrayAdapter <AccountsDB>
{
    Context context1;
    int layout;
    DBHelper dbHelper;
    SharedPreferences sp;
    String curCode;
    ArrayList<CurrencyDB> al1;
    ArrayList<AccountsDB>al;

    public AccountsAdapter(Context context, int resource,ArrayList<AccountsDB>objects)
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
        final TextView name=(TextView)convertView.findViewById(R.id.list_account_name);
        final TextView cur=(TextView)convertView.findViewById(R.id.list_account_cur);
        final TextView amt=(TextView)convertView.findViewById(R.id.list_account_amt);

        dbHelper = new DBHelper(getContext());
        sp = getContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

        AccountsDB db=al.get(position);

        name.setText(db.acc_name);
        amt.setText(db.acc_balance+"");

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
                cur.setText(curDB.c_symbol);
        }

        return convertView;
    }
}
