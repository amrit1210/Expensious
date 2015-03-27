package com.example.dhruvgupta.expensious;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Amrit on 3/24/2015.
 */
public class AddTransactionsActivity extends ActionBarActivity
{
    int mYear,mMonth,mDay,mHour,mMin,flag=0,p_id=0,show=0;
    Button mDate, mTime, mAmt;
    CheckBox mShow;
    SharedPreferences sp;
    DBHelper dbHelper;
    EditText mFromAcc, mToAcc, mCategory, mPerson,mNote;
    String mType;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        mDate=(Button)findViewById(R.id.add_trans_btn_date);
        mTime=(Button)findViewById(R.id.add_trans_btn_time);
        mAmt=(Button)findViewById(R.id.add_trans_btn_amt);
        mFromAcc = (EditText)findViewById(R.id.add_trans_from_account);
        mToAcc = (EditText)findViewById(R.id.add_trans_to_account);
        mCategory = (EditText)findViewById(R.id.add_trans_category);
        mPerson = (EditText)findViewById(R.id.add_trans_person);
        mNote=(EditText)findViewById(R.id.add_trans_note);
        mShow=(CheckBox)findViewById(R.id.add_trans_cb);
        mType="Expense";
        sp=getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        dbHelper=new DBHelper(AddTransactionsActivity.this);
    }
    public void onSaveTransaction(View v)
    { float amt=Float.parseFloat(mAmt.getText().toString());
        if(mShow.isChecked())
        {
            show=1;
        }
        else
        {
            show=0;
        }

            if(amt<=0)
            {
                mAmt.setError("Enter amount");
            }
        else {
                if (flag == 0) {
                    amt=Float.parseFloat(mAmt.getText().toString());
                    if (mFromAcc.getText().toString() != null) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                        if (mPerson.getText().toString() != null)
                            p_id = dbHelper.getPersonColId(sp.getInt("UID", 0), mPerson.getText().toString());
                        boolean b =dbHelper.addTransaction(sp.getInt("UID",0),acc_id, 0,amt, mNote.getText().toString(), p_id, 0, 0, show, mType, mDate.getText().toString(), mTime.getText().toString());
                        Toast.makeText(AddTransactionsActivity.this,"Transaction Added",Toast.LENGTH_LONG).show();
                    }
                }
                else if (flag == 1) {
                        if (mToAcc.getText().toString() != null) {
                            int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mToAcc.getText().toString());
                            if (mPerson.getText().toString() != null)
                                p_id = dbHelper.getPersonColId(sp.getInt("UID", 0), mPerson.getText().toString());
                            dbHelper.addTransaction(sp.getInt("UID",0), 0, acc_id, Float.parseFloat(mAmt.getText().toString()), mNote.getText().toString(), p_id, 0, 0, show, mType, mDate.getText().toString(), mTime.getText().toString());
                            Toast.makeText(AddTransactionsActivity.this,"Transaction Added",Toast.LENGTH_LONG).show();
                        }
                }
                else if (flag == 2) {
                        if (mFromAcc.getText().toString() != null && mToAcc.getText().toString() != null) {
                            int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                            int acc_id1 = dbHelper.getAccountColId(sp.getInt("UID", 0), mToAcc.getText().toString());
                            dbHelper.addTransaction(sp.getInt("UID",0), acc_id, acc_id1, Float.parseFloat(mAmt.getText().toString()), mNote.getText().toString(), p_id, 0, 0, show, mType, mDate.getText().toString(), mTime.getText().toString());
                            Toast.makeText(AddTransactionsActivity.this,"Transaction Added",Toast.LENGTH_LONG).show();
                        }
                }


                dbHelper.getAllTransactions(sp.getInt("UID",1));
            }
    }
    public void onExpenseClick(View v)
    {
        mFromAcc.setVisibility(View.VISIBLE);
        mToAcc.setVisibility(View.GONE);
        mCategory.setVisibility(View.VISIBLE);
        mPerson.setVisibility(View.VISIBLE);
        mType="Expense";
        flag=0;
    }

    public void onIncomeClick(View v)
    {
        mFromAcc.setVisibility(View.GONE);
        mToAcc.setVisibility(View.VISIBLE);
        mCategory.setVisibility(View.VISIBLE);
        mPerson.setVisibility(View.VISIBLE);
        mType="Income";
        flag =1;

    }

    public void onTransferClick(View v)
    {
        mFromAcc.setVisibility(View.VISIBLE);
        mToAcc.setVisibility(View.VISIBLE);
        mCategory.setVisibility(View.GONE);
        mPerson.setVisibility(View.GONE);
        mType="Transfer";
        flag=2;
    }

    public void onDateClick(View v)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddTransactionsActivity.this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth)
                    {
                        if(view.isShown())
                            mDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void onTimeClick(View v)
    { final Calendar c=Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMin = c.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddTransactionsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, mHour, mMin, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void onFromAccountClick(View v)
    {
        Intent i=new Intent(AddTransactionsActivity.this,AccountsViewList.class);
        startActivityForResult(i, 1);
    }

    public void onToAccountClick(View v)
    {
        Intent i=new Intent(AddTransactionsActivity.this,AccountsViewList.class);
        startActivityForResult(i, 2);
    }

    public void onAmountClick(View v){
        Intent i=new Intent(AddTransactionsActivity.this,Calculator.class);
        startActivityForResult(i, 3);
    }
    public void onPersonClick(View v)
    {
        Intent i=new Intent(AddTransactionsActivity.this,PersonsViewList.class);
        startActivityForResult(i, 4);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            if (requestCode==1)
                mFromAcc.setText(data.getExtras().getString("Acc_Name"));
            else if (requestCode==2)
                mToAcc.setText(data.getExtras().getString("Acc_Name"));
            else if (requestCode==3)
                mAmt.setText(data.getExtras().getFloat("RESULT")+"");
            else if (requestCode==4)
                mPerson.setText(data.getExtras().getString("Person_Name"));
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

