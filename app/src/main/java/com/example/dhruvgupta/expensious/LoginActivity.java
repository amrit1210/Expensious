package com.example.dhruvgupta.expensious;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity
{
    EditText mEmail,mPassword;
    CheckBox mRemember;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail=(EditText)findViewById(R.id.logIn_email);
        mPassword=(EditText)findViewById(R.id.logIn_password);
        mRemember=(CheckBox)findViewById(R.id.logIn_remember);
		dbHelper =new DBHelper(LoginActivity.this);
        CategoryDB_Master categoryDB_master=new CategoryDB_Master();
    }

    public void onLogin(View v)
    {
        int id;
        String s;
        if(mEmail.length()==0)
        {
            mEmail.setError("Enter Email");
        }
        if(mPassword.length()==0)
        {
            mPassword.setError("Enter Password");
        }
        if (mEmail.length() != 0 || mPassword.length() != 0)
        {
//            Async async = new Async();
//            async.execute();
            final ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
            Dialog.setMessage("Please Wait");
            Dialog.show();
            ParseUser.logInInBackground(mEmail.getText().toString(), mPassword.getText().toString(), new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    Dialog.dismiss();
                    if (user != null)
                    {
                        if (user.getBoolean("emailVerified")) {
                            // Hooray! The user is logged in.
                            SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFS",MODE_PRIVATE);
                            SharedPreferences.Editor spEdit = sharedPreferences.edit();
                            spEdit.putString("EMAIL",user.getEmail());
                            spEdit.putString("SESSION",user.getSessionToken());
                            spEdit.putInt("UID", user.getInt("uid"));
                            spEdit.commit();

                            ArrayList al = dbHelper.getSettingsUid();
                            if (! al.contains(sharedPreferences.getInt("UID", 0)))
                                dbHelper.addSettings(sharedPreferences.getInt("UID", 0), "INR");

                            Toast.makeText(LoginActivity.this,"You are Logged In "+sharedPreferences.getInt("UID",1110),
                                    Toast.LENGTH_LONG).show();
//                            Intent i=new Intent(LoginActivity.this,AddAccountActivity.class);
//                            Intent i=new Intent(LoginActivity.this,AddTransactionsActivity.class);
//                            Intent i =new Intent(LoginActivity.this,AddCategoryActivity.class);
                            //Intent i=new Intent(LoginActivity.this,AddPersonActivity.class);
                            Intent i=new Intent(LoginActivity.this,AddRecursiveActivity.class);
//                            Intent i=new Intent(LoginActivity.this,AddBudgetActivity.class);
//                                Intent i=new Intent(LoginActivity.this,AddLoanDebtActivity.class);
                            startActivity(i);
                        } else {
                            // Login failed. Look at the ParseException to see what happened.
                            Toast.makeText(LoginActivity.this, "Email not Verified", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

//            if(al1.contains(mEmail.getText().toString()))
//            {
//                id=al1.indexOf(mEmail.getText().toString());
//                id=id+1;
//                Cursor c = dbHelper.getUserData(id);
//                c.moveToFirst();
//                s=c.getString(c.getColumnIndex(DBHelper.USER_COL_PASSWORD));
//                if(mPassword.getText().toString().equals(s))
//                {
//                    SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFS",MODE_PRIVATE);
//                    SharedPreferences.Editor spEdit = sharedPreferences.edit();
//                    spEdit.putString("EMAIL",mEmail.getText().toString());
//                    spEdit.putString("PASSWORD",mPassword.getText().toString());
//                    spEdit.putInt("UID", dbHelper.getUserColId(mEmail.getText().toString()));
//                    spEdit.commit();
//                    Toast.makeText(LoginActivity.this,"You are Logged In "+sharedPreferences.getInt("UID",1110),
//                            Toast.LENGTH_LONG).show();
////                    Intent i=new Intent(LoginActivity.this,AddAccountActivity.class);
////                    Intent i=new Intent(LoginActivity.this,AddTransactionsActivity.class);
////                    Intent i=new Intent(LoginActivity.this,AddPersonActivity.class);
////                    Intent i=new Intent(LoginActivity.this,AddRecursiveActivity.class);
////                    Intent i=new Intent(LoginActivity.this,AddBudgetActivity.class);
//                    Intent i=new Intent(LoginActivity.this,AddLoanDebtActivity.class);
//                    startActivity(i);
//                }
//                else
//                {
//                    Toast.makeText(LoginActivity.this,"Incorrect Password",Toast.LENGTH_LONG).show();
//                    mPassword.setText(null);
//                    mPassword.setError("Enter Correct Password");
//                }
//            }
//            else
//            {
//                Toast.makeText(LoginActivity.this,"Email id not registered",Toast.LENGTH_LONG).show();
//            }
        }
    }

    public void onForgot(View v)
    {
        Intent i=new Intent(LoginActivity.this,ResetActivity.class);
        startActivity(i);
    }
    public void onNewUser(View v)
    {
        Intent i=new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(i);
    }

    class Async extends AsyncTask<String, String, String>
    {
        private ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Dialog.setMessage("Please Wait....");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {

            }
            catch (Exception e)
            {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            Dialog.dismiss();

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
        else if(id == R.id.action_acc)
        {
            Intent i =new Intent(this, AddAccountActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_person)
        {
            Intent i =new Intent(this, AddPersonActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_trans)
        {
            Intent i =new Intent(this, AddTransactionsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}