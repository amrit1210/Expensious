package com.example.dhruvgupta.expensious;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Amrit on 4/23/2015.
 */
public class SettingsActivity extends AbstractNavigationDrawerActivity
{   static TextView mCurrency;
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

    this.setDefaultStartPositionNavigation(8);
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
    public void onChangePeriod(View v)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        final View layout=inflater.inflate(R.layout.settings_period, null);
        builder.setTitle("PERIOD");
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                RadioGroup period=(RadioGroup)layout.findViewById(R.id.radio_group_period);
                Toast.makeText(SettingsActivity.this,"Period is set",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
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
                mCurrency.setText(R.string.currency+" ( "+cur+" )");
                c.close();
            }
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
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
