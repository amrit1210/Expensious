package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class CurrencyViewList extends ActionBarActivity
{
    ListView cur_list;
    ArrayList<CurrencyDB> al;
    CurrencyAdapter currencyAdapter;
    DBHelper dbHelper;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_view);

        cur_list = (ListView)findViewById(R.id.currencies_list_view);
        dbHelper = new DBHelper(CurrencyViewList.this);
        sp= getSharedPreferences("USER_PREFS",MODE_PRIVATE);

        ListOfCurrencies lc = new ListOfCurrencies();
        al = lc.getAllCurrencies();

        currencyAdapter =new CurrencyAdapter(CurrencyViewList.this,R.layout.list_currency,al);
        cur_list.setAdapter(currencyAdapter);
        cur_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrencyDB cur_DB = currencyAdapter.getItem(position);
                dbHelper.updateSettings(sp.getInt("UID", 0), cur_DB.c_code);

                Intent i=new Intent();
                setResult(1, i);
                CurrencyViewList.this.finish();
            }
        });

        this.getSupportActionBar().setTitle("Currency");
    }

}
