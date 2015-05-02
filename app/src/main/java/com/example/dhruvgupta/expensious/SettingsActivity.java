package com.example.dhruvgupta.expensious;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amrit on 4/23/2015.
 */
public class SettingsActivity extends AbstractNavigationDrawerActivity
{   static TextView mCurrency,mA1,mA2,mA3,mA4,mA5,mA6,mA7,mA8,mA9,mA10;
    static DBHelper dbHelper;
    static SharedPreferences sp;

//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        FragmentManager fragmentManager=getFragmentManager();
////        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
////        SettingsFragment settingsFragment=new SettingsFragment();
////        fragmentTransaction.replace(R.id.container,settingsFragment);
////        fragmentTransaction.commit();
//    }
public void onInt(Bundle bundle) {
    super.onInt(bundle);

    this.setDefaultStartPositionNavigation(7);
    this.getSupportActionBar().setTitle("Settings");
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        AccountsFragment fragment = new AccountsFragment();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
}


    public static class SettingsFragment extends Fragment
    {


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            return inflater.inflate(R.layout.activity_settings, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView=getView();
            mCurrency=(TextView)rootView.findViewById(R.id.changeCurrency);

            sp=getActivity().getSharedPreferences("USER_PREFS",MODE_PRIVATE);
            dbHelper=new DBHelper(getActivity());
            Cursor c=dbHelper.getSettingsData(sp.getInt("UID",0));
            c.moveToFirst();
            String cur=c.getString(c.getColumnIndex(DBHelper.SETTINGS_COL_CUR_CODE));
            mCurrency.setText("Change Currency "+" ( "+cur+" )");
            c.close();
        }
    }

    public void onChangeCurrency(View v)
    {
        Intent i=new Intent(SettingsActivity.this,CurrencyViewList.class);
        startActivityForResult(i, 1);

    }

    public void onCustomizeCategory(View v)
    {
        Intent i=new Intent(SettingsActivity.this,CategoriesActivity.class);
        startActivity(i);
    }

    public void onCustomizePerson(View v)
    {
        Intent i=new Intent(SettingsActivity.this,PersonsActivity.class);
        startActivity(i);
    }

    public void onAboutUs(View v){
        LayoutInflater inflater = getLayoutInflater();
        View v1 = inflater.inflate(R.layout.activity_about,null);
        setContentView(v1);
    }

    public void onClearData(View v)
    {
        //deleteAccounts
       ArrayList<AccountsDB> al_account= dbHelper.getAllAccounts(sp.getInt("UID", 0));
        Iterator <AccountsDB> iterator_account=al_account.iterator();
        while(iterator_account.hasNext())
        {
            AccountsDB accountsDB=iterator_account.next();
            dbHelper.deleteAccount(accountsDB.acc_id,sp.getInt("UID",0));
        }
        //deletePerson
        ArrayList<PersonDB> al_person= dbHelper.getAllPersons(sp.getInt("UID", 0));
        Iterator <PersonDB> iterator_person=al_person.iterator();
        while(iterator_person.hasNext())
        {
            PersonDB PersonDB=iterator_person.next();
            dbHelper.deletePerson(PersonDB.p_id,sp.getInt("UID",0));
        }
        //deleteTransaction
        ArrayList<TransactionsDB> al_transaction= dbHelper.getAllTransactions(sp.getInt("UID", 0));
        Iterator <TransactionsDB> iterator_transaction=al_transaction.iterator();
        while(iterator_transaction.hasNext())
        {
            TransactionsDB transactionsDB=iterator_transaction.next();
            dbHelper.deleteTransaction(transactionsDB.t_id,sp.getInt("UID",0));
        }
        //deleteRecursiveTransaction
        ArrayList<RecursiveDB> al_Rec_transaction= dbHelper.getAllRecursive(sp.getInt("UID", 0));
        Iterator <RecursiveDB> iterator_rec_transaction=al_Rec_transaction.iterator();
        while(iterator_rec_transaction.hasNext())
        {
            RecursiveDB recursiveDB=iterator_rec_transaction.next();
            dbHelper.deleteRecursive(recursiveDB.rec_id,sp.getInt("UID",0));
        }
        //deleteBudget
        ArrayList<BudgetDB> al_Budget= dbHelper.getAllBudgets(sp.getInt("UID", 0));
        Iterator <BudgetDB> iterator_Budget=al_Budget.iterator();
        while(iterator_Budget.hasNext())
        {
            BudgetDB budgetDB=iterator_Budget.next();
            dbHelper.deleteBudget(budgetDB.b_id,sp.getInt("UID",0));
        }
        //deleteloanDebt
        ArrayList<LoanDebtDB> al_loandebt= dbHelper.getAllLoanDebt(sp.getInt("UID", 0),0);
        Iterator <LoanDebtDB> iterator_loandebt=al_loandebt.iterator();
        while(iterator_loandebt.hasNext())
        {
            LoanDebtDB loanDebtDB=iterator_loandebt.next();
            dbHelper.deleteLoanDebt(loanDebtDB.l_id,sp.getInt("UID",0));
        }

        Toast.makeText(getApplicationContext(),"Data is cleared",Toast.LENGTH_LONG).show();
    }

