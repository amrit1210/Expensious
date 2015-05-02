package com.example.dhruvgupta.expensious;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dhruvgupta on 4/6/2015.
 */
public class AddRecursiveActivity extends ActionBarActivity {

    int mYear, mMonth, mDay, mHour, mMin, flag = 0, p_id = 0, show = 1;
    Button mStartDate, mEndDate, mTime, mAmt;
    Spinner mRec;
    Switch mAlert;
    CheckBox mShow;
    String mNextDate = null;
    Date start, end, next = null;
    SharedPreferences sp;
    DBHelper dbHelper;
    SimpleDateFormat sdf;
    LinearLayout mLlFrom, mLlTo, mLlCat, mLlPer;
    EditText mFromAcc, mToAcc, mCategory, mPerson, mNote;
    String mType, rec_date, rec_time, rec_type,category;
    int rec_id, rec_u_id, rec_category, rec_subcategory, rec_fromAccount, rec_toAccount, rec_person, rec_recurring, rec_alert, intentFlag;
    Button mExp,mInc,mTrans;
    String from_acc=null,to_acc=null,person=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recursive);
        this.getSupportActionBar().setTitle("Add Recursive");
        mStartDate = (Button) findViewById(R.id.add_recursive_btn_start_date);
        mEndDate = (Button) findViewById(R.id.add_recursive_btn_end_date);
        mTime = (Button) findViewById(R.id.add_recursive_btn_time);
        mAmt = (Button) findViewById(R.id.add_recursive_btn_amt);
        mFromAcc = (EditText) findViewById(R.id.add_recursive_from_account);
        mToAcc = (EditText) findViewById(R.id.add_recursive_to_account);
        mCategory = (EditText) findViewById(R.id.add_recursive_category);
        mPerson = (EditText) findViewById(R.id.add_recursive_person);
        mNote = (EditText) findViewById(R.id.add_recursive_note);
        mShow = (CheckBox) findViewById(R.id.add_recursive_cb);
        mExp=(Button)findViewById(R.id.add_recursive_btn_expense);
        mInc=(Button)findViewById(R.id.add_recursive_btn_income);
        mTrans=(Button)findViewById(R.id.add_recursive_btn_transfer);
        mRec = (Spinner) findViewById(R.id.spinner_recursive);
        mAlert = (Switch)findViewById(R.id.switch_alert);
        mLlFrom = (LinearLayout)findViewById(R.id.add_recursive_ll_from_account);
        mLlTo = (LinearLayout)findViewById(R.id.add_recursive_ll_to_account);
        mLlCat = (LinearLayout)findViewById(R.id.add_recursive_ll_category);
        mLlPer = (LinearLayout)findViewById(R.id.add_recursive_ll_person);

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

        mPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPersonClick(v);
            }
        });

        mCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryClick(v);
            }
        });

        mType = "Expense";
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMin = calendar.get(Calendar.MINUTE);
        mStartDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mDay).append("-").append(mMonth + 1).append("-")
                .append(mYear));
        mEndDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mDay+1).append("-").append(mMonth + 1).append("-")
                .append(mYear));

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            start = sdf.parse(mStartDate.getText().toString());
            end = sdf.parse(mEndDate.getText().toString());
            next = sdf.parse(mStartDate.getText().toString());
            mNextDate = sdf.format(next);
            mStartDate.setText(mNextDate);
            mEndDate.setText(sdf.format(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mTime.setText(new StringBuilder().append(mHour).append(":").append(mMin));//Yes 24 hour time
        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(AddRecursiveActivity.this);
        intentFlag = 0;

        mRec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rec_recurring = (int) mRec.getSelectedItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (getIntent().getIntExtra("rec_id",0) >0) {
            intentFlag = 1;
            rec_id = getIntent().getIntExtra("rec_id", 0);
            rec_u_id = getIntent().getIntExtra("rec_u_id", 0);
            rec_fromAccount = getIntent().getIntExtra("rec_fromAccount", 0);
            rec_toAccount = getIntent().getIntExtra("rec_toAccount", 0);
            mAmt.setText(getIntent().getFloatExtra("rec_bal", 0) + "");
            mNote.setText(getIntent().getStringExtra("rec_note"));
            mStartDate.setText(getIntent().getStringExtra("rec_start_date"));
            mEndDate.setText(getIntent().getStringExtra("rec_end_date"));
            mNextDate = getIntent().getStringExtra("rec_next_date");
            mTime.setText(getIntent().getStringExtra("rec_time"));
            rec_category = getIntent().getIntExtra("rec_category", 0);
            rec_subcategory = getIntent().getIntExtra("rec_subcategory",0);
            if(rec_category!=0)
            {
                Cursor c=dbHelper.getCategoryData(rec_category,sp.getInt("UID",0));
                c.moveToFirst();
                String cat=c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME));
                Log.i("cat recur:",cat);
                c.close();
                if(rec_subcategory!=0)
                {
                    Cursor c1=dbHelper.getSubCategoryData(rec_subcategory,sp.getInt("UID",0));
                    c1.moveToFirst();
                    String sub=c1.getString(c1.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_NAME));
                    c1.close();
                    mCategory.setText(cat+" "+sub);
                }
                else
                {
                    mCategory.setText(cat);
                }
            }
            show = getIntent().getIntExtra("rec_show", 0);
            rec_alert = getIntent().getIntExtra("rec_alert", 0);
            rec_type = getIntent().getStringExtra("rec_type");
            rec_recurring = getIntent().getIntExtra("rec_recurring", 0);


            if (rec_type.equals("Expense")) {
                flag = 0;
                rec_toAccount =0;
                to_acc=null;
                if(rec_fromAccount !=0) {
                    Cursor c1 = dbHelper.getAccountData(rec_fromAccount);
                    c1.moveToFirst();
                    from_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mFromAcc.setText(from_acc);
                    c1.close();
                }
                rec_person = getIntent().getIntExtra("rec_person", 0);
                if(rec_person !=0) {
                    Cursor c = dbHelper.getPersonData(rec_person, sp.getInt("UID", 0));
                    c.moveToFirst();
                    person = c.getString(c.getColumnIndex(DBHelper.PERSON_COL_NAME));
                    mPerson.setText(person);
                    c.close();
                }
                onExpenseClick(mExp);

            }
            if (rec_type.equals("Income")) {
                flag = 1;
                rec_fromAccount =0;
                from_acc=null;
                if(rec_toAccount !=0) {
                    Cursor c1 = dbHelper.getAccountData(rec_toAccount);
                    c1.moveToFirst();
                    to_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mToAcc.setText(to_acc);
                    c1.close();
                }

                rec_person = getIntent().getIntExtra("rec_person", 0);
                if(rec_person !=0) {
                    Cursor c2 = dbHelper.getPersonData(rec_person, sp.getInt("UID", 0));
                    c2.moveToFirst();
                    person = c2.getString(c2.getColumnIndex(DBHelper.PERSON_COL_NAME));
                    mPerson.setText(person);
                    c2.close();
                }
                onIncomeClick(mInc);
            }
            if (rec_type.equals("Transfer")) {
                flag = 2;
                rec_person =0;
                person=null;
                if(rec_fromAccount !=0) {
                    Cursor c1 = dbHelper.getAccountData(rec_fromAccount);
                    c1.moveToFirst();
                    from_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mFromAcc.setText(from_acc);
                    c1.close();
                }
                if(rec_toAccount !=0) {
                    Cursor c2 = dbHelper.getAccountData(rec_toAccount);
                    c2.moveToFirst();
                    to_acc = c2.getString(c2.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mToAcc.setText(to_acc);
                    c2.close();
                }
                onTransferClick(mTrans);
            }

            if (show == 1) {
                mShow.setChecked(true);
            }
            else
            {
                mShow.setChecked(false);
            }

            if (rec_alert == 1)
            {
                mAlert.setChecked(true);
            }
            else
            {
                mAlert.setChecked(false);
            }

            mRec.setSelection(rec_recurring);
        }
    }

    public void onSaveRecursive () {
        float amt = Float.parseFloat(mAmt.getText().toString());
        int dateFlag=0;

        if (mShow.isChecked()) {
            show = 1;
        } else {
            show = 0;
        }

        Log.i("Date", start + " : "+ end + " : " + end.after(start));

        if(mAlert.isChecked())
        {
            rec_alert = 1;
        }
        else
        {
            rec_alert = 0;
        }

        rec_recurring = (int) mRec.getSelectedItemId();

        if (amt <= 0) {
            mAmt.setError("Enter amount");
        }
        else
        {
            mAmt.setError(null);
        }

        if (end.after(start))
        {
            dateFlag = 1;
        }
        else
        {
            dateFlag = 0;
            Toast.makeText(this, "End date should be greater than start date",Toast.LENGTH_SHORT).show();
        }
        if(flag==0 || flag ==1)
        {
            if (mCategory.length() == 0) {
                mCategory.setError("Enter Category");
            } else {
                mCategory.setError(null);
            }
        }

        if(flag==2)
        {
            mCategory.setError(null);
        }

        if(mAmt.getError()==null && dateFlag == 1&& mCategory.getError()==null)
        {

            if (intentFlag == 1) {

                if(from_acc!=null)
                {
                    rec_fromAccount =dbHelper.getAccountColId(sp.getInt("UID",0),from_acc);
                }
                if(to_acc!=null)
                {
                    rec_toAccount =dbHelper.getAccountColId(sp.getInt("UID",0),to_acc);
                }
                if(person!=null)
                {
                    rec_person =dbHelper.getPersonColId(sp.getInt("UID",0),person);
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
                    if(rec_fromAccount <=0)
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
                    if(rec_toAccount <=0)
                    {
                        mToAcc.setError("Enter to Account");
                    }
                    else
                    {
                        mToAcc.setError(null);
                    }

                }
                else if(flag==2)
                {
                    rec_category = 0;
                    rec_subcategory = 0;
                    p_id = 0;
                    mCategory.setError(null);
                    if(rec_fromAccount <=0)
                    {
                        mFromAcc.setError("Enter from Account");
                    }
                    else
                    {
                        mFromAcc.setError(null);
                    }
                    if(rec_toAccount <=0)
                    {
                        mToAcc.setError("Enter to Account");
                    }
                    else
                    {
                        mToAcc.setError(null);
                    }

                }
                if(mFromAcc.getError()==null && mToAcc.getError()==null && mAmt.getError()==null) {
                    boolean b = dbHelper.updateRecursiveData(rec_id, rec_fromAccount, rec_toAccount, rec_person, rec_category,
                            rec_subcategory, amt, mNote.getText().toString(), show, rec_type, mStartDate.getText().toString(),
                            mEndDate.getText().toString(), mNextDate, mTime.getText().toString(),
                            rec_recurring, rec_alert, sp.getInt("UID", 0));

                    if (b) {
                        Toast.makeText(AddRecursiveActivity.this, "Recursive Transaction Updated", Toast.LENGTH_LONG).show();
                        startService(new Intent(this, RecursiveService.class));

                        ParseObject recursive = new ParseObject("Recursive");
                        recursive.put("rec_id", rec_id);
                        recursive.put("rec_uid", sp.getInt("UID", 0));
                        recursive.put("rec_from_acc", rec_fromAccount);
                        recursive.put("rec_to_acc", rec_toAccount);
                        recursive.put("rec_start_date", mStartDate.getText().toString());
                        recursive.put("rec_end_date", mEndDate.getText().toString());
                        recursive.put("rec_next_date", mNextDate);
                        recursive.put("rec_time", mTime.getText().toString());
                        recursive.put("rec_recurring", rec_recurring);
                        recursive.put("rec_alert", rec_alert);
                        recursive.put("rec_category", rec_category);
                        recursive.put("rec_subcategory", rec_subcategory);
                        recursive.put("rec_type", rec_type);
                        recursive.put("rec_note", mNote.getText().toString());
                        recursive.put("rec_person", rec_person);
                        recursive.put("rec_balance", amt);
                        recursive.put("rec_show", show);
                        recursive.pinInBackground("pinRecursiveUpdate");

                        Intent intent = new Intent(AddRecursiveActivity.this, RecursiveActivity.class);
                        startActivity(intent);
                        this.finish();
                    } else {
                        Toast.makeText(AddRecursiveActivity.this, "Error updating Recursive Transaction", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(AddRecursiveActivity.this, RecursiveActivity.class);
                    startActivity(intent);
                    this.finish();
                }

            } else {
                if (flag == 0) {
                    amt = Float.parseFloat(mAmt.getText().toString());
                    if (mFromAcc.length()>0) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                        if (mPerson.getText().toString() != null)
                            p_id = dbHelper.getPersonColId(sp.getInt("UID", 0), mPerson.getText().toString());
                        boolean b = dbHelper.addRecursive(sp.getInt("UID", 0), acc_id, 0, Float.parseFloat(mAmt.getText().toString()),
                                mNote.getText().toString(), p_id,rec_category,rec_subcategory, show, mType, mStartDate.getText().toString(),
                                mEndDate.getText().toString(),mNextDate, mTime.getText().toString(), rec_recurring, rec_alert);
                        if(b) {
                            Toast.makeText(AddRecursiveActivity.this, "Recursive Transaction Added", Toast.LENGTH_LONG).show();
                            startService(new Intent(this, RecursiveService.class));

                            int recid = dbHelper.getRecursiveColId(sp.getInt("UID", 0));

                            ParseObject recursive = new ParseObject("Recursive");
                            recursive.put("rec_id", recid);
                            recursive.put("rec_uid", sp.getInt("UID", 0));
                            recursive.put("rec_from_acc", acc_id);
                            recursive.put("rec_to_acc", 0);
                            recursive.put("rec_start_date", mStartDate.getText().toString());
                            recursive.put("rec_end_date", mEndDate.getText().toString());
                            recursive.put("rec_next_date", mNextDate);
                            recursive.put("rec_time", mTime.getText().toString());
                            recursive.put("rec_recurring", rec_recurring);
                            recursive.put("rec_alert", rec_alert);
                            recursive.put("rec_category", rec_category);
                            recursive.put("rec_subcategory", rec_subcategory);
                            recursive.put("rec_type", mType);
                            recursive.put("rec_note", mNote.getText().toString());
                            recursive.put("rec_person", rec_person);
                            recursive.put("rec_balance", Float.parseFloat(mAmt.getText().toString()));
                            recursive.put("rec_show", show);
                            recursive.pinInBackground("pinRecursive");
                            recursive.saveEventually(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Log.i("Rec saveEventually", "YES! YES! YES!");
                                }
                            });

                            Intent intent = new Intent(AddRecursiveActivity.this, RecursiveActivity.class);
                            startActivity(intent);
                            this.finish();
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
                        boolean b = dbHelper.addRecursive(sp.getInt("UID", 0), 0, acc_id, Float.parseFloat(mAmt.getText().toString()),
                                mNote.getText().toString(), p_id,rec_category,rec_subcategory, show, mType, mStartDate.getText().toString(),
                                mEndDate.getText().toString(),mNextDate, mTime.getText().toString(), rec_recurring, rec_alert);
                        if (b) {
                            Toast.makeText(AddRecursiveActivity.this, "Recursive Transaction Added", Toast.LENGTH_LONG).show();
                            startService(new Intent(this, RecursiveService.class));

                            int recid = dbHelper.getRecursiveColId(sp.getInt("UID", 0));

                            ParseObject recursive = new ParseObject("Recursive");
                            recursive.put("rec_id", recid);
                            recursive.put("rec_uid", sp.getInt("UID", 0));
                            recursive.put("rec_from_acc", 0);
                            recursive.put("rec_to_acc", acc_id);
                            recursive.put("rec_start_date", mStartDate.getText().toString());
                            recursive.put("rec_end_date", mEndDate.getText().toString());
                            recursive.put("rec_next_date", mNextDate);
                            recursive.put("rec_time", mTime.getText().toString());
                            recursive.put("rec_recurring", rec_recurring);
                            recursive.put("rec_alert", rec_alert);
                            recursive.put("rec_category", rec_category);
                            recursive.put("rec_subcategory", rec_subcategory);
                            recursive.put("rec_type", mType);
                            recursive.put("rec_note", mNote.getText().toString());
                            recursive.put("rec_person", rec_person);
                            recursive.put("rec_balance", Float.parseFloat(mAmt.getText().toString()));
                            recursive.put("rec_show", show);
                            recursive.pinInBackground("pinRecursive");
                            recursive.saveEventually(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Log.i("Rec saveEventually", "YES! YES! YES!");
                                }
                            });

                            Intent intent = new Intent(AddRecursiveActivity.this, RecursiveActivity.class);
                            startActivity(intent);
                            this.finish();
                        }
                    }
                    else
                    {
                        mToAcc.setError("Enter Account");
                    }
                } else if (flag == 2) {
                    if (mFromAcc.length()>0 && mToAcc.length()>0) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                        int acc_id1 = dbHelper.getAccountColId(sp.getInt("UID", 0), mToAcc.getText().toString());
                        boolean b = dbHelper.addRecursive(sp.getInt("UID", 0), acc_id, acc_id1, Float.parseFloat(mAmt.getText().toString()),
                                mNote.getText().toString(), p_id, rec_category,rec_subcategory, show, mType, mStartDate.getText().toString(),
                                mEndDate.getText().toString(),mNextDate, mTime.getText().toString(), rec_recurring, rec_alert);
                        if (b) {
                            Toast.makeText(AddRecursiveActivity.this, "Recursive Transaction Added", Toast.LENGTH_LONG).show();
                            startService(new Intent(this, RecursiveService.class));

                            int recid = dbHelper.getRecursiveColId(sp.getInt("UID", 0));

                            ParseObject recursive = new ParseObject("Recursive");
                            recursive.put("rec_id", recid);
                            recursive.put("rec_uid", sp.getInt("UID", 0));
                            recursive.put("rec_from_acc", acc_id);
                            recursive.put("rec_to_acc", acc_id1);
                            recursive.put("rec_start_date", mStartDate.getText().toString());
                            recursive.put("rec_end_date", mEndDate.getText().toString());
                            recursive.put("rec_next_date", mNextDate);
                            recursive.put("rec_time", mTime.getText().toString());
                            recursive.put("rec_recurring", rec_recurring);
                            recursive.put("rec_alert", rec_alert);
                            recursive.put("rec_category", rec_category);
                            recursive.put("rec_subcategory", rec_subcategory);
                            recursive.put("rec_type", mType);
                            recursive.put("rec_note", mNote.getText().toString());
                            recursive.put("rec_person", rec_person);
                            recursive.put("rec_balance", Float.parseFloat(mAmt.getText().toString()));
                            recursive.put("rec_show", show);
                            recursive.pinInBackground("pinRecursive");
                            recursive.saveEventually(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Log.i("Rec saveEventually", "YES! YES! YES!");
                                }
                            });

                            Intent intent = new Intent(AddRecursiveActivity.this, RecursiveActivity.class);
                            startActivity(intent);
                            this.finish();
                        }
                    }

                } else {
                    Toast.makeText(AddRecursiveActivity.this, "Error Adding Recursive Transaction", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddRecursiveActivity.this, RecursiveActivity.class);
                    startActivity(intent);
                    this.finish();
                }

            }
        }

        dbHelper.getAllRecursive(sp.getInt("UID", 0));
    }

    public void onExpenseClick(View v)
    {
        mExp.setPressed(true);
        mLlFrom.setVisibility(View.VISIBLE);
        mLlTo.setVisibility(View.GONE);
        mLlCat.setVisibility(View.VISIBLE);
        mLlPer.setVisibility(View.VISIBLE);
        mType="Expense";
        rec_type ="Expense";
        flag=0;
    }

    public void onIncomeClick(View v)
    {
        mInc.setPressed(true);
        mLlFrom.setVisibility(View.GONE);
        mLlTo.setVisibility(View.VISIBLE);
        mLlCat.setVisibility(View.VISIBLE);
        mLlPer.setVisibility(View.VISIBLE);
        mType="Income";
        rec_type ="Income";
        flag =1;

    }

    public void onTransferClick(View v)
    {
        mTrans.setPressed(true);
        mLlFrom.setVisibility(View.VISIBLE);
        mLlTo.setVisibility(View.VISIBLE);
        mLlCat.setVisibility(View.GONE);
        mLlPer.setVisibility(View.GONE);
        mType="Transfer";
        rec_type ="Transfer";
        flag=2;
    }

    public void onStartDateClick(View v) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddRecursiveActivity.this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth)
                    {
                        if(view.isShown())
                            mStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        try {
                            start = sdf.parse(mStartDate.getText().toString());
                            next = sdf.parse(mStartDate.getText().toString());
                            mNextDate = sdf.format(next);
                            mStartDate.setText(mNextDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);

       dpd.show();
    }

    public void onEndDateClick(View v) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddRecursiveActivity.this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth)
                    {
                        if(view.isShown())
                            mEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        try {
                            end = sdf.parse(mEndDate.getText().toString());
                            mEndDate.setText(sdf.format(end));
                            Log.i("end", mEndDate.getText().toString() + " : " + end);
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
        mTimePicker = new TimePickerDialog(AddRecursiveActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        Intent i=new Intent(AddRecursiveActivity.this,AccountsViewList.class);
        startActivityForResult(i, 1);
    }

    public void onToAccountClick(View v)
    {
        Intent i=new Intent(AddRecursiveActivity.this,AccountsViewList.class);
        startActivityForResult(i, 2);
    }

    public void onAmountClick(View v){
        Intent i=new Intent(AddRecursiveActivity.this,Calculator.class);
        startActivityForResult(i, 3);
    }
    public void onPersonClick(View v)
    {
        Intent i=new Intent(AddRecursiveActivity.this,PersonsViewList.class);
        startActivityForResult(i, 4);
    }
    public void onCategoryClick(View v)
    {
        Intent i=new Intent(AddRecursiveActivity.this,CategoriesViewList.class);
        startActivityForResult(i,5);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            if (requestCode == 1) {
                mFromAcc.setText(data.getExtras().getString("Acc_Name"));
                from_acc = mFromAcc.getText().toString();
                if(mFromAcc.length()<=0)
                {
                    mFromAcc.setError("Enter from Account");
                }
                else
                {
                    mFromAcc.setError(null);
                }
            } else if (requestCode == 2) {
                mToAcc.setText(data.getExtras().getString("Acc_Name"));
                to_acc = mToAcc.getText().toString();
                if(mFromAcc.length()<=0)
                {
                    mFromAcc.setError("Enter from Account");
                }
                else
                {
                    mFromAcc.setError(null);
                }
            } else if (requestCode == 3)
                mAmt.setText(data.getExtras().getFloat("RESULT") + "");
            else if (requestCode == 4) {
                mPerson.setText(data.getExtras().getString("Person_Name"));
                person = mPerson.getText().toString();
            }
            else if(requestCode == 5)
            {
                category=data.getExtras().getString("Category_Id");
                if(category.contains("."))
                {
                    String cat[]=category.split("\\.");
                    rec_category=Integer.parseInt(cat[0]);
                    rec_subcategory=Integer.parseInt(cat[1]);
                    Cursor c=dbHelper.getCategoryData(rec_category,sp.getInt("UID",0));
                    c.moveToFirst();
                    String category=c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME));
                    c.close();
                    Cursor c1= dbHelper.getSubCategoryData(rec_subcategory,sp.getInt("UID",0));
                    c1.moveToFirst();
                    String sub=c1.getString(c1.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_NAME));
                    c1.close();
                    mCategory.setText(category+"\\"+sub);
                }
                else
                {
                    rec_category=Integer.parseInt(category);
                    rec_subcategory=0;
                    Cursor c= dbHelper.getCategoryData(rec_category,sp.getInt("UID",0));
                    c.moveToFirst();
                    mCategory.setText(c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME)));
                    Log.i("rec_cat:",mCategory.getText().toString());
                    c.close();

                }
            }
        }

    }    @Override
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
;
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_done)
        {
            onSaveRecursive();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

