package com.example.dhruvgupta.expensious;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gaurav on 04-Apr-15.
 */
public class AddBudgetActivity extends ActionBarActivity
{
    int mYear, mMonth, mDay;
    Button mB_StartDate, mB_EndDate, mB_Amt, mB_Cur;
    DBHelper dbHelper;
    Date start,end,startDb,endDb;
    SimpleDateFormat sdf;
    SharedPreferences sp;
    int flag,b_id,b_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        mB_StartDate =(Button)findViewById(R.id.add_budget_btn_start_date);
        mB_EndDate =(Button)findViewById(R.id.add_budget_btn_end_date);
        mB_Amt =(Button)findViewById(R.id.add_budget_btn_amt);
        mB_Cur =(Button)findViewById(R.id.add_budget_btn_cur);

        sdf= new SimpleDateFormat("dd-MM-yyyy");

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        mB_StartDate.setText(new StringBuilder().append(mDay).append("-").append(mMonth + 1).append("-").append(mYear));
        mB_EndDate.setText(new StringBuilder().append(mDay+1).append("-").append(mMonth + 1).append("-").append(mYear));

        try
        {
            start = sdf.parse(mB_StartDate.getText().toString());
            end = sdf.parse(mB_EndDate.getText().toString());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(AddBudgetActivity.this);

        flag=0;

        if(getIntent().getStringExtra("b_cur")!=null)
        {
            flag=1;
            b_id=getIntent().getIntExtra("b_id",0);
            b_uid=getIntent().getIntExtra("b_uid",0);
            mB_Amt.setText(getIntent().getFloatExtra("b_amt", 0) + "");
            mB_Cur.setText(getIntent().getStringExtra("b_cur"));
            mB_StartDate.setText(getIntent().getStringExtra("b_sDate"));
            mB_EndDate.setText(getIntent().getStringExtra("b_eDate"));
            try
            {
                startDb = sdf.parse(mB_StartDate.getText().toString());
                endDb = sdf.parse(mB_EndDate.getText().toString());
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
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
                            mB_StartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        try
                        {
                            start = sdf.parse(mB_StartDate.getText().toString());
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
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
                            mB_EndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        try
                        {
                            end = sdf.parse(mB_EndDate.getText().toString());
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void onBudgetAmountClick(View v)
    {
        Intent i=new Intent(AddBudgetActivity.this,Calculator.class);
        startActivityForResult(i,1);
    }

    public void onBudgetCurClick(View v)
    {
        Intent i = new Intent(AddBudgetActivity.this, CurrencyViewList.class);
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
                mB_Amt.setText(data.getExtras().getFloat("RESULT") + "");
            }
            else if (requestCode == 2)
            {
                mB_Cur.setText(data.getExtras().getString("CUR_SYM",""));
            }
        }
    }

    public void onSaveBudget(View v)
    {
        ArrayList<BudgetDB> al = dbHelper.getAllBudgets(sp.getInt("UID",0));

        if(end.after(start))
        {
            if (mB_Amt.getText().toString().equals("0"))
            {
                mB_Amt.setError("Enter Amount");
            }
            else
            {
                mB_Amt.setError(null);
            }
            if (mB_Amt.getError() == null && mB_Cur.getError() == null)
            {
                Log.i("Flag", flag + "");
                if (flag == 1)
                {
                    if (dbHelper.updateBudgetData(sp.getInt("UID", 0), b_id, Float.parseFloat(mB_Amt.getText().toString()),
                            mB_Cur.getText().toString(), mB_StartDate.getText().toString(), mB_EndDate.getText().toString())) {
                        Toast.makeText(AddBudgetActivity.this, "Budget Updated", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddBudgetActivity.this, BudgetsActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(AddBudgetActivity.this, "Error updating Budget", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddBudgetActivity.this, BudgetsActivity.class);
                        startActivity(intent);
                    }
                }
                else
                {
                    if (dbHelper.addBudget(sp.getInt("UID", 0), Float.parseFloat(mB_Amt.getText().toString()),
                            mB_Cur.getText().toString(), mB_StartDate.getText().toString(), mB_EndDate.getText().toString())) {
                        Toast.makeText(AddBudgetActivity.this, "Budget Created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddBudgetActivity.this, BudgetsActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(AddBudgetActivity.this, "Error creating Budget", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddBudgetActivity.this, BudgetsActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }
    }
}
