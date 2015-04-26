package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Gaurav on 16-Apr-15.
 */
public class ReportsPersonsAdapter extends ArrayAdapter<PersonDB>
{
    Context context1;
    int layout;
    String curCode;
    ArrayList<CurrencyDB> al1;
    ArrayList<PersonDB> al;
    String month_name;

    public ReportsPersonsAdapter(Context context, int resource,ArrayList<PersonDB> objects,String str)
    {
        super(context, resource,objects);
        context1=context;
        layout=resource;
        al=objects;
        month_name=str;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DBHelper dbHelper=new DBHelper(getContext());
        TransactionsDB transactionsDB;
        ArrayList<TransactionsDB> allTransactions;
        float amount=0;
        String sysDate;
        SimpleDateFormat sdf,month_date;
        SharedPreferences sp = getContext().getSharedPreferences("USER_PREFS",Context.MODE_PRIVATE);
        Iterator<TransactionsDB> transactionsDBIterator;
        if(convertView==null)
        {
            LayoutInflater in =(LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=in.inflate(layout,null);
        }
        final TextView percent=(TextView)convertView.findViewById(R.id.list_report_percent);
        final TextView name=(TextView) convertView.findViewById(R.id.list_report_name);
        final TextView cur=(TextView)convertView.findViewById(R.id.list_report_cur);
        final TextView amt=(TextView)convertView.findViewById(R.id.list_report_amt);

        allTransactions=dbHelper.getAllTransactions(sp.getInt("UID",0));
        transactionsDBIterator=allTransactions.iterator();

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

        PersonDB db=al.get(position);
        name.setText(db.p_name);
        month_date = new SimpleDateFormat("MMMM");
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        while(transactionsDBIterator.hasNext())
        {
            transactionsDB=transactionsDBIterator.next();
            Date d= null;
            try
            {
                d = sdf.parse(transactionsDB.t_date);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            sysDate=month_date.format(d);
            if(sysDate.equals(month_name))
            {
                if(transactionsDB.t_type.equals("Expense"))
                {
                    if(transactionsDB.t_p_id == db.p_id)
                    {
                        amount += transactionsDB.t_balance;
                    }
                }
            }
            amt.setText(amount+"");
        }

        return convertView;
    }
}
