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
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Amrit on 3/24/2015.
 */
public class AddTransactionsActivity extends ActionBarActivity {
    int mYear, mMonth, mDay, mHour, mMin, flag = 0, p_id = 0, show = 0;
    Button mDate, mTime, mAmt;
    CheckBox mShow;
    SharedPreferences sp;
    SimpleDateFormat sdf;
    DBHelper dbHelper;
    LinearLayout mLlFrom, mLlTo, mLlCat, mLlPer;
    EditText mFromAcc, mToAcc, mCategory, mPerson, mNote;
    String mType, t_date, t_time, t_type, t_type_old,category;
    float t_amt_old;
    int t_id, t_u_id, t_rec_id, t_category = 0,t_subcategory = 0, t_fromAccount, t_toAccount, t_person, intentFlag, t_from_old, t_to_old;
    Button mExp,mInc,mTrans;
    String from_acc=null,to_acc=null,person=null;

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
        mLlFrom = (LinearLayout)findViewById(R.id.add_trans_ll_from_account);
        mLlTo = (LinearLayout)findViewById(R.id.add_trans_ll_to_account);
        mLlCat = (LinearLayout)findViewById(R.id.add_trans_ll_category);
        mLlPer = (LinearLayout)findViewById(R.id.add_trans_ll_person);

        mType = "Expense";
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMin = calendar.get(Calendar.MINUTE);

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

        mDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mDay).append("-").append(mMonth + 1).append("-")
                .append(mYear));

        sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
            mDate.setText(sdf.format(sdf.parse(mDate.getText().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mTime.setText(new StringBuilder().append(mHour).append(":").append(mMin));//Yes 24 hour time
        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(AddTransactionsActivity.this);
        intentFlag = 0;

        if (getIntent().getIntExtra("t_id",0) >0) {
            intentFlag = 1;
            t_id = getIntent().getIntExtra("t_id", 0);
            t_u_id = getIntent().getIntExtra("t_u_id", 0);
            t_rec_id = getIntent().getIntExtra("t_rec_id", 0);
            t_fromAccount = getIntent().getIntExtra("t_fromAccount", 0);
            t_toAccount = getIntent().getIntExtra("t_toAccount", 0);
            mAmt.setText(getIntent().getFloatExtra("t_bal", 0) + "");
            mNote.setText(getIntent().getStringExtra("t_note"));
            mDate.setText(getIntent().getStringExtra("t_date"));
            mTime.setText(getIntent().getStringExtra("t_time"));
            t_category = getIntent().getIntExtra("t_category", 0);
            t_subcategory = getIntent().getIntExtra("t_subcategory", 0);
            if(t_category!=0)
            {
                Cursor c = dbHelper.getCategoryData(t_category,sp.getInt("UID",0));
                c.moveToFirst();
                String cat = c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME));
                c.close();
                if (t_subcategory != 0)
                {
                    Cursor c1 = dbHelper.getSubCategoryData(t_subcategory, sp.getInt("UID", 0));
                    c1.moveToFirst();
                    String sub = c1.getString(c1.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_NAME));
                    c1.close();
                    mCategory.setText(cat + " " + sub);
                } else
                {
                    mCategory.setText(cat);
                }
            }
            show = getIntent().getIntExtra("t_show", 0);
            t_type = getIntent().getStringExtra("t_type");

            t_type_old = t_type;
            t_amt_old = Float.parseFloat(mAmt.getText().toString());
            t_from_old = t_fromAccount;
            t_to_old = t_toAccount;


            if (t_type.equals("Expense")) {
                flag = 0;
                t_toAccount=0;
                to_acc=null;
                if(t_fromAccount!=0) {
                    Cursor c1 = dbHelper.getAccountData(t_fromAccount);
                    c1.moveToFirst();
                    from_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mFromAcc.setText(from_acc);
                    c1.close();
                }
                t_person = getIntent().getIntExtra("t_person", 0);
                if(t_person!=0) {
                    Cursor c = dbHelper.getPersonData(t_person);
                    c.moveToFirst();
                    person = c.getString(c.getColumnIndex(DBHelper.PERSON_COL_NAME));
                    mPerson.setText(person);
                    c.close();
                }
                onExpenseClick(mExp);

            }
            if (t_type.equals("Income")) {
                flag = 1;
                t_fromAccount=0;
                from_acc=null;
                if(t_toAccount!=0) {
                    Cursor c1 = dbHelper.getAccountData(t_toAccount);
                    c1.moveToFirst();
                    to_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                    mToAcc.setText(to_acc);
                    c1.close();
                }

                t_person = getIntent().getIntExtra("t_person", 0);
                if(t_person!=0) {
                    Cursor c2 = dbHelper.getPersonData(t_person);
                    c2.moveToFirst();
                    person = c2.getString(c2.getColumnIndex(DBHelper.PERSON_COL_NAME));
                    mPerson.setText(person);
                    c2.close();
                }
                onIncomeClick(mInc);
            }
             if (t_type.equals("Transfer")) {
                 flag = 2;
                 t_person=0;
                 t_category = 0;
                 t_subcategory = 0;

                 person=null;
                 if(t_fromAccount!=0) {
                     Cursor c1 = dbHelper.getAccountData(t_fromAccount);
                     c1.moveToFirst();
                     from_acc = c1.getString(c1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                     mFromAcc.setText(from_acc);
                     c1.close();
                 }
                 if(t_toAccount!=0) {
                     Cursor c2 = dbHelper.getAccountData(t_toAccount);
                     c2.moveToFirst();
                      to_acc = c2.getString(c2.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
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
         int acc_id_from,acc_id_to,acc_person;
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

        if(mAmt.getError()==null && mCategory.getError()==null)
        {

            if (intentFlag == 1) {

                if(from_acc!=null)
                {
                    t_fromAccount=dbHelper.getAccountColId(sp.getInt("UID",0),from_acc);
                    acc_id_from=t_fromAccount;
                }
                if(to_acc!=null)
                {
                    t_toAccount=dbHelper.getAccountColId(sp.getInt("UID",0),to_acc);
                }
                if(person!=null)
                {
                    t_person=dbHelper.getPersonColId(sp.getInt("UID",0),person);
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
                    if(t_fromAccount<=0)
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
                    if(t_toAccount<=0)
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
                    t_category = 0;
                    t_subcategory = 0;
                    p_id = 0;
                    mCategory.setError(null);
                    if(t_fromAccount<=0)
                    {
                        mFromAcc.setError("Enter from Account");
                    }
                    else
                    {
                        mFromAcc.setError(null);
                    }
                    if(t_toAccount<=0)
                    {
                        mToAcc.setError("Enter to Account");
                    }
                    else
                    {
                        mToAcc.setError(null);
                    }

                }
                if(mFromAcc.getError()==null && mToAcc.getError()==null && mAmt.getError()==null && mCategory.getError()==null ) {
                    amt = Float.parseFloat(mAmt.getText().toString());
                    boolean b = dbHelper.updateTransactionData(t_id, t_fromAccount, t_toAccount, t_person, t_category, t_subcategory,
                            amt, mNote.getText().toString(), show, t_type, mDate.getText().toString(), mTime.getText().toString(),
                            sp.getInt("UID", 0), t_rec_id);

                    if (b) {
                        Toast.makeText(AddTransactionsActivity.this, "Transaction Updated :" + t_rec_id, Toast.LENGTH_LONG).show();

                        ParseObject transactions = new ParseObject("Transactions");
                        transactions.put("trans_id", t_id);
                        transactions.put("trans_uid", sp.getInt("UID", 0));
                        transactions.put("trans_rec_id", t_rec_id);
                        transactions.put("trans_from_acc", t_fromAccount);
                        transactions.put("trans_to_acc", t_toAccount);
                        transactions.put("trans_show", show);
                        transactions.put("trans_date", mDate.getText().toString());
                        transactions.put("trans_time", mTime.getText().toString());
                        transactions.put("trans_note", mNote.getText().toString());
                        transactions.put("trans_category", t_category);
                        transactions.put("trans_subcategory", t_subcategory);
                        transactions.put("trans_balance", amt);
                        transactions.put("trans_type", t_type);
                        transactions.put("trans_person", t_person);
                        transactions.pinInBackground("pinTransactionsUpdate");

                        Log.i("Types", t_type_old + " : " + t_type);

                        if (t_type_old.equals("Expense"))
                        {
                            Cursor cursor = dbHelper.getAccountData(t_from_old);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal + t_amt_old;

                            dbHelper.updateAccountData(t_from_old, name, bal, note, show, uid);

                            ParseObject account = new ParseObject("Accounts");
                            account.put("acc_id", t_from_old);
                            account.put("acc_uid", uid);
                            account.put("acc_name", name);
                            account.put("acc_balance", bal);
                            account.put("acc_note", note);
                            account.put("acc_show", show);
                            account.pinInBackground("pinAccountsUpdate");

                            cursor.close();
                        }
                        else if (t_type_old.equals("Income"))
                        {
                            Cursor cursor = dbHelper.getAccountData(t_to_old);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal - t_amt_old;

                            dbHelper.updateAccountData(t_to_old, name, bal, note, show, uid);

                            ParseObject account = new ParseObject("Accounts");
                            account.put("acc_id", t_to_old);
                            account.put("acc_uid", uid);
                            account.put("acc_name", name);
                            account.put("acc_balance", bal);
                            account.put("acc_note", note);
                            account.put("acc_show", show);
                            account.pinInBackground("pinAccountsUpdate");

                            cursor.close();
                        }
                        else if (t_type_old.equals("Transfer"))
                        {
                            Cursor cursor = dbHelper.getAccountData(t_to_old);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal - t_amt_old;

                            dbHelper.updateAccountData(t_to_old, name, bal, note, show, uid);

                            ParseObject account = new ParseObject("Accounts");
                            account.put("acc_id", t_to_old);
                            account.put("acc_uid", uid);
                            account.put("acc_name", name);
                            account.put("acc_balance", bal);
                            account.put("acc_note", note);
                            account.put("acc_show", show);
                            account.pinInBackground("pinAccountsUpdate");

                            cursor.close();

                            Cursor cursor1 = dbHelper.getAccountData(t_from_old);
                            cursor1.moveToFirst();

                            float bal1 = cursor1.getFloat(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show1 = cursor1.getInt(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid1 = cursor1.getInt(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal1 = bal1 + t_amt_old;

                            dbHelper.updateAccountData(t_from_old, name1, bal1, note1, show1, uid1);

                            ParseObject account1 = new ParseObject("Accounts");
                            account1.put("acc_id", t_from_old);
                            account1.put("acc_uid", uid1);
                            account1.put("acc_name", name1);
                            account1.put("acc_balance", bal1);
                            account1.put("acc_note", note1);
                            account1.put("acc_show", show1);
                            account1.pinInBackground("pinAccountsUpdate");

                            cursor1.close();
                        }

                        if (t_type.equals("Expense"))
                        {
                            Cursor cursor = dbHelper.getAccountData(t_fromAccount);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal - amt;

                            dbHelper.updateAccountData(t_fromAccount, name, bal, note, show, uid);

                            ParseObject account = new ParseObject("Accounts");
                            account.put("acc_id", t_fromAccount);
                            account.put("acc_uid", uid);
                            account.put("acc_name", name);
                            account.put("acc_balance", bal);
                            account.put("acc_note", note);
                            account.put("acc_show", show);
                            account.pinInBackground("pinAccountsUpdate");

                            cursor.close();
                        }
                        else if (t_type.equals("Income"))
                        {
                            Cursor cursor = dbHelper.getAccountData(t_toAccount);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal + amt;

                            dbHelper.updateAccountData(t_toAccount, name, bal, note, show, uid);

                            ParseObject account = new ParseObject("Accounts");
                            account.put("acc_id", t_toAccount);
                            account.put("acc_uid", uid);
                            account.put("acc_name", name);
                            account.put("acc_balance", bal);
                            account.put("acc_note", note);
                            account.put("acc_show", show);
                            account.pinInBackground("pinAccountsUpdate");

                            cursor.close();
                        }
                        else if (t_type.equals("Transfer"))
                        {
                            Cursor cursor = dbHelper.getAccountData(t_toAccount);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal + amt;

                            dbHelper.updateAccountData(t_toAccount, name, bal, note, show, uid);

                            ParseObject account = new ParseObject("Accounts");
                            account.put("acc_id", t_toAccount);
                            account.put("acc_uid", uid);
                            account.put("acc_name", name);
                            account.put("acc_balance", bal);
                            account.put("acc_note", note);
                            account.put("acc_show", show);
                            account.pinInBackground("pinAccountsUpdate");

                            cursor.close();

                            Cursor cursor1 = dbHelper.getAccountData(t_fromAccount);
                            cursor1.moveToFirst();

                            float bal1 = cursor1.getFloat(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show1 = cursor1.getInt(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid1 = cursor1.getInt(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal1 = bal1 - amt;

                            dbHelper.updateAccountData(t_fromAccount, name1, bal1, note1, show1, uid1);

                            ParseObject account1 = new ParseObject("Accounts");
                            account1.put("acc_id", t_fromAccount);
                            account1.put("acc_uid", uid1);
                            account1.put("acc_name", name1);
                            account1.put("acc_balance", bal1);
                            account1.put("acc_note", note1);
                            account1.put("acc_show", show1);
                            account1.pinInBackground("pinAccountsUpdate");

                            cursor1.close();
                        }

                        if (t_rec_id == 0)
                        {
                            setResult(1);
                            AddTransactionsActivity.this.finish();
                        }
                        else
                        {
                            Intent i = new Intent();
                            i.putExtra("REC_ID", t_rec_id);
                            setResult(1, i);
                            AddTransactionsActivity.this.finish();
                        }
                    } else {
                        Toast.makeText(AddTransactionsActivity.this, "Error updating Transaction", Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                if (flag == 0) {
                    amt = Float.parseFloat(mAmt.getText().toString());
                    if (mFromAcc.length()>0 ) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                        if (mPerson.getText().toString() != null)
                            p_id = dbHelper.getPersonColId(sp.getInt("UID", 0), mPerson.getText().toString());

                        boolean b = dbHelper.addTransaction(sp.getInt("UID", 0), acc_id, 0, amt, mNote.getText().toString(), p_id,
                                t_category, t_subcategory, show, mType, mDate.getText().toString(), mTime.getText().toString(), 0);
                        if(b) {
                            Toast.makeText(AddTransactionsActivity.this, "Transaction Added", Toast.LENGTH_LONG).show();

                            int tid = dbHelper.getTransactionsColId(sp.getInt("UID", 0));

                            ParseObject transactions = new ParseObject("Transactions");
                            transactions.put("trans_id", tid);
                            transactions.put("trans_uid", sp.getInt("UID", 0));
                            transactions.put("trans_rec_id", 0);
                            transactions.put("trans_from_acc", acc_id);
                            transactions.put("trans_to_acc", 0);
                            transactions.put("trans_show", show);
                            transactions.put("trans_date", mDate.getText().toString());
                            transactions.put("trans_time", mTime.getText().toString());
                            transactions.put("trans_note", mNote.getText().toString());
                            transactions.put("trans_category", t_category);
                            transactions.put("trans_subcategory", t_subcategory);
                            transactions.put("trans_balance", amt);
                            transactions.put("trans_type", mType);
                            transactions.put("trans_person", p_id);
                            transactions.pinInBackground("pinTransactions");
                            transactions.saveEventually(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Log.i("Trans saveEventually", "YES! YES! YES!");
                                }
                            });

                            Cursor cursor = dbHelper.getAccountData(acc_id);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal - amt;

                            dbHelper.updateAccountData(acc_id, name, bal, note, show, uid);

                            ParseObject account = new ParseObject("Accounts");
                            account.put("acc_id", acc_id);
                            account.put("acc_uid", uid);
                            account.put("acc_name", name);
                            account.put("acc_balance", bal);
                            account.put("acc_note", note);
                            account.put("acc_show", show);
                            account.pinInBackground("pinAccountsUpdate");

                            cursor.close();

                            Intent intent = new Intent(AddTransactionsActivity.this, TransactionsActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        mFromAcc.setError("Enter Account");
                    }
                } else if (flag == 1) {
                    amt = Float.parseFloat(mAmt.getText().toString());
                    if (mToAcc.length()>0 ) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mToAcc.getText().toString());
                        if (mPerson.getText().toString() != null)
                            p_id = dbHelper.getPersonColId(sp.getInt("UID", 0), mPerson.getText().toString());
                        boolean b = dbHelper.addTransaction(sp.getInt("UID", 0), 0, acc_id, Float.parseFloat(mAmt.getText().toString()),
                                mNote.getText().toString(), p_id, t_category, t_subcategory, show, mType, mDate.getText().toString(),
                                mTime.getText().toString(), 0);
                        if (b) {
                            Toast.makeText(AddTransactionsActivity.this, "Transaction Added", Toast.LENGTH_LONG).show();

                            int tid = dbHelper.getTransactionsColId(sp.getInt("UID", 0));

                            ParseObject transactions = new ParseObject("Transactions");
                            transactions.put("trans_id", tid);
                            transactions.put("trans_uid", sp.getInt("UID", 0));
                            transactions.put("trans_rec_id", 0);
                            transactions.put("trans_from_acc", 0);
                            transactions.put("trans_to_acc", acc_id);
                            transactions.put("trans_show", show);
                            transactions.put("trans_date", mDate.getText().toString());
                            transactions.put("trans_time", mTime.getText().toString());
                            transactions.put("trans_note", mNote.getText().toString());
                            transactions.put("trans_category", t_category);
                            transactions.put("trans_subcategory", t_subcategory);
                            transactions.put("trans_balance", Float.parseFloat(mAmt.getText().toString()));
                            transactions.put("trans_type", mType);
                            transactions.put("trans_person", p_id);
                            transactions.pinInBackground("pinTransactions");
                            transactions.saveEventually(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Log.i("Trans saveEventually", "YES! YES! YES!");
                                }
                            });

                            Cursor cursor = dbHelper.getAccountData(acc_id);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal + amt;

                            dbHelper.updateAccountData(acc_id, name, bal, note, show, uid);

                            ParseObject account = new ParseObject("Accounts");
                            account.put("acc_id", acc_id);
                            account.put("acc_uid", uid);
                            account.put("acc_name", name);
                            account.put("acc_balance", bal);
                            account.put("acc_note", note);
                            account.put("acc_show", show);
                            account.pinInBackground("pinAccountsUpdate");

                            cursor.close();

                            Intent intent = new Intent(AddTransactionsActivity.this, TransactionsActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        mToAcc.setError("Enter Account");
                    }
                } else if (flag == 2) {
                    amt = Float.parseFloat(mAmt.getText().toString());
                    mCategory.setText(null);
                    if (mFromAcc.length()>0 && mToAcc.length()>0 ) {
                        int acc_id = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.getText().toString());
                        int acc_id1 = dbHelper.getAccountColId(sp.getInt("UID", 0), mToAcc.getText().toString());
                        boolean b = dbHelper.addTransaction(sp.getInt("UID", 0), acc_id, acc_id1, Float.parseFloat(mAmt.getText().toString()),
                                mNote.getText().toString(), p_id, t_category, t_subcategory, show, mType, mDate.getText().toString(),
                                mTime.getText().toString(), 0);
                        if (b) {
                            Toast.makeText(AddTransactionsActivity.this, "Transaction Added", Toast.LENGTH_LONG).show();

                            int tid = dbHelper.getTransactionsColId(sp.getInt("UID", 0));

                            ParseObject transactions = new ParseObject("Transactions");
                            transactions.put("trans_id", tid);
                            transactions.put("trans_uid", sp.getInt("UID", 0));
                            transactions.put("trans_rec_id", 0);
                            transactions.put("trans_from_acc", acc_id);
                            transactions.put("trans_to_acc", acc_id1);
                            transactions.put("trans_show", show);
                            transactions.put("trans_date", mDate.getText().toString());
                            transactions.put("trans_time", mTime.getText().toString());
                            transactions.put("trans_note", mNote.getText().toString());
                            transactions.put("trans_category", t_category);
                            transactions.put("trans_subcategory", t_subcategory);
                            transactions.put("trans_balance", Float.parseFloat(mAmt.getText().toString()));
                            transactions.put("trans_type", mType);
                            transactions.put("trans_person", p_id);
                            transactions.pinInBackground("pinTransactions");
                            transactions.saveEventually(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Log.i("Trans saveEventually", "YES! YES! YES!");
                                }
                            });

                            Cursor cursor = dbHelper.getAccountData(acc_id);
                            cursor.moveToFirst();

                            float bal = cursor.getFloat(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal = bal - amt;

                            dbHelper.updateAccountData(acc_id, name, bal, note, show, uid);

                            ParseObject account = new ParseObject("Accounts");
                            account.put("acc_id", acc_id);
                            account.put("acc_uid", uid);
                            account.put("acc_name", name);
                            account.put("acc_balance", bal);
                            account.put("acc_note", note);
                            account.put("acc_show", show);
                            account.pinInBackground("pinAccountsUpdate");

                            cursor.close();

                            Cursor cursor1 = dbHelper.getAccountData(acc_id1);
                            cursor1.moveToFirst();

                            float bal1 = cursor1.getFloat(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_BALANCE));
                            String name1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NAME));
                            String note1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_NOTE));
                            int show1 = cursor1.getInt(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_SHOW));
                            int uid1 = cursor1.getInt(cursor1.getColumnIndex(DBHelper.ACCOUNTS_COL_ACC_UID));

                            bal1 = bal1 + amt;

                            dbHelper.updateAccountData(acc_id1, name1, bal1, note1, show1, uid1);

                            ParseObject account1 = new ParseObject("Accounts");
                            account1.put("acc_id", acc_id1);
                            account1.put("acc_uid", uid1);
                            account1.put("acc_name", name1);
                            account1.put("acc_balance", bal1);
                            account1.put("acc_note", note1);
                            account1.put("acc_show", show1);
                            account1.pinInBackground("pinAccountsUpdate");

                            cursor1.close();

                            Intent intent = new Intent(AddTransactionsActivity.this, TransactionsActivity.class);
                            startActivity(intent);
                        }
                    }

                } else {
                    Toast.makeText(AddTransactionsActivity.this, "Error Adding Transaction", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddTransactionsActivity.this, TransactionsActivity.class);
                    startActivity(intent);
                }
            }
        }

        dbHelper.getAllTransactions(sp.getInt("UID", 0));
    }


    public void onExpenseClick(View v)
    {
        mExp.setPressed(true);
        mLlFrom.setVisibility(View.VISIBLE);
        mLlTo.setVisibility(View.GONE);
        mLlCat.setVisibility(View.VISIBLE);
        mLlPer.setVisibility(View.VISIBLE);
        mType="Expense";
        t_type="Expense";
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
        t_type="Income";
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
    public void onCategoryClick(View v)
    {
        Intent i=new Intent(AddTransactionsActivity.this,CategoriesViewList.class);
        startActivityForResult(i, 5);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            if (requestCode == 1) {
                mFromAcc.setText(data.getExtras().getString("Acc_Name"));
                t_fromAccount = dbHelper.getAccountColId(sp.getInt("UID", 0), mFromAcc.toString());
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
                t_toAccount = dbHelper.getAccountColId(sp.getInt("UID", 0), mToAcc.toString());
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
            else if(requestCode==5)
            {
                category=data.getExtras().getString("Category_Id");
                Log.i("Category_Id: in trans", category);
                if(category.contains("."))
                {
                    String cat[]=category.split(".");
                    t_category=Integer.parseInt(cat[0]);
                    t_subcategory=Integer.parseInt(cat[1]);
                    Cursor c=dbHelper.getCategoryData(t_category,sp.getInt("UID",0));
                    c.moveToFirst();
                    String category1 = c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME));
                    c.close();
                    Cursor c1= dbHelper.getSubCategoryData(t_subcategory,sp.getInt("UID",0));
                    c1.moveToFirst();
                    String sub=c1.getString(c1.getColumnIndex(DBHelper.SUBCATEGORY_COL_SUB_NAME));
                    c1.close();
                    mCategory.setText(category1+" "+sub);
                }
                else
                {
                    t_category=Integer.parseInt(category);
                    t_subcategory=0;
                    Cursor c= dbHelper.getCategoryData(t_category,sp.getInt("UID",0));
                    c.moveToFirst();
                    mCategory.setText(c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME)));
                    c.close();

                }
            }
        }

    }    @Override
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

