package com.example.dhruvgupta.expensious;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Amrit on 5/11/2015.
 */
public class MemberViewActivity extends AbstractNavigationDrawerActivity {

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

    public static class MemberViewFragment extends Fragment {

        TextView family_name, head_name, budget, amount_spent, amount_left;
        int u_id, f_id, currentYear, currentMonth, transYear, transMonth;
        float amountspent = 0.0f, amountleft = 0.0f, ubudget = 0.0f;
        Date createdAt, updatedAt, dd;
        SimpleDateFormat sdf;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // layout is inflated here
            return inflater.inflate(R.layout.activity_member_view, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView = getView();
            family_name = (TextView) rootView.findViewById(R.id.family_name);
            head_name = (TextView) rootView.findViewById(R.id.family_head);
            budget = (TextView) rootView.findViewById(R.id.user_budget);
            amount_spent = (TextView) rootView.findViewById(R.id.amount_spent);
            amount_left = (TextView) rootView.findViewById(R.id.amount_left);
            sdf = new SimpleDateFormat("dd-MM-yyyy");

            Calendar cal = Calendar.getInstance();
            currentYear = cal.get(Calendar.YEAR);
            currentMonth = cal.get(Calendar.MONTH);
            Log.i("Current date", cal + " : " + currentMonth + " : " + currentYear);

            ParseUser user = ParseUser.getCurrentUser();
            u_id = user.getInt("uid");
            f_id = user.getInt("fid");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
            query.whereEqualTo("f_id", f_id);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        family_name.setText(parseObject.get("f_name") + "");
                    } else {
                        Log.i("Family Exception:", e.getMessage());
                    }
                }
            });

            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("_User");
            query1.whereEqualTo("uid", f_id);
            query1.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        head_name.setText(parseObject.get("uname") + "");
                    } else {
                        Log.i("Head Exception:", e.getMessage());
                    }
                }
            });

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Member_Budget");
            query2.whereEqualTo("u_id", u_id);
            query2.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        budget.setText(parseObject.get("u_budget") + "");
                        ubudget = Float.parseFloat(parseObject.getDouble("u_budget") + "");
                        Log.i("ubudget", ubudget + "");
                        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Transactions");
                        query3.whereEqualTo("trans_uid", u_id);
                        query3.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                for (ParseObject todo : parseObjects) {
                                    try {
                                        String date = todo.getString("trans_date");
                                        dd = sdf.parse(date);
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(dd);
                                        transYear = calendar.get(Calendar.YEAR);
                                        transMonth = calendar.get(Calendar.MONTH);
                                        Log.i("Trans date", calendar + " : " + transMonth + " : " + transYear);
                                        if (transYear == currentYear) {
                                            if (transMonth == currentMonth) {
                                                float amt = Float.parseFloat(todo.getDouble("trans_balance") + "");
                                                amountspent += amt;
                                            }
                                        }

                                    } catch (java.text.ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                }

                                amount_spent.setText(amountspent + "");
                                amount_left.setText((ubudget - amountspent) + "");
                            }
                        });
                    } else {
                        Log.i("FamilyBudget Exception:", e.getMessage());
                    }
                }
            });
        }
    }

}


