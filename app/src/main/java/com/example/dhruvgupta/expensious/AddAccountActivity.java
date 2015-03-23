package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

/**
 * Created by Gaurav on 13-Mar-15.
 */
public class AddAccountActivity extends ActionBarActivity
{
    EditText mAcc_Name,mAcc_Note;
    CheckBox mInclude;
    Button mAcc_Cur,mAcc_Amt,mAcc_Save;
    DBHelper dbHelper;
    ArrayList<AccountsDB> al;
    int flag,id,u_id;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        mAcc_Name=(EditText)findViewById(R.id.add_acc_name);
        mAcc_Note=(EditText)findViewById(R.id.add_acc_note);
        mAcc_Cur =(Button)findViewById(R.id.add_acc_btn_cur);
        mAcc_Amt =(Button)findViewById(R.id.add_acc_btn_amt);
        mAcc_Save =(Button)findViewById(R.id.add_acc_btn_save);
        mInclude=(CheckBox)findViewById(R.id.add_acc_cb);
        flag=0;
        i=0;

        dbHelper =new DBHelper(AddAccountActivity.this);
        al=new ArrayList<>();

        if(getIntent().getStringExtra("acc_name")!=null)
        {
            flag=1;
            id=getIntent().getIntExtra("acc_id",0);
            u_id=getIntent().getIntExtra("acc_uid",0);
            mAcc_Name.setText(getIntent().getStringExtra("acc_name"));
            mAcc_Cur.setText(getIntent().getStringExtra("acc_cur"));
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

    public void selectCurrency(View v)
    {
        mAcc_Cur.setText("Rs.");
    }

    public  void enterAmount(View v)
    {
       /* Intent intent=new Intent(AddAccountActivity.this,Calculator.class);
        startActivity(intent);*/
        mAcc_Amt.setText("0");
    }

    public  void onSaveAccount(View v)
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
        if (mAcc_Name.length() < 0)
        {
            mAcc_Name.setError("Enter Account name");
        }
        if (mAcc_Name.getError() == null && mAcc_Cur.getError() == null)
        {
            Log.i("Flag",flag+"");
            if(flag==1)
            {
                if(dbHelper.updateAccountData(id,mAcc_Name.getText().toString(),Float.parseFloat(mAcc_Amt.getText()
                        .toString()),mAcc_Note.getText().toString(),mAcc_Cur.getText().toString(),i))
                {
                    Toast.makeText(AddAccountActivity.this, "Account Updated", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(AddAccountActivity.this,AccountsActivity.class);
                    startActivity(intent);
                }

            }
            else
            {
                if(dbHelper.addAccount(sp.getInt("UID",0),mAcc_Name.getText().toString(),Float.parseFloat
                        (mAcc_Amt.getText().toString()),mAcc_Note.getText().toString(), mAcc_Cur.getText().toString(), i))
                {
                    Toast.makeText(AddAccountActivity.this, "Account Created", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddAccountActivity.this, AccountsActivity.class);
                    startActivity(intent);
                }
            }
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

        return super.onOptionsItemSelected(item);
    }
}
