package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by dhruvgupta on 4/25/2015.
 */
public class InviteFamilyActivity extends AbstractNavigationDrawerActivity {

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

    public static class InviteFamilyFragment extends Fragment {

        SharedPreferences sp;
        EditText mEmail;
        LinearLayout mInviteLl;
        Button mInviteBtn, mAddBtn, mNext;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // layout is inflated here
            return inflater.inflate(R.layout.activity_invite, container, false);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView = getView();
            mEmail = (EditText) rootView.findViewById(R.id.invite_name);
            mInviteBtn = (Button) rootView.findViewById(R.id.invite_btn);
            mInviteLl = (LinearLayout) rootView.findViewById(R.id.invite_ll);
            mAddBtn = (Button) rootView.findViewById(R.id.invite_add);
            mNext = (Button) rootView.findViewById(R.id.invite_next);

            mInviteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mInviteLl.setVisibility(View.VISIBLE);
                }
            });

            mAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInviteAdd(v);
                }
            });
        }

        public void onInviteAdd (View view)
        {
            ParseQuery<ParseUser> invite = ParseQuery.getQuery("_User");

        }
    }
}

