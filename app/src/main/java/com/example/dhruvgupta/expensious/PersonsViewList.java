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

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Amrit on 3/27/2015.
 */
public class PersonsViewList extends ActionBarActivity
{
    ListView personList;
    PersonsAdapter personsAdapter;
    SharedPreferences sp;
    DBHelper dbHelper;
    ArrayList<PersonDB> al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        personList = (ListView) findViewById(R.id.persons_list);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(PersonsViewList.this);

//        al = dbHelper.getAllPersons(sp.getInt("UID", 0));
//        personsAdapter = new PersonsAdapter(PersonsViewList.this, R.layout.list_person, al);
//        personList.setAdapter(personsAdapter);
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PersonsViewList.this,AddPersonActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        al = dbHelper.getAllPersons(sp.getInt("UID", 0));
        personsAdapter = new PersonsAdapter(PersonsViewList.this, R.layout.list_person, al);
        personList.setAdapter(personsAdapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);


    }
}
