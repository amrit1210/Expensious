package com.example.dhruvgupta.expensious;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
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
 * Created by Amrit on 3/27/2015.
 */
public class TransactionsActivity extends AbstractNavigationDrawerActivity
{
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        FragmentManager fragmentManager=getFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        TransactionFragment fragment=new TransactionFragment();
//        fragmentTransaction.replace(R.id.container,fragment);
//        fragmentTransaction.commit();
//    }

    public void onInt(Bundle bundle) {
        super.onInt(bundle);

        this.setDefaultStartPositionNavigation(2);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        AccountsFragment fragment = new AccountsFragment();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
    }

    public static class TransactionFragment extends Fragment
    {

        ListView listView;
        ArrayList<TransactionsDB> al;
        TransactionAdapter transactionadapter;
        DBHelper dbHelper;
        SharedPreferences sp;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // layout is inflated here
            return inflater.inflate(R.layout.activity_transactions, container, false);
        }
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView = getView();
            listView = (ListView)rootView.findViewById(R.id.trans_list);

            sp= getActivity().getSharedPreferences("USER_PREFS",MODE_PRIVATE);
            dbHelper =new DBHelper(getActivity());

            al= dbHelper.getAllTransactions(sp.getInt("UID", 0));
            transactionadapter =new TransactionAdapter(getActivity(),R.layout.list_transaction,al);
            listView.setAdapter(transactionadapter);
            registerForContextMenu(listView);
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
                Intent i=new Intent(getActivity(),AddTransactionsActivity.class);
                i.putExtra("t_id",t_id);
                i.putExtra("t_u_id",t_u_id);
                i.putExtra("t_rec_id",t_rec_id);
                i.putExtra("t_date",t_date);
                i.putExtra("t_bal",t_bal);
                i.putExtra("t_note",t_note);
                i.putExtra("t_type",t_type);
                i.putExtra("t_fromAccount",t_fromAccount);
                i.putExtra("t_toAccount",t_toAccount);
                i.putExtra("t_time",t_time);
                i.putExtra("t_category",t_category);
                i.putExtra("t_show",t_show);
                i.putExtra("t_person",t_person);
                i.putExtra("t_subcategory",t_subcategory);
                startActivityForResult(i,1);
            }

            if(id==R.id.Delete)
            {
                if(dbHelper.deleteTransaction(transactionsDB.t_id,sp.getInt("UID",0))>0)
                {
                    String t_type_old = transactionsDB.t_type;
                    int t_from_old = transactionsDB.t_from_acc;
                    int t_to_old = transactionsDB.t_to_acc;
                    float t_amt_old = transactionsDB.t_balance;

                    if (t_type_old.equals("Expense"))
                    {
                        Cursor cursor = dbHelper.getAccountData(t_from_old);
                        cursor.moveToFirst();

                        float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                        String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                        String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                        int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                        int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                        bal = bal + t_amt_old;

                        dbHelper.updateAccountData(t_from_old, name, bal, note, show, uid);
                        cursor.close();
                    }
                    else if (t_type_old.equals("Income"))
                    {
                        Cursor cursor = dbHelper.getAccountData(t_to_old);
                        cursor.moveToFirst();

                        float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                        String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                        String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                        int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                        int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                        bal = bal - t_amt_old;

                        dbHelper.updateAccountData(t_to_old, name, bal, note, show, uid);
                        cursor.close();
                    }
                    else if (t_type_old.equals("Transfer"))
                    {
                        Cursor cursor = dbHelper.getAccountData(t_to_old);
                        cursor.moveToFirst();

                        float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                        String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                        String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                        int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                        int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                        bal = bal - t_amt_old;

                        dbHelper.updateAccountData(t_to_old, name, bal, note, show, uid);
                        cursor.close();

                        Cursor cursor1 = dbHelper.getAccountData(t_from_old);
                        cursor1.moveToFirst();

                        float bal1 = cursor1.getFloat(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                        String name1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                        String note1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                        int show1 = cursor1.getInt(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                        int uid1 = cursor1.getInt(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                        bal1 = bal1 + t_amt_old;

                        dbHelper.updateAccountData(t_from_old, name1, bal1, note1, show1, uid1);
                        cursor1.close();
                    }

                    Intent i = new Intent(getActivity(), TransactionsActivity.class);
                    startActivity(i);
                    Toast.makeText(getActivity(), "Transaction Deleted", Toast.LENGTH_LONG).show();
                }
            }

            return super.onContextItemSelected(item);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 1 && resultCode == 1)
            {
                Intent i = new Intent(getActivity(), TransactionsActivity.class);
                startActivity(i);
            }
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

