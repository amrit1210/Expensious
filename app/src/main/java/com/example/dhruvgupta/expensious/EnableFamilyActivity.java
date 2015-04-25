package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by dhruvgupta on 4/25/2015.
 */
public class EnableFamilyActivity extends AbstractNavigationDrawerActivity {

    public void onInt(Bundle bundle) {
        super.onInt(bundle);

        this.setDefaultStartPositionNavigation(6);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        AccountsFragment fragment = new AccountsFragment();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
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

    public static class EnableFamilyFragment extends Fragment {

        SharedPreferences sp;
        EditText mFamilyName;
        LinearLayout mEnableLl;
        Button mEnableBtn, mSaveBtn;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // layout is inflated here
            return inflater.inflate(R.layout.activity_enable, container, false);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView = getView();
            mFamilyName = (EditText) rootView.findViewById(R.id.enable_name);
            mEnableBtn = (Button) rootView.findViewById(R.id.enable_btn);
            mEnableLl = (LinearLayout) rootView.findViewById(R.id.enable_ll);
            mSaveBtn = (Button) rootView.findViewById(R.id.enable_save);

            mEnableBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEnableLl.setVisibility(View.VISIBLE);
                }
            });

            mSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEnableSave(v);
                }
            });
        }

        public void onEnableSave (View view)
        {

        }
    }
}
