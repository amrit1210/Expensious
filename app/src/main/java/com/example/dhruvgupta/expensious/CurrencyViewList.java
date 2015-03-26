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
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_view);

        cur_list = (ListView)findViewById(R.id.currencies_list_view);
        sp= getSharedPreferences("USER_PREFS",MODE_PRIVATE);

        ListOfCurrencies lc = new ListOfCurrencies();
        al = lc.getAllCurrencies();

        currencyAdapter =new CurrencyAdapter(CurrencyViewList.this,R.layout.list_currency,al);
        cur_list.setAdapter(currencyAdapter);
        cur_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrencyDB cur_DB = currencyAdapter.getItem(position);
                Intent i=new Intent();
                i.putExtra("CUR_SYM",cur_DB.c_symbol);
                setResult(1,i);
                CurrencyViewList.this.finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
