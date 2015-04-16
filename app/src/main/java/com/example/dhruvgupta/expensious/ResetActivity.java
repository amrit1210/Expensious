package com.example.dhruvgupta.expensious;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import javax.mail.MessagingException;

public class ResetActivity extends ActionBarActivity
{
    Button mReset;
    EditText mResetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        mReset = (Button) findViewById(R.id.reset_btn);
        mResetEmail=(EditText)findViewById(R.id.reset_email);
    }

    public void onReset(View v)
	{
//		Async async = new Async();
//        async.execute();
        ParseUser.requestPasswordResetInBackground(mResetEmail.getText().toString(),
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            Toast.makeText(getApplicationContext(),"Check email to reset password",Toast.LENGTH_SHORT).show();
                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            e.printStackTrace();
                        }
                    }
                });
    }

//    class Async extends AsyncTask<String, String, String>
//	{
//        private ProgressDialog Dialog = new ProgressDialog(ResetActivity.this);
//
//        @Override
//        protected void onPreExecute()
//		{
//            super.onPreExecute();
//            Dialog.setMessage("Please Wait....");
//            Dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params)
//		{
//            try
//			{
//                MailSender mMail = new MailSender();
//                try
//				{
//                    mMail.sendEmail();
//                }
//				catch (MessagingException e)
//				{
//                    e.printStackTrace();
//                }
//			}
//			catch (Exception e)
//			{
//				Log.e("Error: ", e.getMessage());
//			}
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s)
//		{
//            Dialog.dismiss();
//            Toast.makeText(getApplicationContext(),"Thank You",Toast.LENGTH_SHORT).show();
//        }
//    }
}