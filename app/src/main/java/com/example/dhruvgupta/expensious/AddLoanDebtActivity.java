package com.example.dhruvgupta.expensious;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
 * Created by Shubhz on 4/6/2015.
 */
public class AddLoanDebtActivity extends ActionBarActivity {
    Button mAmt,mDate,mTime,mLoan,mDebt;
    CheckBox mShow;
    EditText mFromAcc,mToAcc,mNote,mPerson;
    String mType,from_acc=null,to_acc=null,person=null,type=null,note=null,l_date,l_time,l_type;
    int mYear, mMonth, mDay, mHour, mMin, flag=0,l_u_id,l_id,l_balance,l_fromAcc,l_toAcc,l_person,intentFlag=0,p_id=0,show=0,l_parent=0;
    DBHelper dbHelper;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan_debt);
        mAmt = (Button) findViewById(R.id.add_loan_debt_amt);
        mDate = (Button) findViewById(R.id.add_loan_debt_btn_date);
        mTime = (Button) findViewById(R.id.add_loan_debt_btn_time);
        mLoan = (Button) findViewById(R.id.add_loan_debt_btn_loan);
        mDebt = (Button) findViewById(R.id.add_loan_debt_btn_debt);
        mFromAcc = (EditText) findViewById(R.id.add_loan_debt_from_account);
        mToAcc = (EditText) findViewById(R.id.add_loan_debt_to_account);
        mNote = (EditText) findViewById(R.id.add_loan_debt_note);
        mPerson = (EditText) findViewById(R.id.add_loan_debt_person);
        mShow = (CheckBox) findViewById(R.id.add_loan_debt_cb);
        mType = "Loan";
        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMin = calendar.get(Calendar.MINUTE);
        mDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mYear).append(" ").append("-").append(mMonth + 1).append("-")
                .append(mDay));
        mTime.setText(new StringBuilder().append(mHour).append(":").append(mMin));

        if (getIntent().getIntExtra("l_id",0) >0) {
            intentFlag = 1;
            l_id = getIntent().getIntExtra("l_id", 0);
            l_u_id = getIntent().getIntExtra("l_u_id", 0);
            l_fromAcc = getIntent().getIntExtra("l_fromAcc", 0);
            l_toAcc = getIntent().getIntExtra("l_toAcc", 0);
            mAmt.setText(getIntent().getFloatExtra("l_bal", 0) + "");
            mNote.setText(getIntent().getStringExtra("l_note"));
            mDate.setText(getIntent().getStringExtra("l_date"));
            mTime.setText(getIntent().getStringExtra("l_time"));
            show = getIntent().getIntExtra("l_show", 0);
            l_type = getIntent().getStringExtra("l_type");
            if (l_type.equals("Loan")) {
                flag = 0;
                l_toAcc=0;
                to_acc=null;
                if(l_fromAcc!=0) {
                    Cursor c1 = dbHelper.getAccountData(l_fromAcc);
                    c1.moveToFirst();
                    from_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mFromAcc.setText(from_acc);
                    c1.close();
                }
                l_person = getIntent().getIntExtra("t_person", 0);
                if(l_person!=0) {
                    Cursor c = dbHelper.getPersonData(l_person);
                    c.moveToFirst();
                    person = c.getString(c.getColumnIndex(DBHelper.PERSON_COL_NAME));
                    mPerson.setText(person);
                    c.close();
                }
                onLoan(mLoan);

            }
            if (l_type.equals("Debt")) {
                flag = 1;
                l_fromAcc=0;
                from_acc=null;
                if(l_toAcc!=0) {
                    Cursor c1 = dbHelper.getAccountData(l_toAcc);
                    c1.moveToFirst();
                    to_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mToAcc.setText(to_acc);
                    c1.close();
                }

                l_person = getIntent().getIntExtra("t_person", 0);
                if(l_person!=0) {
                    Cursor c2 = dbHelper.getPersonData(l_person);
                    c2.moveToFirst();
                    person = c2.getString(c2.getColumnIndex(DBHelper.PERSON_COL_NAME));
                    mPerson.setText(person);
                    c2.close();
                }
                onDebt(mDebt);
            }

        }
        if (show == 1) {
            mShow.setChecked(true);
        } else {
            mShow.setChecked(false);
        }
    }

    public void onDateClick(View v)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddLoanDebtActivity.this,
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
        mTimePicker = new TimePickerDialog(AddLoanDebtActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        Intent i=new Intent(AddLoanDebtActivity.this,AccountsViewList.class);
        startActivityForResult(i, 1);
    }

    public void onToAccountClick(View v)
    {
        Intent i=new Intent(AddLoanDebtActivity.this,AccountsViewList.class);
        startActivityForResult(i, 2);
    }

    public void onAmountClick(View v){
        Intent i=new Intent(AddLoanDebtActivity.this,Calculator.class);
        startActivityForResult(i, 3);
    }

    public void onPersonClick(View v)
    {
        Intent i=new Intent(AddLoanDebtActivity.this,PersonsViewList.class);
        startActivityForResult(i, 4);
    }

    public void onLoan(View v) {
        mFromAcc.setVisibility(View.VISIBLE);
        mToAcc.setVisibility(View.GONE);
        flag=0;
    }


    public void onDebt(View v){
        mFromAcc.setVisibility(View.GONE);
        mToAcc.setVisibility(View.VISIBLE);
        flag=1;
    }

    public void onSaveTransaction(View v){
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

                if(from_acc!=null)
                {
                    l_fromAcc=dbHelper.getAccountColId(sp.getInt("UID",0),from_acc);
                }
                if(to_acc!=null)
                {
                    l_toAcc=dbHelper.getAccountColId(sp.getInt("UID",0),to_acc);
                }
                if(person!=null)
                {
                    l_person=dbHelper.getPersonColId(sp.getInt("UID",0),person);
                }
                amt = Float.parseFloat(mAmt.getText().toString());
                if(amt<=0)
                {
                    mAmt.setError("Enter amount");
                }
                else
                {
                    mAmt.setError(null);
                }
                if(flag==0)
                {
                    mToAcc.setError(null);
                    if(l_fromAcc<=0)
                    {
                        mFromAcc.setError("Enter from Account");
                    }
                    else
                    {
                        mFromAcc.setError(null);
                    }
                }
                else if(flag==1)
                {
                    mFromAcc.setError(null);
                    if(l_toAcc<=0)
                    {
                        mToAcc.setError("Enter to Account");
                    }
                    else
                    {
                        mToAcc.setError(null);
                    }

                }
                if(mFromAcc.getError()==null && mToAcc.getError()==null && mAmt.getError()==null) {
                    boolean b = dbHelper.updateLoanDebtData(l_id, l_fromAcc, l_toAcc, l_person, amt, mNote.getText().toString(), show, l_type, mDate.getText().toString(), mTime.getText().toString(), sp.getInt("UID", 0));

                    if (b) {
                        Toast.makeText(AddLoanDebtActivity.this, "Transaction Updated", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddLoanDebtActivity.this, LoanDebtActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddLoanDebtActivity.this, "Error updating Transaction", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(AddLoanDebtActivity.this, LoanDebtActivity.class);
                    startActivity(intent);
                }

            } else {
                if (flag == 0) {
                    amt = Float.parseFloat(mAmt.getText().toString());
                    if (mFromAcc.length()>0) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                        if (mPerson.getText().toString() != null)
                            p_id = dbHelper.getPersonColId(sp.getInt("UID", 0), mPerson.getText().toString());
                        boolean b = dbHelper.addLoanDebt(sp.getInt("UID", 0), Float.parseFloat(mAmt.getText().toString()), mDate.getText().toString(), mTime.getText().toString(), acc_id,0,p_id,mNote.getText().toString());
                        if(b) {
                            Toast.makeText(AddLoanDebtActivity.this, "Transaction Added", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddLoanDebtActivity.this, LoanDebtActivity.class);
                            startActivity(intent);
                        }
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
                        boolean b = dbHelper.addLoanDebt(sp.getInt("UID", 0), Float.parseFloat(mAmt.getText().toString()), mDate.getText().toString(), mTime.getText().toString(), acc_id,0,p_id,mNote.getText().toString());
                        if (b) {
                            Toast.makeText(AddLoanDebtActivity.this, "Transaction Added", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddLoanDebtActivity.this, LoanDebtActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        mToAcc.setError("Enter Account");
                    }
                } else {
                    Toast.makeText(AddLoanDebtActivity.this, "Error Adding Transaction", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddLoanDebtActivity.this, LoanDebtActivity.class);
                    startActivity(intent);
                }


            }
        }


        dbHelper.getAllLoanDebt(sp.getInt("UID", 0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            if (requestCode == 1) {
                mFromAcc.setText(data.getExtras().getString("Acc_Name"));
                from_acc = mFromAcc.getText().toString();
                if (mFromAcc.length() <= 0) {
                    mFromAcc.setError("Enter from Account");
                } else {
                    mFromAcc.setError(null);
                }
            } else if (requestCode == 2) {
                mToAcc.setText(data.getExtras().getString("Acc_Name"));
                to_acc = mToAcc.getText().toString();
                if (mFromAcc.length() <= 0) {
                    mFromAcc.setError("Enter from Account");
                } else {
                    mFromAcc.setError(null);
                }
            } else if (requestCode == 3)
                mAmt.setText(data.getExtras().getFloat("RESULT") + "");
            else if (requestCode == 4) {
                mPerson.setText(data.getExtras().getString("Person_Name"));
                person = mPerson.getText().toString();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
