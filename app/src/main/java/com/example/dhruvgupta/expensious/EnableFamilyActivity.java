package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

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
        this.getSupportActionBar().setTitle("Family Sharing");
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
            sp = getActivity().getSharedPreferences("USER_PREFS", MODE_PRIVATE);

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

        public void onEnableSave (View v)
        {
            ParseObject family = new ParseObject("Family");
            family.put("f_id", sp.getInt("UID", 0));
            family.put("h_id", sp.getInt("UID", 0));
            family.put("f_name", mFamilyName.getText().toString());
            family.pinInBackground("pinFamily");
            family.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Family Added", Toast.LENGTH_LONG).show();
                    }
                }
            });

            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("uid", sp.getInt("UID", 0));
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("fid", sp.getInt("UID", 0));
                    user.put("is_head", 1);
                    user.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "User Updated", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getActivity(), HeadFamilyView.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                            else
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        );
                    }
                }

                );
            }
        }
}
