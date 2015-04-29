 package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Amrit on 3/24/2015.
 */
public class AccountsViewList  extends ActionBarActivity
{   ListView acc_list;
    ArrayList<AccountsDB> al;
    AccountsAdapter accountsAdapter;
    DBHelper dbHelper;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_view);
        acc_list =(ListView)findViewById(R.id.accounts_list_view);
        sp= getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        dbHelper =new DBHelper(AccountsViewList.this);

        al= dbHelper.getAllAccounts(sp.getInt("UID",0));
        accountsAdapter =new AccountsAdapter(AccountsViewList.this,R.layout.list_account,al);
        acc_list.setAdapter(accountsAdapter);
        acc_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              AccountsDB acc_DB = accountsAdapter.getItem(position);
                Intent i=new Intent();
                i.putExtra("Acc_Name",acc_DB.acc_name);
                setResult(1,i);
                AccountsViewList.this.finish();
            }
        });
    }

}
