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
 * Created by Gaurav on 04-Apr-15.
 */
public class BudgetsAdapter extends ArrayAdapter<BudgetDB>
{
    Context context1;
    int layout;
    String curCode;
    DBHelper dbHelper;
    SharedPreferences sp;
    ArrayList<CurrencyDB> al1;
    ArrayList<BudgetDB> al;

    public BudgetsAdapter(Context context, int resource,ArrayList<BudgetDB> objects)
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
        final TextView startDate=(TextView)convertView.findViewById(R.id.list_budget_start_date);
        final TextView endDate=(TextView)convertView.findViewById(R.id.list_budget_end_date);
        final TextView b_cur=(TextView)convertView.findViewById(R.id.list_budget_cur);
        final TextView b_amt=(TextView)convertView.findViewById(R.id.list_budget_amt);

        dbHelper = new DBHelper(getContext());
        sp = getContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

        BudgetDB db=al.get(position);

        startDate.setText(db.b_startDate);
        endDate.setText(db.b_endDate);
        b_amt.setText(db.b_amount+"");

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
                b_cur.setText(curDB.c_symbol);
        }

        return convertView;
    }
}
