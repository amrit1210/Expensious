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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dhruvgupta on 4/12/2015.
 */
public class DetailedLoanDebt extends ActionBarActivity
{

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
        DetailedLoanDebtFragment fragment = new DetailedLoanDebtFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }
    public static class DetailedLoanDebtFragment extends Fragment
    {
        ListView listView;
        ArrayList<CurrencyDB> al1;
        ArrayList<LoanDebtDB> al;
        DetailedLDAdapter loanDebtAdapter;
        DBHelper dbHelper;
        String type, curCode;
        TextView mTotal, mReName, mReAmt, mRemaining, mTotCur, mReCur, mRemainCur;
        SharedPreferences sp;
        int ld_id;
        Button addLDbtn;


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             super.onCreateView(inflater, container, savedInstanceState);
            return inflater.inflate(R.layout.detailed_loan_debt,container,false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView=getView();

            mTotal = (TextView)rootView.findViewById(R.id.detailed_ld_total_amt);
            mReName = (TextView)rootView. findViewById(R.id.detailed_ld_re_name);
            mReAmt = (TextView)rootView. findViewById(R.id.detailed_ld_re_amt);
            mRemaining = (TextView)rootView. findViewById(R.id.detailed_ld_remain_amt);
            mTotCur = (TextView)rootView. findViewById(R.id.detailed_ld_total_cur);
            mReCur = (TextView)rootView. findViewById(R.id.detailed_ld_re_cur);
            mRemainCur = (TextView)rootView. findViewById(R.id.detailed_ld_remain_cur);
            listView = (ListView)rootView. findViewById(R.id.detailed_ld_list);
            addLDbtn=(Button)rootView.findViewById(R.id.add_ldbtn);
            sp= getActivity().getSharedPreferences("USER_PREFS",MODE_PRIVATE);
            dbHelper =new DBHelper(getActivity());

            ld_id = getActivity().getIntent().getIntExtra("LD_ID", 0);
            Cursor c = dbHelper.getLoanDebtData(ld_id);
            c.moveToFirst();
            type = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_TYPE));
            Log.i("type", c.getFloat(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_BALANCE))+"");
            mTotal.setText(c.getFloat(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_BALANCE))+"");

            c.close();

            al1 = new ListOfCurrencies().getAllCurrencies();

            Cursor c1 = dbHelper.getSettingsData(sp.getInt("UID", 0));
            c1.moveToFirst();
            curCode = c1.getString(c1.getColumnIndex(DBHelper.SETTINGS_COL_CUR_CODE));
            c1.close();

            Iterator<CurrencyDB> iterator = al1.iterator();
            while (iterator.hasNext())
            {
                CurrencyDB curDB = iterator.next();
                if (curDB.c_code.equals(curCode))
                    mTotCur.setText(curDB.c_symbol);
                mReCur.setText(curDB.c_symbol);
                mRemainCur.setText(curDB.c_symbol);
            }

            if (type.equals("Loan"))
                mReName.setText("Recovered :");
            else if (type.equals("Debt"))
                mReName.setText("Repaid :");

            ArrayList<LoanDebtDB> arrayList = dbHelper.getAllLoanDebt(sp.getInt("UID", 0), ld_id);
            float balance = 0;
            Iterator<LoanDebtDB> ld_iterator = null;
            if (!arrayList.isEmpty())
            {
                ld_iterator = arrayList.iterator();
                while (iterator.hasNext())
                {
                    LoanDebtDB ldDB = ld_iterator.next();
                    balance += ldDB.l_balance;
                }
            }

            mReAmt.setText(balance + "");
            mRemaining.setText(Float.parseFloat(mTotal.getText().toString()) - balance + "");

            al= dbHelper.getAllLoanDebt(sp.getInt("UID", 0), ld_id);
            loanDebtAdapter =new DetailedLDAdapter(getActivity(),R.layout.list_add_ld,al);
            listView.setAdapter(loanDebtAdapter);
            registerForContextMenu(listView);
            addLDbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAdd(v);
                }
            });

        }
        public void onAdd(View v)
        {
            Intent i = new Intent(getActivity(), DetailedAddLD.class);
            i.putExtra("LD_ID", ld_id);
            startActivity(i);
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
                String l_note = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_NOTE));
                String l_type = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_TYPE));
                int l_person = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_PERSON));
                int l_fromAccount = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_FROM_ACC));
                int l_toAccount = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_TO_ACC));
                String l_time = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_TIME));
                int l_parent = c.getInt(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_PARENT));
                c.close();
                Intent i = new Intent(getActivity(), DetailedAddLD.class);
                i.putExtra("l_id", l_id);
                i.putExtra("l_u_id", l_u_id);
                i.putExtra("l_date", l_date);
                i.putExtra("l_bal", l_balance);
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
                String l_type_old = loanDebtDB.l_type;
                int l_to_old = loanDebtDB.l_to_acc;
                int l_from_old = loanDebtDB.l_from_acc;
                float l_amt_old = loanDebtDB.l_balance;
                int l_parent = loanDebtDB.l_parent;
                Log.i("Detailed To From", l_to_old + " ; " + l_from_old);

                if (dbHelper.deleteLoanDebt(loanDebtDB.l_id, sp.getInt("UID", 0)) > 0) {

                    if (l_type_old.equals("Loan"))
                    {
                        Cursor cursor = dbHelper.getAccountData(l_to_old);
                        cursor.moveToFirst();

                        float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                        String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                        String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                        int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                        int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                        bal = bal - l_amt_old;

                        dbHelper.updateAccountData(l_to_old, name, bal, note, show, uid);
                        cursor.close();
                    }
                    else if (l_type_old.equals("Debt"))
                    {
                        Cursor cursor = dbHelper.getAccountData(l_from_old);
                        cursor.moveToFirst();

                        float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                        String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                        String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                        int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                        int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                        bal = bal + l_amt_old;

                        dbHelper.updateAccountData(l_from_old, name, bal, note, show, uid);
                        cursor.close();
                    }

                    Intent i = new Intent(getActivity(), DetailedLoanDebt.class);
                    i.putExtra("LD_ID", l_parent);
                    startActivity(i);
                    Toast.makeText(getActivity(), "Loan/Debt Deleted", Toast.LENGTH_LONG).show();
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
            Intent parentIntent1 = new Intent(this,LoanDebtActivity.class);
            startActivity(parentIntent1);
            return true;
        }
        else if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

