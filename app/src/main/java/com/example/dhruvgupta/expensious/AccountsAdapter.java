package com.example.dhruvgupta.expensious;

import android.accounts.Account;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Amrit on 3/17/2015.
 */
public class AccountsAdapter extends ArrayAdapter <AccountsDB> {
    Context context1;
    int layout;
    ArrayList<AccountsDB>al;
    int id;
    public AccountsAdapter(Context context, int resource,ArrayList<AccountsDB>objects) {
        super(context, resource,objects);
        context1=context;
        layout=resource;
        al=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater in =(LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=in.inflate(layout,null);
        }
       final TextView name=(TextView)convertView.findViewById(R.id.list_account_name);
        final TextView cur=(TextView)convertView.findViewById(R.id.list_account_cur);
        final TextView amt=(TextView)convertView.findViewById(R.id.list_account_amt);
        AccountsDB db=al.get(position);
        name.setText(db.acc_name);
        cur.setText(db.acc_currency);
        amt.setText(db.acc_balance+"");
        id=db.acc_id;
        return convertView;
    }

}
