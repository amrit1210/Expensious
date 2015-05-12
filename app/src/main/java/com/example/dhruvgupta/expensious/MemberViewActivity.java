package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Amrit on 5/11/2015.
 */
public class MemberViewActivity extends ActionBarActivity {
    TextView family_name,head_name,budget,amount_spent,amount_left;
    int u_id,f_id;
    double amount=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_view);
        family_name=(TextView)findViewById(R.id.family_name);
        head_name=(TextView)findViewById(R.id.family_head);
        budget=(TextView)findViewById(R.id.user_budget);
        amount_spent=(TextView)findViewById(R.id.amount_spent);
        amount_left=(TextView)findViewById(R.id.amount_left);

        ParseUser user = ParseUser.getCurrentUser();
        u_id=user.getInt("uid");
        f_id=user.getInt("fid");

        ParseQuery<ParseObject>query=ParseQuery.getQuery("Family");
        query.whereEqualTo("f_id",f_id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e==null)
                {
                    family_name.setText(parseObject.get("f_name")+"");
                }
                else
                {
                    Log.i("Family Exception:",e.getMessage());
                }
            }
        });
        ParseQuery<ParseObject>query1=ParseQuery.getQuery("_User");
        query1.whereEqualTo("uid",f_id);
        query1.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e==null)
                {
                    head_name.setText(parseObject.get("uname")+"");
                }
                else
                {
                    Log.i("Head Exception:",e.getMessage());
                }
            }
        });
        ParseQuery<ParseObject>query2=ParseQuery.getQuery("Member_Budget");
        query2.whereEqualTo("u_id",u_id);
        query2.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e==null)
                {
                    budget.setText(parseObject.get("u_budget")+"");
                }
                else
                {
                    Log.i("FamilyBudget Exception:",e.getMessage());
                }
            }
        });


    }

}


