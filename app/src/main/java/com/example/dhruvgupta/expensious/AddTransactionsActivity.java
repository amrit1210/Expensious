package com.example.dhruvgupta.expensious;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
public class AddTransactionsActivity extends ActionBarActivity {
    int mYear, mMonth, mDay, mHour, mMin, flag = 0, p_id = 0, show = 0;
    Button mDate, mTime, mAmt;
    CheckBox mShow;
    SharedPreferences sp;
    DBHelper dbHelper;
    EditText mFromAcc, mToAcc, mCategory, mPerson, mNote;
    String mType, t_date, t_time, t_type;
    int t_id, t_u_id, t_category,t_subcategory, t_fromAccount, t_toAccount, t_person, intentFlag;
    Button mExp,mInc,mTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        mDate = (Button) findViewById(R.id.add_trans_btn_date);
        mTime = (Button) findViewById(R.id.add_trans_btn_time);
        mAmt = (Button) findViewById(R.id.add_trans_btn_amt);
        mFromAcc = (EditText) findViewById(R.id.add_trans_from_account);
        mToAcc = (EditText) findViewById(R.id.add_trans_to_account);
        mCategory = (EditText) findViewById(R.id.add_trans_category);
        mPerson = (EditText) findViewById(R.id.add_trans_person);
        mNote = (EditText) findViewById(R.id.add_trans_note);
        mShow = (CheckBox) findViewById(R.id.add_trans_cb);
        mExp=(Button)findViewById(R.id.add_trans_btn_expense);
        mInc=(Button)findViewById(R.id.add_trans_btn_income);
        mTrans=(Button)findViewById(R.id.add_trans_btn_transfer);
        mType = "Expense";
        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(AddTransactionsActivity.this);
        intentFlag = 0;

        if (getIntent().getIntExtra("t_id",0) >0) {
            intentFlag = 1;
            t_id = getIntent().getIntExtra("t_id", 0);
            t_u_id = getIntent().getIntExtra("t_u_id", 0);
            t_fromAccount = getIntent().getIntExtra("t_fromAccount", 0);
            t_toAccount = getIntent().getIntExtra("t_toAccount", 0);
            mAmt.setText(getIntent().getFloatExtra("t_bal", 0) + "");
            mNote.setText(getIntent().getStringExtra("t_note"));
            mDate.setText(getIntent().getStringExtra("t_date"));
            mTime.setText(getIntent().getStringExtra("t_time"));
            t_category = getIntent().getIntExtra("t_category", 0);
            show = getIntent().getIntExtra("t_show", 0);

            t_type = getIntent().getStringExtra("t_type");

            t_subcategory= getIntent().getIntExtra("t_subcategory",0);
            if (t_type.equals("Expense")) {
                flag = 0;
                if(t_fromAccount!=0) {
                    Cursor c1 = dbHelper.getAccountData(t_fromAccount);
                    c1.moveToFirst();
                    String from_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mFromAcc.setText(from_acc);
                    c1.close();
                }
                onExpenseClick(mExp);
                t_person = getIntent().getIntExtra("t_person", 0);
                Cursor c =dbHelper.getPersonData(t_person);
                c.moveToFirst();
                String person=c.getString(c.getColumnIndex(DBHelper.PERSON_COL_NAME));
                mPerson.setText(person);
                c.close();
            }
            if (t_type.equals("Income")) {
                flag = 1;
                if(t_toAccount!=0) {
                    Cursor c1 = dbHelper.getAccountData(t_toAccount);
                    c1.moveToFirst();
                    String to_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mToAcc.setText(to_acc);
                    c1.close();
                }
                onIncomeClick(mInc);
                t_person = getIntent().getIntExtra("t_person", 0);
                Cursor c2 =dbHelper.getPersonData(t_person);
                c2.moveToFirst();
                String person=c2.getString(c2.getColumnIndex(DBHelper.PERSON_COL_NAME));
                mPerson.setText(person);
                c2.close();
            }
             if (t_type.equals("Transfer")) {
                flag = 2;

                 if(t_fromAccount!=0) {
                     Cursor c1 = dbHelper.getAccountData(t_fromAccount);
                     c1.moveToFirst();
                     String from_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                     mFromAcc.setText(from_acc);
                     c1.close();
                 }
                 if(t_toAccount!=0) {
                     Cursor c2 = dbHelper.getAccountData(t_toAccount);
                     c2.moveToFirst();
                     String to_acc = c2.getString(c2.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                     mToAcc.setText(to_acc);
                     c2.close();
                 }
                 onTransferClick(mTrans);
            }
            if (show == 1) {
                mShow.setChecked(true);
            } else {
                mShow.setChecked(false);
            }
        }
    }

