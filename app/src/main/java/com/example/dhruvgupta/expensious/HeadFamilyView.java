package com.example.dhruvgupta.expensious;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

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
        ListView member_list;
        ArrayList <SignUpDB> al;
        FamilyMemberAdapter adapter;
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
            member_list=(ListView)rootView.findViewById(R.id.list_member);
            final ParseUser user = ParseUser.getCurrentUser();
            f_id = user.getInt("fid");
            al=new ArrayList<>();
            adapter=new FamilyMemberAdapter(getActivity(), R.layout.list_person, al);
            member_list.setAdapter(adapter);

            ParseQuery<ParseObject>query1= ParseQuery.getQuery("_User");
            query1.whereEqualTo("fid",f_id);
            query1.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {

                    for (ParseObject obj: parseObjects) {
                        SignUpDB usr = new SignUpDB();
                        usr.u_fid = obj.getInt("fid");
                        usr.u_email = obj.getString("email");
                        usr.u_name = obj.getString("uname");
                        usr.u_id = obj.getInt("uid");
                        usr.u_image = obj.getString("userimage");

                        al.add(usr);
                    }
                    adapter.notifyDataSetChanged();
                }
            });



            fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFabAdd(v);
                }
            });
            member_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SignUpDB userDB = adapter.getItem(position);
                    Intent i = new Intent(getActivity(),DetailFamily.class);
                    i.putExtra("User_Name", userDB.u_name);
                    i.putExtra("u_id",userDB.u_id);
                    i.putExtra("u_fid",userDB.u_fid);
                    startActivity(i);
//                    setResult(1, i);
//                    HeadFamilyView.this.finish();
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
                                                //TODO: ACL
                                                ParseACL addAcl = new ParseACL();
                                                addAcl.setPublicReadAccess(true);
                                                addAcl.setPublicWriteAccess(true);
                                                addReq.setACL(addAcl);
                                                addReq.put("uid", uid);
                                                addReq.put("has_request", f_id);
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

