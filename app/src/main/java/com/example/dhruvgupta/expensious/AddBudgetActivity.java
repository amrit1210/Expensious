package com.example.dhruvgupta.expensious;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Gaurav on 04-Apr-15.
 */
public class AddBudgetActivity extends ActionBarActivity
{
    int mYear, mMonth, mDay;
    Button mStartDate, mEndDate, mAmt;
    EditText mAccount;
    DBHelper dbHelper;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        mStartDate=(Button)findViewById(R.id.add_budget_btn_start_date);
        mEndDate=(Button)findViewById(R.id.add_budget_btn_end_date);
        mAmt=(Button)findViewById(R.id.add_budget_btn_amt);
        mAccount=(EditText)findViewById(R.id.add_budget_account);

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        mStartDate.setText(new StringBuilder().append(mDay).append("-").append(mMonth + 1).append("-").append(mYear));
        mEndDate.setText(new StringBuilder().append(mDay).append("-").append(mMonth + 1).append("-").append(mYear));
        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(AddBudgetActivity.this);
    }

    public void onStartDateClick(View v)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddBudgetActivity.this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth)
                    {
                        if(view.isShown())
                            mStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void onEndDateClick(View v)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddBudgetActivity.this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth)
                    {
                        if(view.isShown())
                            mEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void onAccountClick(View v)
    {
        Intent i=new Intent(AddBudgetActivity.this,AccountsViewList.class);
        startActivityForResult(i,1);
    }

    public void onAmountClick(View v)
    {
        Intent i=new Intent(AddBudgetActivity.this,Calculator.class);
        startActivityForResult(i,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1)
        {
            if(requestCode == 1)
            {
                mAccount.setText(data.getExtras().getString("Acc_Name"));
                if (mAccount.length() <= 0)
                {
                    mAccount.setError("Enter Account");
                } else
                {
                    mAccount.setError(null);
                }
            }
            else if (requestCode == 2)
            {
                mAmt.setText(data.getExtras().getFloat("RESULT") + "");
            }
        }
    }
}
