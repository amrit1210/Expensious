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
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by dhruvgupta on 4/25/2015.
 */
public class EnableFamilyActivity extends AbstractNavigationDrawerActivity {

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
            ParseObject family = new ParseObject("Family");
            family.put("f_id", sp.getInt("UID", 0));
            family.put("h_id", sp.getInt("UID", 0));
            family.put("f_name", mFamilyName.getText().toString());
            family.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                    {
                        Toast.makeText(getActivity(), "Family Added", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), InviteFamilyActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                }
            });
        }
    }
}