    public void onSaveTransaction(View v) {
        float amt = Float.parseFloat(mAmt.getText().toString());
        if (mShow.isChecked()) {
            show = 1;
        } else {
            show = 0;
        }

        if (amt <= 0) {
            mAmt.setError("Enter amount");
        }
        else
        {
            mAmt.setError(null);
        }

        if(mAmt.getError()==null)
        {

            if (intentFlag == 1) {

                amt = Float.parseFloat(mAmt.getText().toString());
                boolean b = dbHelper.updateTransactionData(t_id, t_fromAccount, t_toAccount,t_person,t_category,t_subcategory, amt, mNote.getText().toString(),show, t_type, mDate.getText().toString(), mTime.getText().toString(),sp.getInt("UID",0));

              /*  if(flag==0)
                {

                }
                if(flag==1)
                {

                }
                if(flag==2)
                {

                }*/
                if (b) {
                    Toast.makeText(AddTransactionsActivity.this, "Transaction Updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddTransactionsActivity.this, TransactonsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddTransactionsActivity.this, "Error updating Transaction", Toast.LENGTH_LONG).show();
                }


            } else {
                if (flag == 0) {
                    amt = Float.parseFloat(mAmt.getText().toString());
                    if (mFromAcc.length()>0) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                        if (mPerson.getText().toString() != null)
                            p_id = dbHelper.getPersonColId(sp.getInt("UID", 0), mPerson.getText().toString());
                        boolean b = dbHelper.addTransaction(sp.getInt("UID", 0), acc_id, 0, amt, mNote.getText().toString(), p_id, 0, 0, show, mType, mDate.getText().toString(), mTime.getText().toString());
                        Toast.makeText(AddTransactionsActivity.this, "Transaction Added", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        mFromAcc.setError("Enter Account");
                    }
                } else if (flag == 1) {
                    if (mToAcc.length()>0) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mToAcc.getText().toString());
                        if (mPerson.getText().toString() != null)
                            p_id = dbHelper.getPersonColId(sp.getInt("UID", 0), mPerson.getText().toString());
                       boolean b= dbHelper.addTransaction(sp.getInt("UID", 0), 0, acc_id, Float.parseFloat(mAmt.getText().toString()), mNote.getText().toString(), p_id, 0, 0, show, mType, mDate.getText().toString(), mTime.getText().toString());
                        Toast.makeText(AddTransactionsActivity.this, "Transaction Added", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        mToAcc.setError("Enter Account");
                    }
                } else if (flag == 2) {
                    if (mFromAcc.length()>0 && mToAcc.length()>0) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                        int acc_id1 = dbHelper.getAccountColId(sp.getInt("UID", 0), mToAcc.getText().toString());
                        boolean b=dbHelper.addTransaction(sp.getInt("UID", 0), acc_id, acc_id1, Float.parseFloat(mAmt.getText().toString()), mNote.getText().toString(), p_id, 0, 0, show, mType, mDate.getText().toString(), mTime.getText().toString());
                        Toast.makeText(AddTransactionsActivity.this, "Transaction Added", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(AddTransactionsActivity.this, "Error Adding Transaction", Toast.LENGTH_LONG).show();

                }
                Intent intent = new Intent(AddTransactionsActivity.this, TransactonsActivity.class);
                startActivity(intent);

            }
        }


        dbHelper.getAllTransactions(sp.getInt("UID", 0));
    }




    public void onExpenseClick(View v)
    {
        mExp.setPressed(true);
        mFromAcc.setVisibility(View.VISIBLE);
        mToAcc.setVisibility(View.GONE);
        mCategory.setVisibility(View.VISIBLE);
        mPerson.setVisibility(View.VISIBLE);
        mType="Expense";
        t_type="Expense";
        flag=0;
    }

    public void onIncomeClick(View v)
    {
        mInc.setPressed(true);
        mFromAcc.setVisibility(View.GONE);
        mToAcc.setVisibility(View.VISIBLE);
        mCategory.setVisibility(View.VISIBLE);
        mPerson.setVisibility(View.VISIBLE);
        mType="Income";
        t_type="Income";
        flag =1;

    }

    public void onTransferClick(View v)
    {
        mTrans.setPressed(true);
        mFromAcc.setVisibility(View.VISIBLE);
        mToAcc.setVisibility(View.VISIBLE);
        mCategory.setVisibility(View.GONE);
        mPerson.setVisibility(View.GONE);
        mType="Transfer";
        t_type="Transfer";
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

