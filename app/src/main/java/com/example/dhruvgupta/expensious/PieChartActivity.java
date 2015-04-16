package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Gaurav on 4/14/2015.
 */
public class PieChartActivity extends ActionBarActivity implements OnChartValueSelectedListener{
    PieChart mPie;
    Legend legend;
    DBHelper dbHelper;
    SharedPreferences sp;
    ArrayList<AccountsDB> allAccounts;
    ArrayList<TransactionsDB> allTransactions;
    ArrayList<PersonDB> allPersons;
    ListView mLv_Pie;
    AccountsDB accountsDB;
    PersonDB personDB;
    TransactionsDB transactionsDB;
    Iterator<AccountsDB> accountsDBIterator;
    Iterator<TransactionsDB> transactionsDBIterator;
    Iterator<PersonDB> personDBIterator;
    ArrayList<Float> mAmt,mAmt_Person;
    ArrayList<String> mAcc;
    ArrayList<String> mPer;
    ReportsAccountsAdapter reportsAccountsAdapter;
    ReportsPersonsAdapter reportsPersonsAdapter;
    int actionId=0;
    float mAmount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mPie = (PieChart) findViewById(R.id.report_chart);
        mLv_Pie= (ListView) findViewById(R.id.report_list);
        mAcc=new ArrayList<>();
        mPer=new ArrayList<>();
        mAmt=new ArrayList<>();
        mAmt_Person=new ArrayList<>();
        dbHelper = new DBHelper(PieChartActivity.this);
        sp= getSharedPreferences("USER_PREFS",MODE_PRIVATE);

        allAccounts=dbHelper.getAllAccounts(sp.getInt("UID", 0));
        allPersons=dbHelper.getAllPersons(sp.getInt("UID",0));
        allTransactions=dbHelper.getAllTransactions(sp.getInt("UID",0));
        accountsDBIterator =allAccounts.iterator();

        if(getIntent().getIntExtra("actionId",0)>0)
        {
            actionId=getIntent().getIntExtra("actionId",0);
        }
        else
        {
            actionId=0;
        }

        legend = mPie.getLegend();
        mPie.setUsePercentValues(true);
        mPie.setHoleColorTransparent(true);
        mPie.setHoleRadius(60f);
        mPie.setDrawCenterText(true);
        Log.i("onCreate:","onCreate entered "+actionId);
        if(actionId == 1)
        {
            Log.i("onCreate:","onCreate entered "+actionId);
            while (accountsDBIterator.hasNext())
            {
                accountsDB=accountsDBIterator.next();
                mAmount=0;
                transactionsDBIterator =allTransactions.iterator();
                while (transactionsDBIterator.hasNext())
                {
                    transactionsDB=transactionsDBIterator.next();
                    if(transactionsDB.t_type.equals("Expense"))
                    {
                        if(transactionsDB.t_from_acc == accountsDB.acc_id)
                        {
                            mAmount += transactionsDB.t_balance;
                        }
                    }
                }
                mAcc.add(accountsDB.acc_name);
                mAmt.add(mAmount);
            }

            mPie.setCenterText("PieChart Accounts");
            setData(mAmt.size() - 1, actionId);
            reportsAccountsAdapter =new ReportsAccountsAdapter(PieChartActivity.this,R.layout.list_report,allAccounts);
            mLv_Pie.setAdapter(reportsAccountsAdapter);
        }
        else if(actionId == 2)
        {
            Log.i("onCreate:","onCreate entered "+actionId);
            personDBIterator=allPersons.iterator();
            while (personDBIterator.hasNext())
            {
                personDB=personDBIterator.next();
                mAmount=0;
                transactionsDBIterator =allTransactions.iterator();
                while (transactionsDBIterator.hasNext())
                {
                    transactionsDB=transactionsDBIterator.next();
                    if(transactionsDB.t_type.equals("Expense"))
                    {
                        if(transactionsDB.t_p_id == personDB.p_id)
                        {
                            mAmount += transactionsDB.t_balance;
                        }
                    }
                }
                mPer.add(personDB.p_name);
                mAmt_Person.add(mAmount);
            }
            mPie.setCenterText("PieChart Persons");
            setData(mAmt_Person.size() - 1, actionId);
            reportsPersonsAdapter =new ReportsPersonsAdapter(PieChartActivity.this,R.layout.list_report,allPersons);
            mLv_Pie.setAdapter(reportsPersonsAdapter);
        }

        mPie.setDrawHoleEnabled(true);
        mPie.setRotationAngle(0);
        mPie.setOnChartValueSelectedListener(this);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(5f);
    }

    private void setData(int count,int id)
    {
        Log.i("Set data:","setData entered "+id);
        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();
        PieDataSet dataSet = new PieDataSet(yValues,"Values");
        if(id == 1)
        {
            for (int i = 0; i < count + 1; i++)
            {
                yValues.add(new Entry(mAmt.get(i), i));
            }

            for (int i = 0; i < count + 1; i++)
            {
                xValues.add(mAcc.get(i));
            }
            dataSet = new PieDataSet(yValues, "Accounts");
        }
        else if (id == 2)
        {
            for (int i = 0; i < count + 1; i++)
            {
                yValues.add(new Entry(mAmt_Person.get(i), i));
            }

            for (int i = 0; i < count + 1; i++)
            {
                xValues.add(mPer.get(i));
            }
            dataSet = new PieDataSet(yValues, "Persons");
        }


        dataSet.setSliceSpace(3f);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xValues, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPie.setData(data);
        mPie.highlightValues(null);

        mPie.invalidate();
    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight)
    {
        if(entry==null)
        {
            return;
        }
        Log.i("Val Selected","Value:" + entry.getVal() + "xIndex" + entry.getXIndex());
    }

    @Override
    public void onNothingSelected()
    {
        Log.i("PieChart","Nothing Selected");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pie, menu);
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
        else if(id == R.id.action_acc_pie)
        {
            actionId=1;
            Intent i=new Intent(PieChartActivity.this,PieChartActivity.class);
            i.putExtra("actionId",actionId);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_person_pie)
        {
            actionId=2;
            Intent i=new Intent(PieChartActivity.this,PieChartActivity.class);
            i.putExtra("actionId",actionId);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
