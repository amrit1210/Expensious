package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Gaurav on 27-Apr-15.
 */
public class ReportsCategoriesAdapter extends ArrayAdapter<CategoryDB_Specific>
{
    Context context1;
    int layout;
    String curCode;
    ArrayList<CurrencyDB> al1;
    ArrayList<CategoryDB_Specific> al;
    int period;

    public ReportsCategoriesAdapter(Context context, int resource,ArrayList<CategoryDB_Specific> objects,int i)
    {
        super(context, resource,objects);
        context1=context;
        layout=resource;
        al=objects;
        period=i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DBHelper dbHelper=new DBHelper(getContext());
        TransactionsDB transactionsDB;
        ArrayList<TransactionsDB> allTransactions;
        float amount=0;
        String sysDate;
        SimpleDateFormat sdf,month_date,year_date;
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

        CategoryDB_Specific db=al.get(position);
        name.setText(db.c_name);
        month_date = new SimpleDateFormat("MMMM");
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        year_date = new SimpleDateFormat("yyyy");
        while(transactionsDBIterator.hasNext())
        {
            transactionsDB=transactionsDBIterator.next();
            try
            {
                Date d = sdf.parse(transactionsDB.t_date);
                Log.d("transDB", "amt = " + transactionsDB.t_balance);
                if(period == 0)
                {
                    sysDate=year_date.format(d);
                    if(sysDate.equals(PieChartActivity.year))
                    {
                        if(transactionsDB.t_type.equals("Expense"))
                        {
                            if(transactionsDB.t_c_id == db.c_id)
                            {
                                amount += transactionsDB.t_balance;
                            }
                        }
                    }
                }
                else if(period == 1)
                {
                    sysDate=month_date.format(d);
                    if(sysDate.equals(PieChartActivity.month_name))
                    {
                        if(transactionsDB.t_type.equals("Expense"))
                        {
                            if(transactionsDB.t_c_id == db.c_id)
                            {
                                amount +=transactionsDB.t_balance;
                            }
                        }
                    }
                }
                else if(period == 3)
                {
                    sysDate = sdf.format(d);
                    if (sysDate.equals(PieChartActivity.day))
                    {
                        if (transactionsDB.t_type.equals("Expense"))
                        {
                            if (transactionsDB.t_c_id == db.c_id)
                            {
                                amount += transactionsDB.t_balance;
                            }
                        }
                    }
                }
                else if(period == 4)
                {
                    if(!d.before(PieChartActivity.start) && !d.after(PieChartActivity.end))
                    {
                        if(transactionsDB.t_type.equals("Expense"))
                        {
                            if(transactionsDB.t_c_id == db.c_id)
                            {
                                amount += transactionsDB.t_balance;
                            }
                        }
                    }
                }
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
//            amt.setText(amount+"");
        }
        amt.setText(amount+"");
        return convertView;
    }
}