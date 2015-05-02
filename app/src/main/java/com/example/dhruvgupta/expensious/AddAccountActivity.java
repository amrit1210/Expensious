package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Gaurav on 13-Mar-15.
 */
public class AddAccountActivity extends ActionBarActivity
{
    EditText mAcc_Name,mAcc_Note;
    CheckBox mInclude;
    Button mAcc_Cur,mAcc_Amt,mAcc_Save;
    SharedPreferences sp;
    DBHelper dbHelper;

    int flag,acc_id,u_id,i;
    ArrayList<CurrencyDB> al;
    String curCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        this.getSupportActionBar().setTitle("Add Account");
        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        mAcc_Name=(EditText)findViewById(R.id.add_acc_name);
        mAcc_Note=(EditText)findViewById(R.id.add_acc_note);
        mAcc_Cur =(Button)findViewById(R.id.add_acc_btn_cur);
        mAcc_Amt =(Button)findViewById(R.id.add_acc_btn_amt);
        mInclude=(CheckBox)findViewById(R.id.add_acc_cb);
        flag=0;
        i=1;

        dbHelper =new DBHelper(AddAccountActivity.this);

        al = new ListOfCurrencies().getAllCurrencies();

        Cursor c = dbHelper.getSettingsData(sp.getInt("UID", 0));
        c.moveToFirst();
        curCode = c.getString(c.getColumnIndex(DBHelper.SETTINGS_COL_CUR_CODE));
        c.close();

        Iterator<CurrencyDB> iterator = al.iterator();
        while (iterator.hasNext())
        {
            CurrencyDB curDB = iterator.next();
            if (curDB.c_code.equals(curCode))
                mAcc_Cur.setText(curDB.c_symbol);
        }

        if(getIntent().getStringExtra("acc_name")!=null)
        {
            flag=1;
            acc_id=getIntent().getIntExtra("acc_id",0);
            u_id=getIntent().getIntExtra("acc_uid",0);
            mAcc_Name.setText(getIntent().getStringExtra("acc_name"));
            mAcc_Amt.setText(getIntent().getFloatExtra("acc_bal",0)+"");
            mAcc_Note.setText(getIntent().getStringExtra("acc_note"));
            i=(getIntent().getIntExtra("acc_show",0));
            if (i==1)
            {
                mInclude.setChecked(true);
            }
            else
            {
                mInclude.setChecked(false);
            }
        }
    }

    public  void enterAmount(View v)
    {
        Intent intent=new Intent(AddAccountActivity.this,Calculator.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1 && resultCode == 1)
            {
                mAcc_Amt.setText(data.getExtras().getFloat("RESULT",0.0f)+"");
            }
    }

    public void onSaveAccount()
    {
        SharedPreferences sp= getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        if (mInclude.isChecked())
        {
            i = 1;
        }
        else
        {
            i=0;
        }

        if (mAcc_Name.length() <= 0)
        {
            mAcc_Name.setError("Enter Account Name");
        }
        else if (!mAcc_Name.getText().toString().matches("[a-zA-Z0-9][a-zA-Z0-9 ]+"))
        {
            mAcc_Name.setError("Enter valid Account Name");
        }
        else
        {
            mAcc_Name.setError(null);
        }

        if (Float.parseFloat(mAcc_Amt.getText().toString()) == 0)
        {
            mAcc_Amt.setError("Enter Amount > 0");
        }
        else
        {
            mAcc_Amt.setError(null);
        }

        if (mAcc_Name.getError() == null && mAcc_Amt.getError() == null)
        {
            Log.i("Flag",flag+"");
            if(flag==1)
            {
                if(dbHelper.updateAccountData(acc_id,mAcc_Name.getText().toString(),Float.parseFloat(mAcc_Amt.getText()
                        .toString()),mAcc_Note.getText().toString(),i,sp.getInt("UID",0)))
                {
                    Toast.makeText(AddAccountActivity.this, "Account Updated", Toast.LENGTH_LONG).show();

                    ParseObject account = new ParseObject("Accounts");
                    account.put("acc_id", acc_id);
                    account.put("acc_uid", sp.getInt("UID",0));
                    account.put("acc_name", mAcc_Name.getText().toString());
                    account.put("acc_balance", Float.parseFloat(mAcc_Amt.getText().toString()));
                    account.put("acc_note", mAcc_Note.getText().toString());
                    account.put("acc_show", i);
                    account.pinInBackground("pinAccountsUpdate");

//                    Intent intent=new Intent(AddAccountActivity.this,AccountsActivity.class);
//                    startActivity(intent);

                    Intent i = new Intent();
                    this.finish();
                }
                else
                {
                    Toast.makeText(AddAccountActivity.this, "Error updating Account", Toast.LENGTH_LONG).show();
//                    Intent intent=new Intent(AddAccountActivity.this,AccountsActivity.class);
//                    startActivity(intent);

                    Intent i = new Intent();
                    this.finish();
                }

            }
            else
            {
                if(dbHelper.addAccount(sp.getInt("UID",0),mAcc_Name.getText().toString(),Float.parseFloat
                        (mAcc_Amt.getText().toString()),mAcc_Note.getText().toString(), i))
                {
                    Toast.makeText(AddAccountActivity.this, "Account Created", Toast.LENGTH_LONG).show();

                    final int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mAcc_Name.getText().toString());

                    ParseObject account = new ParseObject("Accounts");
                    account.put("acc_id", acc_id);
                    account.put("acc_uid", sp.getInt("UID",0));
                    account.put("acc_name", mAcc_Name.getText().toString());
                    account.put("acc_balance", Float.parseFloat(mAcc_Amt.getText().toString()));
                    account.put("acc_note", mAcc_Note.getText().toString());
                    account.put("acc_show", i);
                    account.pinInBackground("pinAccounts");
                    account.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.i("Account saveEventually", "YES! YES! YES!");
                        }
                    });

//                    Intent intent = new Intent(AddAccountActivity.this, AccountsActivity.class);
//                    startActivity(intent);

                    Intent i = new Intent();
                    this.finish();
                }
                else
                {
                    Toast.makeText(AddAccountActivity.this, "Error creating Account", Toast.LENGTH_LONG).show();
//                    Intent intent=new Intent(AddAccountActivity.this,AccountsActivity.class);
//                    startActivity(intent);
                    Intent i = new Intent();
                    this.finish();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        Drawable saveBtn = new IconDrawable(this, Iconify.IconValue.fa_check_circle_o)
                .colorRes(R.color.accent_color_200)
                .actionBarSize();
        menu.findItem(R.id.action_done).setIcon(saveBtn);

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
       if (id == R.id.action_done)
        {
            onSaveAccount();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
