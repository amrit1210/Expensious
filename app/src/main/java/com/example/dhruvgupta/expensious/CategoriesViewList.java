package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Gaurav on 20-Apr-15.
 */
public class CategoriesViewList extends ActionBarActivity
{
    ListView categoryList;
    CategoriesAdapter categoriesAdapter;
    DBHelper dbHelper;
    ArrayList<String> al;
    Collection<String> al1;
    ArrayList<String> keys;
    int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        categoryList = (ListView) findViewById(R.id.category_list);

        SharedPreferences sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(CategoriesViewList.this);

        if(type==0) {
            al1 = CategoriesActivity.mapExpense.values();
            keys = (ArrayList) CategoriesActivity.mapExpense.keySet();
        }
        else if(type==1)
        {
             al1 = CategoriesActivity.mapIncome.values();
             keys = (ArrayList) CategoriesActivity.mapIncome.keySet();
        }
        categoriesAdapter = new CategoriesAdapter(CategoriesViewList.this, R.layout.list_category, (ArrayList)al1);
        categoryList.setAdapter(categoriesAdapter);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
             //   String s = categoriesAdapter.getItem(position);

              String s=keys.get(position);
                Intent i = new Intent();
                i.putExtra("Category_Id",s);
              //  i.putExtra("Cat_Id",)
                setResult(1, i);
                CategoriesViewList.this.finish();
            }
        });
    }

    public void onExpenseBtnClick(View v)
    {
        type=0;

    }
    public void onIncomeBtnClick(View v)

    {
        type=1;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}
