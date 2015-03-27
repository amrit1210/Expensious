package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Amrit on 3/27/2015.
 */
public class PersonsViewList extends ActionBarActivity
{
    ListView personList;
    PersonsAdapter personsAdapter;
    DBHelper dbHelper;
    ArrayList<PersonDB> al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        personList = (ListView) findViewById(R.id.persons_list);

        SharedPreferences sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(PersonsViewList.this);

        al = dbHelper.getAllPersons(sp.getInt("UID", 0));
        personsAdapter = new PersonsAdapter(PersonsViewList.this, R.layout.list_person, al);
        personList.setAdapter(personsAdapter);
        personList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonDB personDB = personsAdapter.getItem(position);
                Intent i = new Intent();
                i.putExtra("Person_Name", personDB.p_name);
                setResult(1, i);
                PersonsViewList.this.finish();
            }

        });
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
