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
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by dhruvgupta on 5/7/2015.
 */
public class AcceptFamilyActivity extends AbstractNavigationDrawerActivity {

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

    public static class AcceptFamilyFragment extends Fragment {

        SharedPreferences sp;
        Button mAccept;
        int req;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // layout is inflated here
            return inflater.inflate(R.layout.activity_accept, container, false);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView = getView();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Family_request");
            query.whereEqualTo("uid", sp.getInt("UID", 0));
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    req = parseObject.getInt("has_request");
                }
            });

            mAccept = (Button) rootView.findViewById(R.id.accept_btn);
            mAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAcceptClick(v);
                }
            });
        }

        public void onAcceptClick (View view)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

            View layout = layoutInflater.inflate(R.layout.dialog_accept, null);
            builder.setView(layout);
            builder.setTitle("Accept Request");

            final TextView family = (TextView) layout.findViewById(R.id.accept_family);
            final TextView head = (TextView) layout.findViewById(R.id.accept_head);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
            query.whereEqualTo("f_id", req);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null)
                    {
                        family.setText(parseObject.getString("f_name"));
                    }
                }
            });

            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("_User");
            query1.whereEqualTo("uid", req);
            query1.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        head.setText(parseObject.getString("uname"));
                    }
                }
            });

            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("fid", req);
                    user.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "User updated", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Family_request");
                    query.whereEqualTo("uid", sp.getInt("UID", 0));
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            parseObject.deleteEventually(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null)
                                        Toast.makeText(getActivity(), "Delete Request", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });
            builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Family_request");
                    query.whereEqualTo("uid", sp.getInt("UID", 0));
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            parseObject.deleteEventually(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null)
                                        Toast.makeText(getActivity(), "Delete Request", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });
            builder.create();
            builder.show();
        }
    }
}


