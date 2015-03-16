package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    AccountsDBHelper accountsDBHelper;
    ArrayList<AccountsDB> al;
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

        accountsDBHelper =new AccountsDBHelper(AddAccountActivity.this);
        al=new ArrayList<>();
    }

    public void selectCurrency(View v)
    {

    }

    public  void enterAmount(View v)
    {
        Intent intent=new Intent(AddAccountActivity.this,Calculator.class);
        startActivity(intent);
    }

    public  void onSave(View v)
    {
        int i=0;
        if(mInclude.isChecked())
        {
            i = 1;
        }
        if(mAcc_Name.length()>0)
        {
            mAcc_Name.setError("Enter Account name");
        }
        if(mAcc_Name.getError()==null && mAcc_Cur.getError()==null)
        {
            if (accountsDBHelper.addAccount(mAcc_Name.getText().toString(), Integer.parseInt(mAcc_Amt.getText().toString()),
                    mAcc_Note.getText().toString(), mAcc_Cur.getText().toString(), i))
            {
                Toast.makeText(AddAccountActivity.this, "Account Created", Toast.LENGTH_LONG).show();
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
