package com.example.dhruvgupta.expensious;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity
{
    EditText mEmail,mPassword;
    DBHelper dbHelper;
    SharedPreferences sp;
//    ImageView tutImage;
//    AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("USER_PREFS",MODE_PRIVATE);

        if (sp.getInt("UID", 0) == 0)
            setContentView(R.layout.activity_login);
        else
        {
            Intent i = new Intent(LoginActivity.this, PieChartActivity.class);
            startActivity(i);
        }
//        tutImage = (ImageView) findViewById(R.id.img_tut);
        mEmail=(EditText)findViewById(R.id.logIn_email);
        mPassword=(EditText)findViewById(R.id.logIn_password);
//        mEmail.setSelected(false);
//        mPassword.setSelected(false);
//        tutImage.setBackgroundResource(R.drawable.tutorial);
//        anim = (AnimationDrawable) tutImage.getBackground();
        dbHelper =new DBHelper(LoginActivity.this);
//            anim.start();

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
            Async async = new Async();
            async.execute();

            final ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
            Dialog.setMessage("Please Wait");
            Dialog.show();
            ParseUser.logInInBackground(mEmail.getText().toString(), mPassword.getText().toString(), new LogInCallback() {
                public void done(final ParseUser user, ParseException e) {
                    Dialog.dismiss();
                    if (user != null)
                    {
                        if (user.getBoolean("emailVerified")) {
                            // Hooray! The user is logged in.

                            ParseFile file = user.getParseFile("userimage");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArr);
                                    String path = MediaStore.Images.Media.insertImage(LoginActivity.this.getContentResolver(), bitmap, "Title", null);
                                    Uri image = Uri.parse(path);
                                    Log.i("byte", image + " ; " + bytes);

                                    SharedPreferences sp1 = getSharedPreferences("USER_IMAGE", MODE_PRIVATE);
                                    SharedPreferences.Editor spEdit = sp1.edit();
                                    spEdit.putString("UIMAGE", image + "");
                                    spEdit.commit();

                                }
                            });

                            SharedPreferences.Editor spEdit = sp.edit();
                            spEdit.putString("EMAIL",user.getEmail());
                            spEdit.putString("USERNAME", user.getString("uname"));
                            spEdit.putInt("UID", user.getInt("uid"));
                            spEdit.putInt("FID", user.getInt("fid"));
                            spEdit.putInt("REQUEST", user.getInt("has_request"));
                            spEdit.putInt("HEAD", user.getInt("is_head"));
                            spEdit.commit();

//                            ArrayList al = dbHelper.getSettingsUid();
//                            if (! al.contains(sp.getInt("UID", 0)))
//                                dbHelper.addSettings(sp.getInt("UID", 0), "INR");

                            Toast.makeText(LoginActivity.this,"You are Logged In "+sp.getInt("UID",1110),
                                    Toast.LENGTH_LONG).show();
                            Intent i=new Intent(LoginActivity.this,PieChartActivity.class);
//                            Intent i=new Intent(LoginActivity.this,AddAccountActivity.class);
//                            Intent i=new Intent(LoginActivity.this,AddTransactionsActivity.class);
//                            Intent i =new Intent(LoginActivity.this,AddCategoryActivity.class);
//                            Intent i=new Intent(LoginActivity.this,AddPersonActivity.class);
//                            Intent i=new Intent(LoginActivity.this,AddRecursiveActivity.class);
//                            Intent i=new Intent(LoginActivity.this,SettingsActivity.class);
//                            Intent i=new Intent(LoginActivity.this,AddBudgetActivity.class);
//                            Intent i=new Intent(LoginActivity.this,AddLoanDebtActivity.class);
                            startActivity(i);

//                            Intent intent = new Intent(LoginActivity.this, WebSyncService.class);
//                            startService(intent);
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
            Dialog.setMessage("Please Wait.... Data is being syncing");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                dbHelper.deleteAllData(sp.getInt("UID", 0));
                final ParseQuery<ParseObject> accounts = ParseQuery.getQuery("Accounts");
                accounts.whereEqualTo("acc_uid", sp.getInt("UID", 0));
                accounts.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                int acc_id = parseObject.getInt("acc_id");
                                String acc_name = parseObject.getString("acc_name");
                                float acc_bal = Float.parseFloat(parseObject.getDouble("acc_balance")+"");
                                int show = parseObject.getInt("acc_show");
                                String note = parseObject.getString("acc_note");
                                if (dbHelper.addAccount(sp.getInt("UID", 0), acc_id, acc_name, acc_bal, note, show)) {
                                    Log.i("Account added :", acc_id + "");
                                }
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> budgets = ParseQuery.getQuery("Budgets");
                budgets.whereEqualTo("b_uid", sp.getInt("UID", 0));
                budgets.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                int b_id = parseObject.getInt("b_id");
                                String b_startDate = parseObject.getString("b_startDate");
                                String b_endDate = parseObject.getString("b_endDate");
                                float b_amount = Float.parseFloat(parseObject.getDouble("b_amount") + "");
                                if (dbHelper.addBudget(sp.getInt("UID", 0), b_id, b_amount, b_startDate, b_endDate)) {
                                    Log.i("Budget added :", b_id + "");
                                }
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> transactions = ParseQuery.getQuery("Transactions");
                transactions.whereEqualTo("trans_uid", sp.getInt("UID", 0));
                transactions.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                int trans_id = parseObject.getInt("trans_id");
                                int trans_rec_id = parseObject.getInt("trans_rec_id");
                                int trans_from_acc = parseObject.getInt("trans_from_acc");
                                int trans_to_acc = parseObject.getInt("trans_to_acc");
                                int trans_show = parseObject.getInt("trans_show");
                                String trans_date = parseObject.getString("trans_date");
                                String trans_time = parseObject.getString("trans_time");
                                String trans_note = parseObject.getString("trans_note");
                                String trans_type = parseObject.getString("trans_type");
                                int trans_category = parseObject.getInt("trans_category");
                                int trans_subcategory = parseObject.getInt("trans_subcategory");
                                int trans_person = parseObject.getInt("trans_person");
                                float trans_balance = Float.parseFloat(parseObject.getDouble("trans_balance") + "");
                                if (dbHelper.addTransaction(sp.getInt("UID", 0), trans_id, trans_from_acc, trans_to_acc, trans_balance,
                                        trans_note, trans_person, trans_category, trans_subcategory, trans_show, trans_type, trans_date,
                                        trans_time, trans_rec_id)) {
                                    Log.i("Transactions added :", trans_id + "");
                                }
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> recursive = ParseQuery.getQuery("Recursive");
                recursive.whereEqualTo("rec_uid", sp.getInt("UID", 0));
                recursive.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                int rec_id = parseObject.getInt("rec_id");
                                int rec_from_acc = parseObject.getInt("rec_from_acc");
                                int rec_to_acc = parseObject.getInt("rec_to_acc");
                                int rec_show = parseObject.getInt("rec_show");
                                String rec_start_date = parseObject.getString("rec_start_date");
                                String rec_end_date = parseObject.getString("rec_end_date");
                                String rec_next_date = parseObject.getString("rec_next_date");
                                String rec_time = parseObject.getString("rec_time");
                                String rec_note = parseObject.getString("rec_note");
                                String rec_type = parseObject.getString("rec_type");
                                int rec_recurring = parseObject.getInt("rec_recurring");
                                int rec_alert = parseObject.getInt("rec_alert");
                                int rec_category = parseObject.getInt("rec_category");
                                int rec_subcategory = parseObject.getInt("rec_subcategory");
                                int rec_person = parseObject.getInt("rec_person");
                                float rec_balance = Float.parseFloat(parseObject.getDouble("rec_balance") + "");
                                if (dbHelper.addRecursive(sp.getInt("UID", 0), rec_id, rec_from_acc, rec_to_acc, rec_balance,
                                        rec_note, rec_person, rec_category, rec_subcategory, rec_show, rec_type, rec_start_date,
                                        rec_end_date, rec_next_date, rec_time, rec_recurring, rec_alert)) {
                                    Log.i("Recursive added :", rec_id + "");
                                }
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> persons = ParseQuery.getQuery("Persons");
                persons.whereEqualTo("p_uid", sp.getInt("UID", 0));
                persons.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                int p_id = parseObject.getInt("p_id");
                                String p_name = parseObject.getString("p_name");
                                String p_color = parseObject.getString("p_color");
                                String p_color_code = parseObject.getString("p_color_code");
                                if (dbHelper.addPerson(p_name, p_id, p_color, p_color_code, sp.getInt("UID", 0))) {
                                    Log.i("Person added :", p_id + "");
                                }
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> category_specific = ParseQuery.getQuery("Category_specific");
                category_specific.whereEqualTo("c_uid", sp.getInt("UID", 0));
                category_specific.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                int c_id = parseObject.getInt("c_id");
                                int c_type = parseObject.getInt("c_type");
                                String c_name = parseObject.getString("c_name");
                                String c_icon = parseObject.getString("c_icon");
                                if (dbHelper.addCategorySpecific(sp.getInt("UID", 0), c_id, c_name, c_type, c_icon)) {
                                    Log.i("Category added :", c_id + "");
                                }
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> sub_category = ParseQuery.getQuery("Sub_category");
                sub_category.whereEqualTo("sub_uid", sp.getInt("UID", 0));
                sub_category.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                int sub_id = parseObject.getInt("sub_id");
                                int sub_c_id = parseObject.getInt("sub_c_id");
                                String sub_name = parseObject.getString("sub_name");
                                String sub_icon = parseObject.getString("sub_icon");
                                if (dbHelper.addSubCategory(sub_c_id, sub_id, sub_name, sp.getInt("UID", 0), sub_icon)) {
                                    Log.i("SubCategory added :", sub_id + "");
                                }
                            }
                        }
                    }
                });

                final ParseQuery<ParseObject> loan_debt = ParseQuery.getQuery("Loan_debt");
                loan_debt.whereEqualTo("loan_debt_uid", sp.getInt("UID", 0));
                loan_debt.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        for (final ParseObject parseObject : parseObjects) {
                            if (e == null) {
                                int loan_debt_id = parseObject.getInt("loan_debt_id");
                                int loan_debt_parent = parseObject.getInt("loan_debt_parent");
                                int loan_debt_from_acc = parseObject.getInt("loan_debt_from_acc");
                                int loan_debt_to_acc = parseObject.getInt("loan_debt_to_acc");
                                String loan_debt_date = parseObject.getString("loan_debt_date");
                                String loan_debt_time = parseObject.getString("loan_debt_time");
                                String loan_debt_note = parseObject.getString("loan_debt_note");
                                String loan_debt_type = parseObject.getString("loan_debt_type");
                                int loan_debt_person = parseObject.getInt("loan_debt_person");
                                float loan_debt_balance = Float.parseFloat(parseObject.getDouble("loan_debt_balance") + "");
                                if (dbHelper.addLoanDebt(sp.getInt("UID", 0), loan_debt_id, loan_debt_balance, loan_debt_date, loan_debt_time,
                                        loan_debt_from_acc, loan_debt_to_acc, loan_debt_person, loan_debt_note, loan_debt_type, loan_debt_parent)) {
                                    Log.i("LoanDebt added :", loan_debt_id + "");
                                }
                            }
                        }
                    }
                });

//                final ParseQuery<ParseObject> settings = ParseQuery.getQuery("Settings");
//                settings.whereEqualTo("settings_uid", sp.getInt("UID", 0));
//                settings.findInBackground(new FindCallback<ParseObject>() {
//                    @Override
//                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
//                        for (final ParseObject parseObject : parseObjects) {
//                            if (e == null) {
//                                String settings_cur_code = parseObject.getString("settings_cur_code");
//                                ArrayList al = dbHelper.getSettingsUid();
//                                if (! al.contains(sp.getInt("UID", 0)))
//                                    if (dbHelper.addSettings(sp.getInt("UID", 0), settings_cur_code)) {
//                                        Log.i("Settings added :", settings_cur_code);
//                                    }
//                            }
//                        }
//                    }
//                });
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
}