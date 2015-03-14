package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Set;

public class LoginActivity extends ActionBarActivity
{
    EditText mEmail,mPassword;
    CheckBox mRemember;
    UsersDBHelper usersDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail=(EditText)findViewById(R.id.logIn_email);
        mPassword=(EditText)findViewById(R.id.logIn_password);
        mRemember=(CheckBox)findViewById(R.id.logIn_remember);
        usersDBHelper=new UsersDBHelper(LoginActivity.this);
    }

    public void onLogin(View v)
    {
        int id;
        String s;
        ArrayList al1=usersDBHelper.getUserColEmail();
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
            if(al1.contains(mEmail.getText().toString()))
            {
                id=al1.indexOf(mEmail.getText().toString());
                id=id+1;
                Cursor c = usersDBHelper.getUserData(id);
                c.moveToFirst();
                s=c.getString(c.getColumnIndex(usersDBHelper.USER_COL_PASSWORD));
                if(mPassword.getText().toString().equals(s))
                {
                    Toast.makeText(LoginActivity.this,"You are Logged In",Toast.LENGTH_LONG).show();
                }
                else
                {
                Toast.makeText(LoginActivity.this,"Incorrect Password",Toast.LENGTH_LONG).show();
                mPassword.setText(null);
                mPassword.setError("Enter Correct Password");
                }
            }
            else
            {
                Toast.makeText(LoginActivity.this,"Email id not registered",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onForgot(View v)
    {
//        Intent i=new Intent(LoginActivity.this,ResetActivity.class);
//        startActivity(i);
    }
    public void onNewUser(View v)
    {
        Intent i=new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(i);
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

        return super.onOptionsItemSelected(item);
    }
}