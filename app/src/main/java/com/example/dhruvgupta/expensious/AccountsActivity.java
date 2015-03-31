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
 * Created by Gaurav on 13-Mar-15.
 */
public class AccountsActivity extends ActionBarActivity
{
    ListView listView;
    ArrayList<AccountsDB> al;
    AccountsAdapter accountsAdapter;
    DBHelper dbHelper;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        listView = (ListView) findViewById(R.id.accounts_list);

        sp= getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        dbHelper =new DBHelper(AccountsActivity.this);

        al= dbHelper.getAllAccounts(sp.getInt("UID",0));
        accountsAdapter =new AccountsAdapter(AccountsActivity.this,R.layout.list_account,al);
        listView.setAdapter(accountsAdapter);
        registerForContextMenu(listView);
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
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        AccountsDB acc_DB = (AccountsDB)listView.getAdapter().getItem(listPosition);
        int id=item.getItemId();

        if(id==R.id.Edit)
        {
            Cursor c= dbHelper.getAccountData(acc_DB.acc_id);
            c.moveToFirst();
            int acc_id=c.getInt(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_ID));
            int acc_uid=c.getInt(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));
            String acc_name=c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
            float acc_bal=c.getFloat(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
            String acc_cur=c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_CURRENCY));
            String acc_note=c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
            int acc_show=c.getInt(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
            c.close();
            Intent i=new Intent(AccountsActivity.this,AddAccountActivity.class);
            i.putExtra("acc_id",acc_id);
            i.putExtra("acc_uid",acc_uid);
            i.putExtra("acc_name",acc_name);
            i.putExtra("acc_bal",acc_bal);
            i.putExtra("acc_cur",acc_cur);
            i.putExtra("acc_note",acc_note);
            i.putExtra("acc_show",acc_show);
            startActivity(i);
        }

        if(id==R.id.Delete)
        {
            dbHelper.deleteAccount(acc_DB.acc_id,sp.getInt("UID",0));
            Intent i=new Intent(AccountsActivity.this,AccountsActivity.class);
            startActivity(i);
            Toast.makeText(AccountsActivity.this, "Account Deleted", Toast.LENGTH_LONG).show();
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
