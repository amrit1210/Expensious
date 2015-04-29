package com.example.dhruvgupta.expensious;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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


import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;

public class LoanDebtActivity extends AbstractNavigationDrawerActivity
{
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
////        FragmentManager fragmentManager=getFragmentManager();
////        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
////        LoanDebtFragment loanDebtFragment=new LoanDebtFragment();
////        fragmentTransaction.replace(R.id.container,loanDebtFragment);
////        fragmentTransaction.commit();
//
//    }

    public void onInt(Bundle bundle) {
        super.onInt(bundle);

        this.setDefaultStartPositionNavigation(5);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        AccountsFragment fragment = new AccountsFragment();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
    }


    public static class LoanDebtFragment extends Fragment
    {
        ListView listView;
        ArrayList<LoanDebtDB> al;
        LoanDebtAdapter loanDebtAdapter;
        DBHelper dbHelper;
        SharedPreferences sp;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            return inflater.inflate(R.layout.activity_transactions,container,false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView=getView();

            listView = (ListView)rootView.findViewById(R.id.trans_list);
            FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);

            sp= getActivity().getSharedPreferences("USER_PREFS",MODE_PRIVATE);
            dbHelper =new DBHelper(getActivity());

            al= dbHelper.getAllLoanDebt(sp.getInt("UID", 0), 0);
            loanDebtAdapter =new LoanDebtAdapter(getActivity(),R.layout.list_loan_debt,al);
            listView.setAdapter(loanDebtAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LoanDebtDB recDb = (LoanDebtDB) loanDebtAdapter.getItem(position);
                    Intent i = new Intent(getActivity(), AddLoanDebtActivity.class);
                    i.putExtra("LD_ID", recDb.l_id);
                    startActivity(i);
                }
            });
            registerForContextMenu(listView);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(getActivity(),AddLoanDebtActivity.class);
                    startActivity(i);

                }
            });
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
                Intent i = new Intent(getActivity(), AddLoanDebtActivity.class);
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
                if (dbHelper.deleteLoanDebt(loanDebtDB.l_id, sp.getInt("UID", 0)) > 0) {
                    String l_type_old = loanDebtDB.l_type;
                    int l_to_old = loanDebtDB.l_to_acc;
                    int l_id = loanDebtDB.l_id;
                    int l_from_old = loanDebtDB.l_from_acc;
                    float l_amt_old = loanDebtDB.l_balance;

                    if (l_type_old.equals("Loan"))
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

                        ArrayList<LoanDebtDB> arrayList = dbHelper.getAllLoanDebt(sp.getInt("UID", 0), l_id);
                        Iterator <LoanDebtDB> iterator = null;
                        if (!arrayList.isEmpty())
                        {
                            iterator = arrayList.iterator();
                            while (iterator.hasNext())
                            {
                                LoanDebtDB ldDB = iterator.next();
                                Cursor c = dbHelper.getAccountData(ldDB.l_to_acc);
                                c.moveToFirst();
                                if (dbHelper.deleteLoanDebt(ldDB.l_id, sp.getInt("UID", 0)) > 0) {

                                    float bal1 = c.getFloat(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                                    String name1 = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                                    String note1 = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                                    int show1 = c.getInt(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                                    int uid1 = c.getInt(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                                    bal1 = bal1 - ldDB.l_balance;

                                    dbHelper.updateAccountData(ldDB.l_to_acc, name1, bal1, note1, show1, uid1);
                                    c.close();
                                }
                            }
                        }

                    }
                    else if (l_type_old.equals("Debt"))
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

                        ArrayList<LoanDebtDB> arrayList = dbHelper.getAllLoanDebt(sp.getInt("UID", 0), l_id);

                        Iterator <LoanDebtDB> iterator = null;
                        if (!arrayList.isEmpty())
                        {
                            iterator = arrayList.iterator();
                            while (iterator.hasNext())
                            {
                                LoanDebtDB ldDB = iterator.next();
                                Cursor c = dbHelper.getAccountData(ldDB.l_from_acc);
                                c.moveToFirst();
                                Log.i("While", "inside while debt");

                                if (dbHelper.deleteLoanDebt(ldDB.l_id, sp.getInt("UID", 0)) > 0) {
                                    Log.i("If While", "inside if while debt");
                                    float bal1 = c.getFloat(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                                    String name1 = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                                    String note1 = c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                                    int show1 = c.getInt(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                                    int uid1 = c.getInt(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                                    bal1 = bal1 + ldDB.l_balance;

                                    dbHelper.updateAccountData(ldDB.l_from_acc, name1, bal1, note1, show1, uid1);
                                    c.close();
                                }
                            }
                        }

                    }

                    Intent i = new Intent(getActivity(), LoanDebtActivity.class);
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


}
