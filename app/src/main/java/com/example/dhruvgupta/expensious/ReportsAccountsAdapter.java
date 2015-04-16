package com.example.dhruvgupta.expensious;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Gaurav on 16-Apr-15.
 */
public class ReportsAccountsAdapter extends ArrayAdapter<AccountsDB>
{
    Context context1;
    int layout;
    ArrayList<AccountsDB> al;

    public ReportsAccountsAdapter(Context context, int resource,ArrayList<AccountsDB> objects)
    {
        super(context, resource,objects);
        context1=context;
        layout=resource;
        al=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DBHelper dbHelper=new DBHelper(getContext());
        TransactionsDB transactionsDB;
        ArrayList<TransactionsDB> allTransactions;
        float amount=0;
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

        AccountsDB db=al.get(position);
        name.setText(db.acc_name);
        while(transactionsDBIterator.hasNext())
        {
            transactionsDB=transactionsDBIterator.next();
            if(transactionsDB.t_type.equals("Expense"))
            if(transactionsDB.t_from_acc == db.acc_id)
            {
                amount +=transactionsDB.t_balance;
            }
            amt.setText(amount+"");
            cur.setText("CUR");
        }

        return convertView;
    }
}
