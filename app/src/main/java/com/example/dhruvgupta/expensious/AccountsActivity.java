package com.example.dhruvgupta.expensious;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
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

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Gaurav on 13-Mar-15.
 */
public class AccountsActivity extends AbstractNavigationDrawerActivity
{
    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        // set the acitvity's layout containing the FrameLayout for fragment
//        setContentView(R.layout.activity_main);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        AccountsFragment fragment = new AccountsFragment();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
//    }
    public void onInt(Bundle bundle) {
        super.onInt(bundle);

        this.setDefaultStartPositionNavigation(1);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        AccountsFragment fragment = new AccountsFragment();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
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

    public static class AccountsFragment extends Fragment {
        ListView listView;
        TextView mAcc_amt, mAcc_cur;
        float amt=0;
        String curCode;
        ArrayList<AccountsDB> allAccounts;
        ArrayList<CurrencyDB> al;
        AccountsAdapter accountsAdapter;
        AccountsDB accountsDB;
        Iterator<AccountsDB> accountsDBIterator;
        DBHelper dbHelper;
        SharedPreferences sp;

        public AccountsFragment()
        {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // layout is inflated here
            return inflater.inflate(R.layout.activity_accounts, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView = getView();

            // views are now found from rootView defined above
            mAcc_amt = (TextView) rootView.findViewById(R.id.accounts_bal);
            mAcc_cur = (TextView) rootView.findViewById(R.id.accounts_cur);
            listView = (ListView) rootView.findViewById(R.id.accounts_list);

            // use getActivity where "this" or nothing was used
            // means Activity's functions should be called using getActivity()
            sp= getActivity().getSharedPreferences("USER_PREFS",MODE_PRIVATE);
            dbHelper =new DBHelper(getActivity());

            al = new ListOfCurrencies().getAllCurrencies();

            Cursor c = dbHelper.getSettingsData(sp.getInt("UID", 0));
            c.moveToFirst();
            curCode = c.getString(c.getColumnIndex(DBHelper.SETTINGS_COL_CUR_CODE));
            c.close();

            Iterator<CurrencyDB> iterator = al.iterator();
            while (iterator.hasNext())
            {
                CurrencyDB curDB = iterator.next();
                if (curDB.c_code.equals(curCode))
                    mAcc_cur.setText(curDB.c_symbol);
            }

            allAccounts=dbHelper.getAllAccounts(sp.getInt("UID", 0));
            accountsDBIterator =allAccounts.iterator();

            while (accountsDBIterator.hasNext())
            {
                accountsDB = accountsDBIterator.next();
                amt +=accountsDB.acc_balance;
            }
            mAcc_amt.setText(amt+"");

            allAccounts= dbHelper.getAllAccounts(sp.getInt("UID",0));
            // use getActivity where "this" or nothing was used
            // means Activity's functions should be called using getActivity()
            accountsAdapter =new AccountsAdapter(getActivity(),R.layout.list_account,allAccounts);
            listView.setAdapter(accountsAdapter);
            registerForContextMenu(listView);
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
                String acc_note=c.getString(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                int acc_show=c.getInt(c.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                c.close();
                // use getActivity where "this" or nothing was used
                // means Activity's functions should be called using getActivity()
                Intent i=new Intent(getActivity(),AddAccountActivity.class);
                i.putExtra("acc_id",acc_id);
                i.putExtra("acc_uid",acc_uid);
                i.putExtra("acc_name",acc_name);
                i.putExtra("acc_bal",acc_bal);
                i.putExtra("acc_note",acc_note);
                i.putExtra("acc_show",acc_show);
                startActivity(i);
            }

            if(id==R.id.Delete)
            {
                dbHelper.deleteAccount(acc_DB.acc_id,sp.getInt("UID",0));

                ParseObject account = new ParseObject("Accounts");
                account.put("acc_id", acc_DB.acc_id);
                account.put("acc_uid", acc_DB.acc_u_id);
                account.put("acc_name",acc_DB.acc_name);
                account.put("acc_balance", acc_DB.acc_balance);
                account.put("acc_note", acc_DB.acc_note);
                account.put("acc_show", acc_DB.acc_show);
                account.pinInBackground("pinAccountsDelete");
                // use getActivity where "this" or nothing was used
                // means Activity's functions should be called using getActivity()
                Intent i=new Intent(getActivity(),AccountsActivity.class);
                startActivity(i);
                // use getActivity where "this" or nothing was used
                // means Activity's functions should be called using getActivity()
                Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_LONG).show();
            }

            return super.onContextItemSelected(item);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
        {
            super.onCreateContextMenu(menu, v, menuInfo);

            // use getActivity where "this" or nothing was used
            // means Activity's functions should be called using getActivity()
            MenuInflater inflater= getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu,menu);
        }

    }
}
