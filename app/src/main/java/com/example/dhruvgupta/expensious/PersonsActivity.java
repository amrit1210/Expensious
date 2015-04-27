package com.example.dhruvgupta.expensious;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseObject;


import java.util.ArrayList;


/**
 * Created by Amrit on 3/19/2015.
 */
public class PersonsActivity extends ActionBarActivity
{

//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_persons);
//        listView=(ListView)findViewById(R.id.persons_list);
//
//        sp=getSharedPreferences("USER_PREFS",MODE_PRIVATE);
//        dbHelper=new DBHelper(PersonsActivity.this);
//
//        al= dbHelper.getAllPersons(sp.getInt("UID",0));
//        personsAdapter =new PersonsAdapter(PersonsActivity.this,R.layout.list_person,al);
//        listView.setAdapter(personsAdapter);
//        registerForContextMenu(listView);
//
//    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        this.setDefaultStartPositionNavigation(8);
//
//        android.support.v4.app.FragmentManager mFragmentManager = getSupportFragmentManager();
//        android.support.v4.app.Fragment mFragment=null;
//        mFragmentManager.beginTransaction().replace(layoutId, new PersonsFragment()).commit();
//
       PersonsFragment fragment = new PersonsFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PersonsActivity.this, SettingsActivity.class);
        startActivity(intent);
        this.finish();
    }

    public static class PersonsFragment extends Fragment
    {
        ListView listView;
        PersonsAdapter personsAdapter;
        DBHelper dbHelper;
        SharedPreferences sp;
        ArrayList<PersonDB> al;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           super.onCreateView(inflater, container, savedInstanceState);
            return inflater.inflate(R.layout.activity_persons,container,false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView=getView();

            listView=(ListView)rootView.findViewById(R.id.persons_list);

        sp=getActivity().getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        dbHelper=new DBHelper(getActivity());

        al= dbHelper.getAllPersons(sp.getInt("UID",0));
        personsAdapter =new PersonsAdapter(getActivity(),R.layout.list_person,al);
        listView.setAdapter(personsAdapter);
        registerForContextMenu(listView);
        }

        @Override
        public boolean onContextItemSelected(MenuItem item)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int listPosition = info.position;
            final PersonDB per_DB = (PersonDB)listView.getAdapter().getItem(listPosition);
            int id=item.getItemId();

            if(id==R.id.Edit)
            {
                Cursor c= dbHelper.getPersonData(per_DB.p_id);
                c.moveToFirst();
                int p_id=c.getInt(c.getColumnIndex(DBHelper.PERSON_COL_ID));
                int p_uid=c.getInt(c.getColumnIndex(DBHelper.PERSON_COL_UID));
                String p_name=c.getString(c.getColumnIndex(DBHelper.PERSON_COL_NAME));
                String p_color=c.getString(c.getColumnIndex(DBHelper.PERSON_COL_COLOR));
                String p_color_code = c.getString(c.getColumnIndex(DBHelper.PERSON_COL_COLOR_CODE));
                Intent i=new Intent(getActivity(),AddPersonActivity.class);
                i.putExtra("p_id",p_id);
                i.putExtra("p_uid",p_uid);
                i.putExtra("p_name",p_name);
                i.putExtra("p_color",p_color);
                i.putExtra("p_color_code", p_color_code);
                startActivity(i);
            }

            if(id==R.id.Delete)
            {
                dbHelper.deletePerson(per_DB.p_id,sp.getInt("UID",0));

                ParseObject object = new ParseObject("Persons");
                object.put("p_id", per_DB.p_id);
                object.put("p_uid", per_DB.p_u_id);
                object.put("p_name", per_DB.p_name);
                object.put("p_color", per_DB.p_color);
                object.put("p_color_code", per_DB.p_color_code);
                object.pinInBackground("pinPersonsDelete");

                Intent i=new Intent(getActivity(),PersonsActivity.class);
                startActivity(i);
                Toast.makeText(getActivity(), "Person Deleted", Toast.LENGTH_LONG).show();
            }

            return super.onContextItemSelected(item);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
        {
            super.onCreateContextMenu(menu, v, menuInfo);

            MenuInflater inflater= getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu,menu);
        }


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
        if (id == android.R.id.home) {
            Intent parentIntent1 = new Intent(this,SettingsActivity.class);
            startActivity(parentIntent1);
            return true;
        }
        else if (id == R.id.action_settings)
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


}
