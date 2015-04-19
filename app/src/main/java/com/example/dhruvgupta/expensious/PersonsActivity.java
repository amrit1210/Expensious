package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amrit on 3/19/2015.
 */
public class PersonsActivity extends ActionBarActivity
{
    ListView listView;
    PersonsAdapter personsAdapter;
    DBHelper dbHelper;
    SharedPreferences sp;
    ArrayList<PersonDB> al;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        listView=(ListView)findViewById(R.id.persons_list);

        sp=getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        dbHelper=new DBHelper(PersonsActivity.this);

        al= dbHelper.getAllPersons(sp.getInt("UID",0));
        personsAdapter =new PersonsAdapter(PersonsActivity.this,R.layout.list_person,al);
        listView.setAdapter(personsAdapter);
        registerForContextMenu(listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Persons");
        query.fromPin("pinPersons");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todos, ParseException e) {
                if (e == null) {
                    for (final ParseObject todo : todos) {
                        // Set isdraft flag to false before syncing to Parse
                        // todo.setDraft(false);
                        todo.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                //if (e == null) {
                                // Let adapter know to update view
                                if (!isFinishing()) {
                                    //refresh list data
//                                    todo.unpinInBackground("pinPersons");
                                    System.out.println("Image DATA is saved ..... ");

                                }

                            }

                        });

                    }
                } else {
                    Log.i("MainActivity", "syncTodosToParse: Error finding pinned todos: " + e.getMessage());
                    e.printStackTrace();
                }
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
        else if(id == R.id.action_acc)
        {
            Intent i =new Intent(this, AddAccountActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_person)
        {
            Intent i =new Intent(this, AddPersonActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_trans)
        {
            Intent i =new Intent(this, AddTransactionsActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_budget)
        {
            Intent i =new Intent(this, AddBudgetActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        PersonDB per_DB = (PersonDB)listView.getAdapter().getItem(listPosition);
        int id=item.getItemId();

        if(id==R.id.Edit)
        {
            Cursor c= dbHelper.getPersonData(per_DB.p_id);
            c.moveToFirst();
            int p_id=c.getInt(c.getColumnIndex(DBHelper.PERSON_COL_ID));
            int p_uid=c.getInt(c.getColumnIndex(DBHelper.PERSON_COL_UID));
            String p_name=c.getString(c.getColumnIndex(DBHelper.PERSON_COL_NAME));
            String p_color=c.getString(c.getColumnIndex(DBHelper.PERSON_COL_COLOR));
            Intent i=new Intent(PersonsActivity.this,AddPersonActivity.class);
            i.putExtra("p_id",p_id);
            i.putExtra("p_uid",p_uid);
            i.putExtra("p_name",p_name);
            i.putExtra("p_color",p_color);
            startActivity(i);
        }

        if(id==R.id.Delete)
        {
            dbHelper.deletePerson(per_DB.p_id,sp.getInt("UID",0));
            Intent i=new Intent(PersonsActivity.this,PersonsActivity.class);
            startActivity(i);
            Toast.makeText(PersonsActivity.this, "Person Deleted", Toast.LENGTH_LONG).show();
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
    }
}
