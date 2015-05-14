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

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Amrit on 4/23/2015.
 */
public class SettingsActivity extends AbstractNavigationDrawerActivity
{   static TextView mCurrency,mA1,mA2,mA3,mA4,mA5,mA6,mA7,mA8,mA9,mA10;
    static DBHelper dbHelper;
    static SharedPreferences sp;
    int clearData=0;
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

    public void onClearData(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Delete local data");
        builder.setMessage("All your local data will be cleared, and can be restored");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteAllData(sp.getInt("UID", 0));
                Toast.makeText(getApplicationContext(), "Data is cleared", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    public void onClearAllData(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Delete all data");
        builder.setMessage("All your data will be cleared, and can never be restored");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteAllData(sp.getInt("UID", 0));

                final ParseQuery<ParseObject> accounts = ParseQuery.getQuery("Accounts");
                accounts.whereEqualTo("acc_uid", sp.getInt("UID", 0));
                accounts.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                parseObject.deleteEventually();
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> budgets = ParseQuery.getQuery("Budgets");
                budgets.whereEqualTo("b_uid", sp.getInt("UID", 0));
                budgets.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                parseObject.deleteEventually();
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> transactions = ParseQuery.getQuery("Transactions");
                transactions.whereEqualTo("trans_uid", sp.getInt("UID", 0));
                transactions.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                parseObject.deleteEventually();
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> recursive = ParseQuery.getQuery("Recursive");
                recursive.whereEqualTo("rec_uid", sp.getInt("UID", 0));
                recursive.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                parseObject.deleteEventually();
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> persons = ParseQuery.getQuery("Persons");
                persons.whereEqualTo("p_uid", sp.getInt("UID", 0));
                persons.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                parseObject.deleteEventually();
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> category_specific = ParseQuery.getQuery("Category_specific");
                category_specific.whereEqualTo("c_uid", sp.getInt("UID", 0));
                category_specific.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                parseObject.deleteEventually();
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> sub_category = ParseQuery.getQuery("Sub_category");
                sub_category.whereEqualTo("sub_uid", sp.getInt("UID", 0));
                sub_category.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                parseObject.deleteEventually();
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> loan_debt = ParseQuery.getQuery("Loan_debt");
                loan_debt.whereEqualTo("loan_debt_uid", sp.getInt("UID", 0));
                loan_debt.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                parseObject.deleteEventually();
                            }
                        }
                    }
                });

                Toast.makeText(getApplicationContext(),"Data is cleared",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    public void onRestoreData(View v)
    {
        dbHelper.deleteAllData(sp.getInt("UID", 0));
        final ParseQuery<ParseObject> accounts = ParseQuery.getQuery("Accounts");
        accounts.whereEqualTo("acc_uid", sp.getInt("UID", 0));
        accounts.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                for (final ParseObject parseObject : parseObjects) {
                    if (e == null) {
                        int acc_id = parseObject.getInt("acc_id");
                        String acc_name = parseObject.getString("acc_name");
                        float acc_bal = Float.parseFloat(parseObject.getDouble("acc_balance")+"");
                        int show = parseObject.getInt("acc_show");
                        String note = parseObject.getString("acc_note");
                        if (dbHelper.addAccount(sp.getInt("UID", 0), acc_id, acc_name, acc_bal, note, show)) {
                            Log.i("Account added :", acc_id + "");
                        }
                    }
                }
            }
        });

        final ParseQuery<ParseObject> budgets = ParseQuery.getQuery("Budgets");
        budgets.whereEqualTo("b_uid", sp.getInt("UID", 0));
        budgets.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                for (final ParseObject parseObject : parseObjects) {
                    if (e == null) {
                        int b_id = parseObject.getInt("b_id");
                        String b_startDate = parseObject.getString("b_startDate");
                        String b_endDate = parseObject.getString("b_endDate");
                        float b_amount = Float.parseFloat(parseObject.getDouble("b_amount") + "");
                        if (dbHelper.addBudget(sp.getInt("UID", 0), b_id, b_amount, b_startDate, b_endDate)) {
                            Log.i("Budget added :", b_id + "");
                        }
                    }
                }
            }
        });

        final ParseQuery<ParseObject> transactions = ParseQuery.getQuery("Transactions");
        transactions.whereEqualTo("trans_uid", sp.getInt("UID", 0));
        transactions.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                for (final ParseObject parseObject : parseObjects) {
                    if (e == null) {
                        int trans_id = parseObject.getInt("trans_id");
                        int trans_rec_id = parseObject.getInt("trans_rec_id");
                        int trans_from_acc = parseObject.getInt("trans_from_acc");
                        int trans_to_acc = parseObject.getInt("trans_to_acc");
                        int trans_show = parseObject.getInt("trans_show");
                        String trans_date = parseObject.getString("trans_date");
                        String trans_time = parseObject.getString("trans_time");
                        String trans_note = parseObject.getString("trans_note");
                        String trans_type = parseObject.getString("trans_type");
                        int trans_category = parseObject.getInt("trans_category");
                        int trans_subcategory = parseObject.getInt("trans_subcategory");
                        int trans_person = parseObject.getInt("trans_person");
                        float trans_balance = Float.parseFloat(parseObject.getDouble("trans_balance") + "");
                        if (dbHelper.addTransaction(sp.getInt("UID", 0), trans_id, trans_from_acc, trans_to_acc, trans_balance,
                                trans_note, trans_person, trans_category, trans_subcategory, trans_show, trans_type, trans_date,
                                trans_time, trans_rec_id)) {
                            Log.i("Transactions added :", trans_id + "");
                        }
                    }
                }
            }
        });

        final ParseQuery<ParseObject> recursive = ParseQuery.getQuery("Recursive");
        recursive.whereEqualTo("rec_uid", sp.getInt("UID", 0));
        recursive.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                for (final ParseObject parseObject : parseObjects) {
                    if (e == null) {
                        int rec_id = parseObject.getInt("rec_id");
                        int rec_from_acc = parseObject.getInt("rec_from_acc");
                        int rec_to_acc = parseObject.getInt("rec_to_acc");
                        int rec_show = parseObject.getInt("rec_show");
                        String rec_start_date = parseObject.getString("rec_start_date");
                        String rec_end_date = parseObject.getString("rec_end_date");
                        String rec_next_date = parseObject.getString("rec_next_date");
                        String rec_time = parseObject.getString("rec_time");
                        String rec_note = parseObject.getString("rec_note");
                        String rec_type = parseObject.getString("rec_type");
                        int rec_recurring = parseObject.getInt("rec_recurring");
                        int rec_alert = parseObject.getInt("rec_alert");
                        int rec_category = parseObject.getInt("rec_category");
                        int rec_subcategory = parseObject.getInt("rec_subcategory");
                        int rec_person = parseObject.getInt("rec_person");
                        float rec_balance = Float.parseFloat(parseObject.getDouble("rec_balance") + "");
                        if (dbHelper.addRecursive(sp.getInt("UID", 0), rec_id, rec_from_acc, rec_to_acc, rec_balance,
                                rec_note, rec_person, rec_category, rec_subcategory, rec_show, rec_type, rec_start_date,
                                rec_end_date, rec_next_date, rec_time, rec_recurring, rec_alert)) {
                            Log.i("Recursive added :", rec_id + "");
                        }
                    }
                }
            }
        });

        final ParseQuery<ParseObject> persons = ParseQuery.getQuery("Persons");
        persons.whereEqualTo("p_uid", sp.getInt("UID", 0));
        persons.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                for (final ParseObject parseObject : parseObjects) {
                    if (e == null) {
                        int p_id = parseObject.getInt("p_id");
                        String p_name = parseObject.getString("p_name");
                        String p_color = parseObject.getString("p_color");
                        String p_color_code = parseObject.getString("p_color_code");
                        if (dbHelper.addPerson(p_name, p_id, p_color, p_color_code, sp.getInt("UID", 0))) {
                            Log.i("Person added :", p_id + "");
                        }
                    }
                }
            }
        });

        final ParseQuery<ParseObject> category_specific = ParseQuery.getQuery("Category_specific");
        category_specific.whereEqualTo("c_uid", sp.getInt("UID", 0));
        category_specific.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                for (final ParseObject parseObject : parseObjects) {
                    if (e == null) {
                        int c_id = parseObject.getInt("c_id");
                        int c_type = parseObject.getInt("c_type");
                        String c_name = parseObject.getString("c_name");
                        String c_icon = parseObject.getString("c_icon");
                        if (dbHelper.addCategorySpecific(sp.getInt("UID", 0), c_id, c_name, c_type, c_icon)) {
                            Log.i("Category added :", c_id + "");
                        }
                    }
                }
            }
        });

        final ParseQuery<ParseObject> sub_category = ParseQuery.getQuery("Sub_category");
        sub_category.whereEqualTo("sub_uid", sp.getInt("UID", 0));
        sub_category.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                for (final ParseObject parseObject : parseObjects) {
                    if (e == null) {
                        int sub_id = parseObject.getInt("sub_id");
                        int sub_c_id = parseObject.getInt("sub_c_id");
                        String sub_name = parseObject.getString("sub_name");
                        String sub_icon = parseObject.getString("sub_icon");
                        if (dbHelper.addSubCategory(sub_c_id, sub_id, sub_name, sp.getInt("UID", 0), sub_icon)) {
                            Log.i("SubCategory added :", sub_id + "");
                        }
                    }
                }
            }
        });

        final ParseQuery<ParseObject> loan_debt = ParseQuery.getQuery("Loan_debt");
        loan_debt.whereEqualTo("loan_debt_uid", sp.getInt("UID", 0));
        loan_debt.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                for (final ParseObject parseObject : parseObjects) {
                    if (e == null) {
                        int loan_debt_id = parseObject.getInt("loan_debt_id");
                        int loan_debt_parent = parseObject.getInt("loan_debt_parent");
                        int loan_debt_from_acc = parseObject.getInt("loan_debt_from_acc");
                        int loan_debt_to_acc = parseObject.getInt("loan_debt_to_acc");
                        String loan_debt_date = parseObject.getString("loan_debt_date");
                        String loan_debt_time = parseObject.getString("loan_debt_time");
                        String loan_debt_note = parseObject.getString("loan_debt_note");
                        String loan_debt_type = parseObject.getString("loan_debt_type");
                        int loan_debt_person = parseObject.getInt("loan_debt_person");
                        float loan_debt_balance = Float.parseFloat(parseObject.getDouble("loan_debt_balance") + "");
                        if (dbHelper.addLoanDebt(sp.getInt("UID", 0), loan_debt_id, loan_debt_balance, loan_debt_date, loan_debt_time,
                                loan_debt_from_acc, loan_debt_to_acc, loan_debt_person, loan_debt_note, loan_debt_type, loan_debt_parent)) {
                            Log.i("LoanDebt added :", loan_debt_id + "");
                        }
                    }
                }
            }
        });

//        final ParseQuery<ParseObject> settings = ParseQuery.getQuery("Settings");
//        settings.whereEqualTo("settings_uid", sp.getInt("UID", 0));
//        settings.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
//                for (final ParseObject parseObject : parseObjects) {
//                    if (e == null) {
//                        String settings_cur_code = parseObject.getString("settings_cur_code");
//                        ArrayList al = dbHelper.getSettingsUid();
//                        if (! al.contains(sp.getInt("UID", 0)))
//                            if (dbHelper.addSettings(sp.getInt("UID", 0), settings_cur_code)) {
//                                Log.i("Settings added :", settings_cur_code);
//                            }
//                    }
//                }
//            }
//        });
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
                    mA2.setVisibility(View.GONE);
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
                    mA4.setVisibility(View.GONE);
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
                    mA6.setVisibility(View.GONE);
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
                    mA8.setVisibility(View.GONE);
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
                    mA10.setVisibility(View.GONE);
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
