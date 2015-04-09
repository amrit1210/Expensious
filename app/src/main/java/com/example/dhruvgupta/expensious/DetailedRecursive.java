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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dhruvgupta on 4/9/2015.
 */
public class DetailedRecursive extends ActionBarActivity {
    ListView listView;
    ArrayList<TransactionsDB> al;
    DBHelper dbHelper;
    TransactionAdapter transactionAdapter;
    TextView s_date, e_date, amt, cur;
    int rec_id;
    Cursor c;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_recursive);

        s_date = (TextView) findViewById(R.id.detailed_rec_start_date);
        e_date = (TextView) findViewById(R.id.detailed_rec_end_date);
        amt = (TextView) findViewById(R.id.detailed_rec_amt);
        listView = (ListView) findViewById(R.id.detailed_rec_list);

        sp = getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        dbHelper =new DBHelper(DetailedRecursive.this);

        rec_id = getIntent().getIntExtra("REC_ID", 0);
        c = dbHelper.getRecursiveData(rec_id);
        c.moveToFirst();
        s_date.setText(c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_START_DATE)));
        e_date.setText(c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_END_DATE)));
        amt.setText(c.getFloat(c.getColumnIndex(DBHelper.RECURSIVE_COL_BALANCE))+"");
        c.close();

        al= dbHelper.getAllRecTrans(sp.getInt("UID", 0), rec_id);
        transactionAdapter =new TransactionAdapter(DetailedRecursive.this,R.layout.list_transaction,al);
        listView.setAdapter(transactionAdapter);
        registerForContextMenu(listView);
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
        } else if (id == R.id.action_acc) {
            Intent i = new Intent(this, AddAccountActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_person) {
            Intent i = new Intent(this, AddPersonActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_trans) {
            Intent i = new Intent(this, AddTransactionsActivity.class);
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
        TransactionsDB transactionsDB= (TransactionsDB)listView.getAdapter().getItem(listPosition);
        int id=item.getItemId();

        if(id==R.id.Edit)
        {
            Cursor c= dbHelper.getTransactionData(transactionsDB.t_id);
            c.moveToFirst();
            int t_id=c.getInt(c.getColumnIndex(DBHelper.TRANSACTION_COL_ID));
            int t_u_id=c.getInt(c.getColumnIndex(DBHelper.TRANSACTION_COL_UID));
            int t_rec_id=c.getInt(c.getColumnIndex(DBHelper.TRANSACTION_COL_RECID));
            String t_date=c.getString(c.getColumnIndex(DBHelper.TRANSACTION_COL_DATE));
            float t_bal=c.getFloat(c.getColumnIndex(DBHelper.TRANSACTION_COL_BALANCE));
            String t_cur="Rs.";
            String t_note=c.getString(c.getColumnIndex(DBHelper.TRANSACTION_COL_NOTE));
            String t_type=c.getString(c.getColumnIndex(DBHelper.TRANSACTION_COL_TYPE));
            int t_category=c.getInt(c.getColumnIndex(DBHelper.TRANSACTION_COL_CATEGORY));
            int t_subcategory=c.getInt(c.getColumnIndex(DBHelper.TRANSACTION_COL_SUBCATEGORY));
            int t_person=c.getInt(c.getColumnIndex(DBHelper.TRANSACTION_COL_PERSON));
            int t_fromAccount = c.getInt(c.getColumnIndex(DBHelper.TRANSACTION_COL_FROM_ACC));
            int t_toAccount=c.getInt(c.getColumnIndex(DBHelper.TRANSACTION_COL_TO_ACC));
            String t_time=c.getString(c.getColumnIndex(DBHelper.TRANSACTION_COL_TIME));
            int t_show=c.getInt(c.getColumnIndex(DBHelper.TRANSACTION_COL_SHOW));
            c.close();
            Intent i=new Intent(DetailedRecursive.this, AddTransactionsActivity.class);
            i.putExtra("t_id",t_id);
            i.putExtra("t_u_id",t_u_id);
            i.putExtra("t_rec_id",t_rec_id);
            i.putExtra("t_date",t_date);
            i.putExtra("t_bal",t_bal);
            i.putExtra("t_cur",t_cur);
            i.putExtra("t_note",t_note);
            i.putExtra("t_type",t_type);
            i.putExtra("t_fromAccount",t_fromAccount);
            i.putExtra("t_toAccount",t_toAccount);
            i.putExtra("t_time",t_time);
            i.putExtra("t_category",t_category);
            i.putExtra("t_show",t_show);
            i.putExtra("t_person",t_person);
            i.putExtra("t_subcategory",t_subcategory);
            startActivityForResult(i, 1);
        }

        if(id==R.id.Delete)
        {
            if(dbHelper.deleteTransaction(transactionsDB.t_id,sp.getInt("UID",0))>0)
            {
                Intent i = new Intent(DetailedRecursive.this, DetailedRecursive.class);
                startActivity(i);
                Toast.makeText(DetailedRecursive.this, "Transaction Deleted", Toast.LENGTH_LONG).show();
            }
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == 1)
        {
            rec_id = data.getExtras().getInt("REC_ID");
            Intent i = new Intent(DetailedRecursive.this, DetailedRecursive.class);
            i.putExtra("REC_ID", rec_id);
            startActivity(i);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
    }
}
