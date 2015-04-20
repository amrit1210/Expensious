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

import java.util.ArrayList;

/**
 * Created by dhruvgupta on 4/6/2015.
 */
public class RecursiveActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        RecursiveFragment recursiveFragment=new RecursiveFragment();
        fragmentTransaction.replace(R.id.container,recursiveFragment);
        fragmentTransaction.commit();

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

        return super.onOptionsItemSelected(item);
    }

    public static class RecursiveFragment extends Fragment
    {
        ListView listView;
        ArrayList<RecursiveDB> al;
        RecursiveAdapter recursiveAdapter;
        DBHelper dbHelper;
        SharedPreferences sp;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             super.onCreateView(inflater, container, savedInstanceState);
            return inflater.inflate(R.layout.activity_recursive,container,false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView =getView();
            listView = (ListView) rootView.findViewById(R.id.recursive_list);

            sp= getActivity().getSharedPreferences("USER_PREFS",MODE_PRIVATE);
            dbHelper =new DBHelper(getActivity());

            al= dbHelper.getAllRecursive(sp.getInt("UID", 0));
            recursiveAdapter =new RecursiveAdapter(getActivity(),R.layout.list_recursive,al);
            listView.setAdapter(recursiveAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RecursiveDB recDb = (RecursiveDB) recursiveAdapter.getItem(position);
                    Intent i = new Intent(getActivity(), DetailedRecursive.class);
                    i.putExtra("REC_ID", recDb.rec_id);
                    startActivity(i);
                }
            });
            registerForContextMenu(listView);
        }
        @Override
        public boolean onContextItemSelected(MenuItem item)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int listPosition = info.position;
            RecursiveDB recursiveDB= (RecursiveDB)listView.getAdapter().getItem(listPosition);
            int id=item.getItemId();

            if(id==R.id.Edit)
            {
                Cursor c= dbHelper.getRecursiveData(recursiveDB.rec_id);
                c.moveToFirst();
                int rec_id=c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_ID));
                int rec_u_id=c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_UID));
                String rec_start_date=c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_START_DATE));
                String rec_end_date=c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_END_DATE));
                String rec_next_date=c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_NEXT_DATE));
                float rec_bal=c.getFloat(c.getColumnIndex(DBHelper.RECURSIVE_COL_BALANCE));
                String rec_cur="Rs.";
                String rec_note=c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_NOTE));
                String rec_type=c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_TYPE));
                int rec_category=c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_CATEGORY));
                int rec_subcategory=c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_SUBCATEGORY));
                int rec_person=c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_PERSON));
                int rec_fromAccount = c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_FROM_ACC));
                int rec_toAccount=c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_TO_ACC));
                String rec_time=c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_TIME));
                int rec_show=c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_SHOW));
                int rec_recurring=c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_RECURRING));
                int rec_alert=c.getInt(c.getColumnIndex(DBHelper.RECURSIVE_COL_ALERT));
                c.close();
                Intent i=new Intent(getActivity(),AddRecursiveActivity.class);
                i.putExtra("rec_id",rec_id);
                i.putExtra("rec_u_id",rec_u_id);
                i.putExtra("rec_start_date",rec_start_date);
                i.putExtra("rec_end_date",rec_end_date);
                i.putExtra("rec_next_date",rec_next_date);
                i.putExtra("rec_bal",rec_bal);
                i.putExtra("rec_cur",rec_cur);
                i.putExtra("rec_note",rec_note);
                i.putExtra("rec_type",rec_type);
                i.putExtra("rec_fromAccount",rec_fromAccount);
                i.putExtra("rec_toAccount",rec_toAccount);
                i.putExtra("rec_time",rec_time);
                i.putExtra("rec_category",rec_category);
                i.putExtra("rec_show",rec_show);
                i.putExtra("rec_recurring",rec_recurring);
                i.putExtra("rec_alert",rec_alert);
                i.putExtra("rec_person",rec_person);
                i.putExtra("rec_subcategory",rec_subcategory);
                startActivity(i);
            }

            if(id==R.id.Delete)
            {
                if(dbHelper.deleteRecursive(recursiveDB.rec_id,sp.getInt("UID",0))>0)
                {
                    Intent i = new Intent(getActivity(), RecursiveActivity.class);
                    startActivity(i);
                    Toast.makeText(getActivity(), "Recursive Transaction Deleted", Toast.LENGTH_LONG).show();
                }
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


}

