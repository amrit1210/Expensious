package com.example.dhruvgupta.expensious;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Gaurav on 4/14/2015.
 */
public class PieChartActivity extends ActionBarActivity {

    static int actionId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        PieChartFragment pieChartFragment=new PieChartFragment();
        fragmentTransaction.replace(R.id.container,pieChartFragment);
        fragmentTransaction.commit();

    }
    public static class PieChartFragment extends Fragment implements OnChartValueSelectedListener
    {
        PieChart mPie;
        Legend legend;
        DBHelper dbHelper;
        SharedPreferences sp;
        Calendar mCal;
        String month_name;
        String sysDate;
        SimpleDateFormat month_date,sdf;
        TextView mPeriod_show;
        ImageButton mPrev,mNext;
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
        float mAmount=0;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             super.onCreateView(inflater, container, savedInstanceState);
            return  inflater.inflate(R.layout.activity_report,container,false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView =getView();
            mPie = (PieChart) rootView.findViewById(R.id.report_chart);
            mLv_Pie= (ListView) rootView. findViewById(R.id.report_list);
            mPeriod_show=(TextView)rootView.findViewById(R.id.report_period);
            mPrev=(ImageButton)rootView.findViewById(R.id.report_prev);
            mNext=(ImageButton)rootView.findViewById(R.id.report_next);
            mAcc=new ArrayList<>();
            mPer=new ArrayList<>();
            mAmt=new ArrayList<>();
            mAmt_Person=new ArrayList<>();

            dbHelper = new DBHelper(getActivity());
            sp= getActivity().getSharedPreferences("USER_PREFS",MODE_PRIVATE);

            allAccounts=dbHelper.getAllAccounts(sp.getInt("UID", 0));
            allPersons=dbHelper.getAllPersons(sp.getInt("UID",0));
            allTransactions=dbHelper.getAllTransactions(sp.getInt("UID",0));
            accountsDBIterator =allAccounts.iterator();

            if(getActivity().getIntent().getIntExtra("actionId",0)>0)
            {
              actionId=getActivity().getIntent().getIntExtra("actionId",0);
            }
            else
            {
                actionId=1;
            }

            legend = mPie.getLegend();
            mPie.setUsePercentValues(true);
            mPie.setHoleColorTransparent(true);
            mPie.setHoleRadius(60f);
            mPie.setDrawCenterText(true);

            mCal = Calendar.getInstance();
            month_date = new SimpleDateFormat("MMMM");
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            month_name = month_date.format(mCal.getTime());
            mPeriod_show.setText(month_name);
            sysDate=sdf.format(mCal.getTime());

            if(actionId == 1)
            {
                while (accountsDBIterator.hasNext())
                {
                    accountsDB=accountsDBIterator.next();
                    mAmount=0;
                    transactionsDBIterator =allTransactions.iterator();
                    while (transactionsDBIterator.hasNext())
                    {
                        try
                        {
                            transactionsDB=transactionsDBIterator.next();
                            Date d=sdf.parse(transactionsDB.t_date);
                            sysDate=month_date.format(d);
                            Log.i("Trans Month",sysDate);
                            Log.i("IF",sysDate.equals(month_name)+"");
                            if(sysDate.equals(month_name))
                            {
                                if(transactionsDB.t_type.equals("Expense"))
                                {
                                    if(transactionsDB.t_from_acc == accountsDB.acc_id)
                                    {
                                        mAmount += transactionsDB.t_balance;
                                    }
                                }
                            }
                        }
                        catch(ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    mAcc.add(accountsDB.acc_name);
                    mAmt.add(mAmount);
                }
                mPie.setCenterText("Accounts");
                setData(mAmt.size() - 1, actionId);
                reportsAccountsAdapter =new ReportsAccountsAdapter(getActivity(),R.layout.list_report,allAccounts);
                mLv_Pie.setAdapter(reportsAccountsAdapter);
            }
            else if(actionId == 2)
            {
                personDBIterator=allPersons.iterator();
                while (personDBIterator.hasNext())
                {
                    personDB=personDBIterator.next();
                    mAmount=0;
                    transactionsDBIterator =allTransactions.iterator();
                    while (transactionsDBIterator.hasNext())
                    {
                        try
                        {
                            transactionsDB=transactionsDBIterator.next();
                            Date d=sdf.parse(transactionsDB.t_date);
                            sysDate=month_date.format(d);
                            Log.i("Trans Month",sysDate);
                            Log.i("IF",sysDate.equals(month_name)+"");
                            if(transactionsDB.t_type.equals("Expense"))
                            {
                                if(transactionsDB.t_p_id == personDB.p_id)
                                {
                                    mAmount += transactionsDB.t_balance;
                                }
                            }
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    mPer.add(personDB.p_name);
                    mAmt_Person.add(mAmount);
                }
                mPie.setCenterText("Persons");
                setData(mAmt_Person.size() - 1, actionId);
                reportsPersonsAdapter =new ReportsPersonsAdapter(getActivity(),R.layout.list_report,allPersons);
                mLv_Pie.setAdapter(reportsPersonsAdapter);
            }

            mPie.setDrawHoleEnabled(true);
            mPie.setRotationAngle(0);
            mPie.setOnChartValueSelectedListener(this);
            legend.setXEntrySpace(7f);
            legend.setYEntrySpace(5f);


        }
        public void onLeftArrow(View v) throws ParseException
        {
            mCal.add(Calendar.MONTH,-1);
            sysDate=sdf.format(mCal.getTime());
            month_name = month_date.format(mCal.getTime());
            mPeriod_show.setText(month_name);
//        month_name=month_date.format(mPeriod_show.getText().toString());
            Log.i("Month from TextView",month_name);
            int id=actionId;
            transactionsDBIterator=allTransactions.iterator();
            while(transactionsDBIterator.hasNext())
            {
                transactionsDB=transactionsDBIterator.next();
                Date d=sdf.parse(transactionsDB.t_date);
                sysDate=month_date.format(d);
                Log.i("Trans Month",sysDate);
                Log.i("IF",sysDate.equals(month_name)+"");
                if(sysDate.equals(month_name))
                {
                    if(id == 1)
                    {
                        while (accountsDBIterator.hasNext())
                        {
                            accountsDB=accountsDBIterator.next();
                            mAmount=0;
                            if(transactionsDB.t_type.equals("Expense"))
                            {
                                if(transactionsDB.t_from_acc == accountsDB.acc_id)
                                {
                                    mAmount += transactionsDB.t_balance;
                                }
                            }
                            mAcc.add(accountsDB.acc_name);
                            mAmt.add(mAmount);
                        }
                        mPie.setCenterText("Accounts");
                        setData(mAmt.size() - 1, actionId);
                        reportsAccountsAdapter =new ReportsAccountsAdapter(getActivity(),R.layout.list_report,allAccounts);
                        mLv_Pie.setAdapter(reportsAccountsAdapter);
                    }
                    else if(id == 2)
                    {
                        while (personDBIterator.hasNext())
                        {
                            personDB=personDBIterator.next();
                            mAmount=0;
                            if(transactionsDB.t_type.equals("Expense"))
                            {
                                if(transactionsDB.t_p_id == personDB.p_id)
                                {
                                    mAmount += transactionsDB.t_balance;
                                }
                            }
                            mPer.add(personDB.p_name);
                            mAmt_Person.add(mAmount);
                        }
                        mPie.setCenterText("Persons");
                        setData(mAmt_Person.size() - 1, actionId);
                        reportsPersonsAdapter =new ReportsPersonsAdapter(getActivity(),R.layout.list_report,allPersons);
                        mLv_Pie.setAdapter(reportsPersonsAdapter);
                    }
                }
            }
        }

        public void onRightArrow(View v) throws ParseException
        {
            mCal.add(Calendar.MONTH,+1);
            sysDate=sdf.format(mCal.getTime());
            month_name = month_date.format(mCal.getTime());
            mPeriod_show.setText(month_name);
//        month_name=month_date.format(mPeriod_show.getText().toString());
            Log.i("Month from TextView",month_name);
            int id=actionId;
            transactionsDBIterator=allTransactions.iterator();
            while(transactionsDBIterator.hasNext())
            {
                transactionsDB=transactionsDBIterator.next();
                Date d=sdf.parse(transactionsDB.t_date);
                sysDate=month_date.format(d);
                Log.i("Trans Month",sysDate);
                Log.i("IF",sysDate.equals(month_name)+"");
                if(sysDate.equals(month_name))
                {
                    if(id == 1)
                    {
                        while (accountsDBIterator.hasNext())
                        {
                            accountsDB=accountsDBIterator.next();
                            mAmount=0;
                            if(transactionsDB.t_type.equals("Expense"))
                            {
                                if(transactionsDB.t_from_acc == accountsDB.acc_id)
                                {
                                    mAmount += transactionsDB.t_balance;
                                }
                            }
                            mAcc.add(accountsDB.acc_name);
                            mAmt.add(mAmount);
                        }
                        mPie.setCenterText("Accounts");
                        setData(mAmt.size() - 1, actionId);
                        reportsAccountsAdapter =new ReportsAccountsAdapter(getActivity(),R.layout.list_report,allAccounts);
                        mLv_Pie.setAdapter(reportsAccountsAdapter);
                    }
                    else if(id == 2)
                    {
                        while (personDBIterator.hasNext())
                        {
                            personDB=personDBIterator.next();
                            mAmount=0;
                            if(transactionsDB.t_type.equals("Expense"))
                            {
                                if(transactionsDB.t_p_id == personDB.p_id)
                                {
                                    mAmount += transactionsDB.t_balance;
                                }
                            }
                            mPer.add(personDB.p_name);
                            mAmt_Person.add(mAmount);
                        }
                        mPie.setCenterText("Persons");
                        setData(mAmt_Person.size() - 1, actionId);
                        reportsPersonsAdapter =new ReportsPersonsAdapter(getActivity(),R.layout.list_report,allPersons);
                        mLv_Pie.setAdapter(reportsPersonsAdapter);
                    }
                }
            }
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
                    Log.i("Amounts SetData",yValues+"");
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
                    Log.i("Amounts SetData",yValues+"");
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
            data.setValueTextColor(Color.BLACK);
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