    public void onHelp(View v){
        LayoutInflater inflater = getLayoutInflater();
        View v1 = inflater.inflate(R.layout.activity_help,null);
        setContentView(v1);
        mA1 = (TextView) v1.findViewById(R.id.faq1);
        mA2 = (TextView) v1.findViewById(R.id.faq2);
        mA3 = (TextView) v1.findViewById(R.id.faq3);
        mA4 = (TextView) v1.findViewById(R.id.faq4);
        mA5 = (TextView) v1.findViewById(R.id.faq5);
        mA6 = (TextView) v1.findViewById(R.id.faq6);
        mA7 = (TextView) v1.findViewById(R.id.faq7);
        mA8 = (TextView) v1.findViewById(R.id.faq8);
        mA9 = (TextView) v1.findViewById(R.id.faq9);
        mA10 = (TextView) v1.findViewById(R.id.faq10);
        mA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mA2.getVisibility()==View.VISIBLE){
                    mA2.setVisibility(View.INVISIBLE);
                }
                else{
                    mA2.setVisibility(View.VISIBLE);
                }
            }
        });
        mA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mA4.getVisibility()==View.VISIBLE){
                    mA4.setVisibility(View.INVISIBLE);
                }
                else{
                    mA4.setVisibility(View.VISIBLE);
                }
            }
        });
        mA5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mA6.getVisibility()==View.VISIBLE){
                    mA6.setVisibility(View.INVISIBLE);
                }
                else{
                    mA6.setVisibility(View.VISIBLE);
                }
            }
        });
        mA7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mA8.getVisibility()==View.VISIBLE){
                    mA8.setVisibility(View.INVISIBLE);
                }
                else{
                    mA8.setVisibility(View.VISIBLE);
                }
            }
        });
        mA9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mA10.getVisibility()==View.VISIBLE){
                    mA10.setVisibility(View.INVISIBLE);
                }
                else{
                    mA10.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==1)
            {
                Cursor c=dbHelper.getSettingsData(sp.getInt("UID",0));
                c.moveToFirst();
                String cur=c.getString(c.getColumnIndex(DBHelper.SETTINGS_COL_CUR_CODE));
                if(dbHelper.updateSettings(sp.getInt("UID",0),cur))
                {
                    mCurrency.setText("Change Currency" + " ( " + cur + " )");
                    ParseObject settings = new ParseObject("Settings");
                    settings.put("settings_uid", sp.getInt("UID", 0));
                    settings.put("settings_cur_code", cur);
                    settings.pinInBackground("pinSettingsUpdate");
                    c.close();
                }
            }
        }
    }

}
