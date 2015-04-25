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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dhruvgupta on 4/12/2015.
 */
public class DetailedAddLD extends ActionBarActivity {
    Button mAmt,mDate, mTime;
    EditText mFromAcc, mToAcc, mNote;
    LinearLayout mLlFromAcc, mLlToAcc;
    SimpleDateFormat sdf;
    float l_amt_old;
    String mType, from_acc=null, to_acc=null, person=null, type=null, note=null, l_date, l_time, l_type, l_type_old, mPerson;
    int mYear, mMonth, mDay, mHour, mMin, flag=0, l_u_id, l_id, l_balance, l_fromAcc, l_toAcc, l_person, intentFlag=0, p_id=0, show=0,
            l_parent=0, l_from_old, l_to_old;
    DBHelper dbHelper;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_add_ld);
        mLlFromAcc = (LinearLayout) findViewById(R.id.detailed_add_ld_ll_from_account);
        mLlToAcc = (LinearLayout) findViewById(R.id.detailed_add_ld_ll_to_account);
        mAmt = (Button) findViewById(R.id.detailed_add_ld_amt);
        mDate = (Button) findViewById(R.id.detailed_add_ld_date);
        mTime = (Button) findViewById(R.id.detailed_add_ld_time);
        mFromAcc = (EditText) findViewById(R.id.detailed_add_ld_from_account);
        mToAcc = (EditText) findViewById(R.id.detailed_add_ld_to_account);
        mNote = (EditText) findViewById(R.id.detailed_add_ld_note);
        mType = "Loan";
        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(DetailedAddLD.this);

        mFromAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFromAccountClick(v);
            }
        });

        mToAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToAccountClick(v);
            }
        });

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMin = calendar.get(Calendar.MINUTE);
        mDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mDay).append("-").append(mMonth + 1).append("-")
                .append(mYear));
        mTime.setText(new StringBuilder().append(mHour).append(":").append(mMin));

        sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
            mDate.setText(sdf.format(sdf.parse(mDate.getText().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
            l_parent = getIntent().getIntExtra("l_parent", 0);
            l_person = getIntent().getIntExtra("l_person", 0);
            l_type = getIntent().getStringExtra("l_type");

            l_type_old = l_type;
            l_amt_old = Float.parseFloat(mAmt.getText().toString());
            l_from_old = l_fromAcc;
            l_to_old = l_toAcc;

            if (l_type.equals("Debt")) {
                flag = 0;
                l_toAcc=0;
                to_acc=null;
                mType = "Debt";
                mLlFromAcc.setVisibility(View.VISIBLE);
                mLlToAcc.setVisibility(View.GONE);

                if(l_fromAcc!=0) {
                    Cursor c1 = dbHelper.getAccountData(l_fromAcc);
                    c1.moveToFirst();
                    from_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mFromAcc.setText(from_acc);
                    c1.close();
                }
            }
            if (l_type.equals("Loan")) {
                flag = 1;
                l_fromAcc=0;
                from_acc=null;
                mType = "Loan";
                mLlFromAcc.setVisibility(View.GONE);
                mLlToAcc.setVisibility(View.VISIBLE);

                if(l_toAcc!=0) {
                    Cursor c1 = dbHelper.getAccountData(l_toAcc);
                    c1.moveToFirst();
                    to_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mToAcc.setText(to_acc);
                    c1.close();
                }
            }
        }
        else
        {
            l_parent = getIntent().getIntExtra("LD_ID", 0);
            Cursor c = dbHelper.getLoanDebtData(l_parent);
            c.moveToFirst();
            l_type = c.getString(c.getColumnIndex(DBHelper.LOAN_DEBT_COL_TYPE));

            if (l_type.equals("Debt")) {
                flag = 0;
                l_toAcc=0;
                to_acc=null;
                mType = "Debt";
                mLlFromAcc.setVisibility(View.VISIBLE);
                mLlToAcc.setVisibility(View.GONE);

                if(l_fromAcc!=0) {
                    Cursor c1 = dbHelper.getAccountData(l_fromAcc);
                    c1.moveToFirst();
                    from_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mFromAcc.setText(from_acc);
                    c1.close();
                }
            }
            if (l_type.equals("Loan")) {
                flag = 1;
                l_fromAcc=0;
                from_acc=null;
                mType = "Loan";
                mLlFromAcc.setVisibility(View.GONE);
                mLlToAcc.setVisibility(View.VISIBLE);

                if(l_toAcc!=0) {
                    Cursor c1 = dbHelper.getAccountData(l_toAcc);
                    c1.moveToFirst();
                    to_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mToAcc.setText(to_acc);
                    c1.close();
                }
            }
        }
    }

    public void onDateClick(View v)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(DetailedAddLD.this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth)
                    {
                        if(view.isShown())
                            mDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        try {
                            mDate.setText(sdf.format(sdf.parse(mDate.getText().toString())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void onTimeClick(View v)
    { final Calendar c=Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMin = c.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(DetailedAddLD.this, new TimePickerDialog.OnTimeSetListener() {
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

        Intent i=new Intent(DetailedAddLD.this,AccountsViewList.class);
        startActivityForResult(i, 1);
    }

    public void onToAccountClick(View v)
    {
        Intent i=new Intent(DetailedAddLD.this,AccountsViewList.class);
        startActivityForResult(i, 2);
    }

    public void onAmountClick(View v){
        Intent i=new Intent(DetailedAddLD.this,Calculator.class);
        startActivityForResult(i, 3);
    }

    public void onSaveDetailedLD(View v){
        float amt = Float.parseFloat(mAmt.getText().toString());

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
                    amt = Float.parseFloat(mAmt.getText().toString());
                    boolean b = dbHelper.updateLoanDebtData(l_id, l_fromAcc, l_toAcc, l_person, amt, mNote.getText().toString(), l_type,
                            mDate.getText().toString(), mTime.getText().toString(), sp.getInt("UID", 0));

                    if (b) {
                        Toast.makeText(DetailedAddLD.this, "Updated", Toast.LENGTH_LONG).show();

                        Log.i("Types", l_type_old + " : " + l_type);

                        if (l_type_old.equals("Debt"))
                        {
                            Cursor cursor = dbHelper.getAccountData(l_from_old);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal + l_amt_old;

                            dbHelper.updateAccountData(l_from_old, name, bal, note, show, uid);
                            cursor.close();
                        }
                        else if (l_type_old.equals("Loan"))
                        {
                            Cursor cursor = dbHelper.getAccountData(l_to_old);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal - l_amt_old;

                            dbHelper.updateAccountData(l_to_old, name, bal, note, show, uid);
                            cursor.close();
                        }

                        if (l_type.equals("Debt"))
                        {
                            Cursor cursor = dbHelper.getAccountData(l_fromAcc);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal - amt;

                            dbHelper.updateAccountData(l_fromAcc, name, bal, note, show, uid);
                            cursor.close();
                        }
                        else if (l_type.equals("Loan"))
                        {
                            Cursor cursor = dbHelper.getAccountData(l_toAcc);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal + amt;

                            dbHelper.updateAccountData(l_toAcc, name, bal, note, show, uid);
                            cursor.close();
                        }

                        Intent i = new Intent(DetailedAddLD.this, DetailedLoanDebt.class);
                        i.putExtra("LD_ID", l_parent);
                        startActivity(i);
                    } else {
                        Toast.makeText(DetailedAddLD.this, "Error updating Transaction", Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                if (flag == 0) {
                    amt = Float.parseFloat(mAmt.getText().toString());
                    if (mFromAcc.length()>0) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                        boolean b = dbHelper.addLoanDebt(sp.getInt("UID", 0), amt, mDate.getText().toString(), mTime.getText().toString(),
                                acc_id,0,p_id,mNote.getText().toString(), mType, l_parent);
                        if(b) {
                            Toast.makeText(DetailedAddLD.this, "Added", Toast.LENGTH_LONG).show();

                            Cursor cursor = dbHelper.getAccountData(acc_id);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal - amt;

                            dbHelper.updateAccountData(acc_id, name, bal, note, show, uid);
                            cursor.close();

                            Intent intent = new Intent(DetailedAddLD.this, DetailedLoanDebt.class);
                            intent.putExtra("LD_ID", l_parent);
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
                        boolean b = dbHelper.addLoanDebt(sp.getInt("UID", 0), amt, mDate.getText().toString(), mTime.getText().toString(),
                                0, acc_id, p_id, mNote.getText().toString(), mType, l_parent);
                        if (b) {
                            Toast.makeText(DetailedAddLD.this, "Added", Toast.LENGTH_LONG).show();

                            Cursor cursor = dbHelper.getAccountData(acc_id);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal + amt;

                            dbHelper.updateAccountData(acc_id, name, bal, note, show, uid);
                            cursor.close();

                            Intent intent = new Intent(DetailedAddLD.this, DetailedLoanDebt.class);
                            intent.putExtra("LD_ID", l_parent);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        mToAcc.setError("Enter Account");
                    }
                } else {
                    Toast.makeText(DetailedAddLD.this, "Error Adding Transaction", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DetailedAddLD.this, LoanDebtActivity.class);
                    startActivity(intent);
                }
            }
        }
        dbHelper.getAllLoanDebt(sp.getInt("UID", 0), 0);
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
            }
            else if (requestCode == 2) {
                mToAcc.setText(data.getExtras().getString("Acc_Name"));
                to_acc = mToAcc.getText().toString();
                if (mFromAcc.length() <= 0) {
                    mFromAcc.setError("Enter from Account");
                } else {
                    mFromAcc.setError(null);
                }
            }
            else if (requestCode == 3)
                mAmt.setText(data.getExtras().getFloat("RESULT") + "");
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

