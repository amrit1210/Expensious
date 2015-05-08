package com.example.dhruvgupta.expensious;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.melnykov.fab.FloatingActionButton;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by dhruvgupta on 4/25/2015.
 */
public class HeadFamilyView extends AbstractNavigationDrawerActivity {

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

    public static class HeadFamilyViewFragment extends Fragment {

        SharedPreferences sp;
        FloatingActionButton fab;
        int f_id;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // layout is inflated here
            return inflater.inflate(R.layout.activity_head, container, false);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView = getView();

            ParseUser user = ParseUser.getCurrentUser();
            f_id = user.getInt("fid");

            fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFabAdd(v);
                }
            });
        }

        public void onFabAdd (View view)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

            View layout = layoutInflater.inflate(R.layout.dialog_invite, null);
            builder.setView(layout);
            builder.setTitle("Invite Members");

            final EditText mEmail = (EditText)layout.findViewById(R.id.invite_email);

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (! mEmail.getText().toString().matches("[a-zA-Z0-9\\.\\_]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9]{0,64}" +
                            "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"))
                    {
                        Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                        query.whereEqualTo("email", mEmail.getText().toString());
                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (parseObject == null)
                                {
                                    Toast.makeText(getActivity(), "Email not registered with Expensious", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    final int fid = parseObject.getInt("fid");
                                    final int uid = parseObject.getInt("uid");

                                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Family_request");
                                    query1.whereEqualTo("uid", uid);
                                    query1.getFirstInBackground(new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject parseObject, ParseException e) {
                                            int has_req;
                                            if (parseObject == null)
                                                has_req = 0;
                                            else
                                                has_req = parseObject.getInt("has_request");

                                            if (fid != 0 || has_req != 0)
                                                Toast.makeText(getActivity(), "User already a part of another family", Toast.LENGTH_LONG).show();
                                            else
                                            {
                                                ParseObject addReq = new ParseObject("Family_request");
                                                ParseACL addAcl = new ParseACL();
                                                addAcl.setPublicReadAccess(true);
                                                addAcl.setPublicWriteAccess(true);
                                                addReq.put("uid", uid);
                                                addReq.put("has_request", f_id);
                                                addReq.setACL(addAcl);
                                                addReq.pinInBackground("pinRequest");
                                                addReq.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Toast.makeText(getActivity(), "User updated", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                                Toast.makeText(getActivity(), "Request sent", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                                }

                            }
                        });
                    }
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
    }
}

