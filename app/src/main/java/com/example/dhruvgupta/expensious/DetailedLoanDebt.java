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
 * Created by dhruvgupta on 4/12/2015.
 */
public class DetailedLoanDebt extends ActionBarActivity
{
    ListView listView;
    ArrayList<LoanDebtDB> al;
    LoanDebtAdapter loanDebtAdapter;
    DBHelper dbHelper;
    String type;
    TextView mTotal, mReName, mReAmt, mRemaining, mTotCur, mReCur, mRemainCur;
    SharedPreferences sp;
    int ld_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        mTotal = (TextView) findViewById(R.id.detailed_ld_total_amt);
        mReName = (TextView) findViewById(R.id.detailed_ld_re_name);
        mReAmt = (TextView) findViewById(R.id.detailed_ld_re_amt);
        mRemaining = (TextView) findViewById(R.id.detailed_ld_remain_amt);
        mTotCur = (TextView) findViewById(R.id.detailed_ld_total_cur);
        mReCur = (TextView) findViewById(R.id.detailed_ld_re_cur);
        mRemainCur = (TextView) findViewById(R.id.detailed_ld_remain_cur);
        listView = (ListView) findViewById(R.id.trans_list);

        sp= getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        dbHelper =new DBHelper(DetailedLoanDebt.this);

        ld_id = getIntent().getIntExtra("LD_ID", 0);
        Cursor c = dbHelper.getLoanDebtData(ld_id);
        c.moveToFirst();
        type = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_TYPE));
        mTotal.setText(c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_BALANCE)));
        mTotCur.setText("Cur");
        mReCur.setText("Cur");
        mRemainCur.setText("Cur");

        if (type.equals("Loan"))
            mReName.setText("Recovered");
        else if (type.equals("Debt"))
            mReName.setText("Repaid");

        c.close();

        al= dbHelper.getAllLoanDebt(sp.getInt("UID", 0), ld_id);
        loanDebtAdapter =new LoanDebtAdapter(DetailedLoanDebt.this,R.layout.list_loan_debt,al);
        listView.setAdapter(loanDebtAdapter);
        registerForContextMenu(listView);
    }

    public void onAdd(View v)
    {
        Intent i = new Intent(DetailedLoanDebt.this, DetailedAddLD.class);
        i.putExtra("LD_ID", ld_id);
        startActivity(i);
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
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        LoanDebtDB loanDebtDB = (LoanDebtDB) listView.getAdapter().getItem(listPosition);
        int id = item.getItemId();

        if (id == R.id.Edit) {
            Cursor c = dbHelper.getLoanDebtData(loanDebtDB.l_id);
            c.moveToFirst();
            int l_id = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_ID));
            int l_u_id = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_UID));
            String l_date = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_DATE));
            float l_balance = c.getFloat(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_BALANCE));
            String l_cur = "Rs.";
            String l_note = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_NOTE));
            String l_type = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_TYPE));
            int l_person = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_PERSON));
            int l_fromAccount = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_FROM_ACC));
            int l_toAccount = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_TO_ACC));
            String l_time = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_TIME));
            int l_parent = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_PARENT));
            c.close();
            Intent i = new Intent(DetailedLoanDebt.this, AddLoanDebtActivity.class);
            i.putExtra("l_id", l_id);
            i.putExtra("l_u_id", l_u_id);
            i.putExtra("l_date", l_date);
            i.putExtra("l_bal", l_balance);
            i.putExtra("l_cur", l_cur);
            i.putExtra("l_note", l_note);
            i.putExtra("l_type", l_type);
            i.putExtra("l_fromAcc", l_fromAccount);
            i.putExtra("l_toAcc", l_toAccount);
            i.putExtra("l_time", l_time);
            i.putExtra("l_parent", l_parent);
            i.putExtra("l_person", l_person);
            startActivity(i);
        }

        if (id == R.id.Delete) {
            if (dbHelper.deleteLoanDebt(loanDebtDB.l_id, sp.getInt("UID", 0)) > 0) {
                Intent i = new Intent(DetailedLoanDebt.this, LoanDebtActivity.class);
                startActivity(i);
                Toast.makeText(DetailedLoanDebt.this, "Loan/Debt Deleted", Toast.LENGTH_LONG).show();
            }
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

