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
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dhruvgupta on 4/9/2015.
 */
public class DetailedRecursive extends ActionBarActivity
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
        DetailedRecursiveFragment fragment = new DetailedRecursiveFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }
    public static class DetailedRecursiveFragment extends Fragment
    {
        ListView listView;
        ArrayList<TransactionsDB> al;
        ArrayList<CurrencyDB> al1;
        String curCode;
        DBHelper dbHelper;
        TransactionAdapter transactionAdapter;
        TextView s_date, e_date, amt, cur;
        SharedPreferences sp;
        int rec_id;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             super.onCreateView(inflater, container, savedInstanceState);
            return inflater.inflate(R.layout.detailed_recursive,container,false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView=getView();

            s_date = (TextView)rootView.findViewById(R.id.detailed_rec_start_date);
            e_date = (TextView)rootView.findViewById(R.id.detailed_rec_end_date);
            amt = (TextView) rootView.findViewById(R.id.detailed_rec_amt);
            cur = (TextView)rootView. findViewById(R.id.detailed_rec_cur);
            listView = (ListView)rootView. findViewById(R.id.detailed_rec_list);
            FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);

            sp = getActivity().getSharedPreferences("USER_PREFS", MODE_PRIVATE);
            dbHelper =new DBHelper(getActivity());

            rec_id = getActivity().getIntent().getIntExtra("REC_ID", 0);
            Cursor c = dbHelper.getRecursiveData(rec_id);
            c.moveToFirst();
            s_date.setText(c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_START_DATE)));
            e_date.setText(c.getString(c.getColumnIndex(DBHelper.RECURSIVE_COL_END_DATE)));
            amt.setText(c.getFloat(c.getColumnIndex(DBHelper.RECURSIVE_COL_BALANCE))+"");
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
                    cur.setText(curDB.c_symbol);
            }


            al= dbHelper.getAllRecTrans(sp.getInt("UID", 0), rec_id);
            transactionAdapter =new TransactionAdapter(getActivity(),R.layout.list_transaction,al);
            listView.setAdapter(transactionAdapter);
            registerForContextMenu(listView);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(getActivity(),AddTransactionsActivity.class);
                    startActivity(i);

                }
            });
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
                Intent i=new Intent(getActivity(), AddTransactionsActivity.class);
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
                startActivityForResult(i, 1);
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

                    Intent i = new Intent(getActivity(), DetailedRecursive.class);
                    i.putExtra("REC_ID", rec_id);
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
                rec_id = data.getExtras().getInt("REC_ID");
                Intent i = new Intent(getActivity(), DetailedRecursive.class);
                i.putExtra("REC_ID", rec_id);
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
