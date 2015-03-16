package com.example.dhruvgupta.expensious;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Gaurav on 3/11/15.
 */
public class SignUpActivity extends ActionBarActivity
{
    EditText mName,mEmail,mPassword,mConfirm_Password;
    UsersDBHelper usersDBHelper;
    ArrayList<SignUpDB> al;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mName=(EditText)findViewById(R.id.signUp_name);
        mEmail=(EditText)findViewById(R.id.signUp_email);
        mPassword=(EditText)findViewById(R.id.signUp_password);
        mConfirm_Password=(EditText)findViewById(R.id.signUp_confirm_password);

        usersDBHelper =new UsersDBHelper(SignUpActivity.this);
        al=new ArrayList<>();

        mName.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    if (mName.length() == 0)
                    {
                        mName.setError("Enter UserName");
                    }
                    else
                    {
                        mName.setError(null);
                    }
                }
            }
        });

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    if (mEmail.length() == 0)
                    {
                        mEmail.setError("Enter Email");
                    }
                    else if (mEmail.length() > 0)
                    {
                        if (Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches())
                        {
                            mEmail.setError(null);
                        }
                        else
                        {
                            mEmail.setError("Invalid Email");
                        }
                    }
                }
            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    if(mPassword.length()==0)
                    {
                        mPassword.setError("Enter Password");
                    }
                    else
                    {
                        if(mPassword.length()<8)
                        {
                            mPassword.setError("Min Password length is 8");
                        }
                        else
                        {
                            mPassword.setError(null);
                        }
                    }
                }
            }
        });

        mConfirm_Password.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    if (mPassword.length() == 0)
                    {
                        mConfirm_Password.setError("Enter Password First");
                        mPassword.setError("Enter Password");
                    }
                    else
                    {
                        if(mConfirm_Password.length()==0)
                        {
                            mConfirm_Password.setError("Enter Confirm Password");
                        }
                        else
                        {
                            mConfirm_Password.setError(null);
                        }
                    }
                    if(mPassword.length()>0 && mPassword.length()<8)
                    {
                        mPassword.setError("Min Password length is 8");
                    }
                }
            }
        });
    }

    public void onSignUp(View v)
    {
        if(!(mName.length()==0 || mEmail.length()==0 || mPassword.length()==0 || mConfirm_Password.length()==0))
        {
            ArrayList al1= usersDBHelper.getUserColEmail();
            if (al1.contains(mEmail.getText().toString()))
            {
                mEmail.setError("Email already exists");
                mEmail.setText(null);
            }

            if(mEmail.getError()==null && mPassword.getError()==null)
            {
                if(mPassword.getText().toString().equals(mConfirm_Password.getText().toString()))
                {
                    if(mName.getError()==null&&mEmail.getError()==null&&mPassword.getError()==null&&mConfirm_Password.getError()==null)
                    {
                        if(usersDBHelper.addUser(mName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString()))
                        {
                            Toast.makeText(SignUpActivity.this,"You are Signed Up!!", Toast.LENGTH_LONG).show();
                            mName.setText(null);
                            mEmail.setText(null);
                            mPassword.setText(null);
                            mConfirm_Password.setText(null);
                        }
                    }
                }
                else
                {
                    Toast.makeText(SignUpActivity.this,"Password and Confirm password must be same",Toast.LENGTH_LONG).show();
                    mConfirm_Password.setError("Confirm Password must be same");
                    mConfirm_Password.setText(null);
                }
            }
        }
        else
        {
            if(mName.length()==0)
            {
                mName.setError("Enter Username");
            }
            if(mEmail.length()==0)
            {
                mEmail.setError("Enter Email");
            }
            if(mPassword.length()==0)
            {
                mPassword.setError("Enter Password");
            }
            if(mConfirm_Password.length()==0)
            {
                mConfirm_Password.setError("Enter Confirm Password");
            }
        }
        al= usersDBHelper.getAllUsers();
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