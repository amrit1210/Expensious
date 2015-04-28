package com.example.dhruvgupta.expensious;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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
public class PieChartActivity extends AbstractNavigationDrawerActivity
{
    static int actionId=0;
    static int p=0;
    static String month_name=null;
    static String day=null;
    static String year=null;
    //@Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        FragmentManager fragmentManager=getFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        PieChartFragment pieChartFragment=new PieChartFragment();
//        fragmentTransaction.replace(R.id.container,pieChartFragment);
//        fragmentTransaction.commit();
//
//    }
    public void onInt(Bundle bundle)
    {
        super.onInt(bundle);

        this.setDefaultStartPositionNavigation(7);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        AccountsFragment fragment = new AccountsFragment();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
    }
    public static class PieChartFragment extends Fragment implements OnChartValueSelectedListener
    {
        PieChart mPie;
        Legend legend;
        DBHelper dbHelper;
        SharedPreferences sp;
        Calendar mCal;
        String sysDate;
        SimpleDateFormat month_date,sdf,year_date;
        TextView mPeriod_show;
        ImageButton mPrev,mNext;
        Spinner mSpinner;
        ArrayList<AccountsDB> allAccounts;
        ArrayList<TransactionsDB> allTransactions;
        ArrayList<PersonDB> allPersons;
        ArrayList<CategoryDB_Specific> allCategories;
        ArrayList<SubCategoryDB> allSubCategories;
        ListView mLv_Pie;
        AccountsDB accountsDB;
        PersonDB personDB;
        CategoryDB_Specific categoryDB;
        TransactionsDB transactionsDB;
        SubCategoryDB subCategoryDB;
        Iterator<AccountsDB> accountsDBIterator;
        Iterator<TransactionsDB> transactionsDBIterator;
        Iterator<PersonDB> personDBIterator;
        Iterator<CategoryDB_Specific> categoryDBIterator;
        Iterator<SubCategoryDB> subCategoryDBIterator;
        ArrayList<Float> mAmt,mAmt_Person,mAmt_Category,mAmt_Sub;
        ArrayList<String> mAcc,mPer,mCat,mSub;
        ReportsAccountsAdapter reportsAccountsAdapter;
        ReportsPersonsAdapter reportsPersonsAdapter;
        ReportsCategoriesAdapter reportsCategoriesAdapter;
        ReportsSubCategoriesAdapter reportsSubCategoriesAdapter;
        float mAmount=0;
        int mPeriod_adapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            super.onCreateView(inflater, container, savedInstanceState);
            return  inflater.inflate(R.layout.activity_report,container,false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            View rootView =getView();
            mPie = (PieChart) rootView.findViewById(R.id.report_chart);
            mLv_Pie= (ListView) rootView. findViewById(R.id.report_list);
            mPeriod_show=(TextView)rootView.findViewById(R.id.report_period);
            mPrev=(ImageButton)rootView.findViewById(R.id.report_prev);
            mNext=(ImageButton)rootView.findViewById(R.id.report_next);
            mSpinner=(Spinner)rootView.findViewById(R.id.report_spinner_period);
            mAcc=new ArrayList<>();
            mPer=new ArrayList<>();
            mCat=new ArrayList<>();
            mSub=new ArrayList<>();
            mAmt=new ArrayList<>();
            mAmt_Person=new ArrayList<>();
            mAmt_Category=new ArrayList<>();
            mAmt_Sub=new ArrayList<>();

            dbHelper = new DBHelper(getActivity());
            sp= getActivity().getSharedPreferences("USER_PREFS",MODE_PRIVATE);

            allAccounts=dbHelper.getAllAccounts(sp.getInt("UID", 0));
            allPersons=dbHelper.getAllPersons(sp.getInt("UID", 0));
            allTransactions=dbHelper.getAllTransactions(sp.getInt("UID", 0));
            allCategories=dbHelper.getCategories(sp.getInt("UID", 0));
            allSubCategories=dbHelper.getSubCategories(sp.getInt("UID", 0));

            if(getActivity().getIntent().getIntExtra("actionId",0)>0)
            {
                actionId=getActivity().getIntent().getIntExtra("actionId",0);
            }
            else
            {
                actionId=1;
                p=1;
            }

            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    mPeriod_adapter = (int) mSpinner.getSelectedItemId();
                    p=mPeriod_adapter;
                    if(p==0)
                    {
                        year=year_date.format(mCal.getTime());
                        mPeriod_show.setText(year);
                        mPeriod_adapter=p;
                    }
                    else if(p==1)
                    {
                        month_name = month_date.format(mCal.getTime());
                        mPeriod_show.setText(month_name);
                        mPeriod_adapter=p;
                    }
                    else if(p==3)
                    {
                        day = sdf.format(mCal.getTime());
                        mPeriod_show.setText(day);
                        mPeriod_adapter=p;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });

            legend = mPie.getLegend();
            mPie.setUsePercentValues(true);
            mPie.setHoleColorTransparent(true);
            mPie.setHoleRadius(60f);
            mPie.setDrawCenterText(true);

            mCal = Calendar.getInstance();
            month_date = new SimpleDateFormat("MMMM");
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            year_date = new SimpleDateFormat("yyyy");

            if(actionId == 1)
            {
                categoryDBIterator =allCategories.iterator();
                while (categoryDBIterator.hasNext())
                {
                    categoryDB=categoryDBIterator.next();
                    transactionsDBIterator =allTransactions.iterator();
                    mAmount=0;
                    while (transactionsDBIterator.hasNext())
                    {
                        try
                        {
                            transactionsDB=transactionsDBIterator.next();
                            Date d=sdf.parse(transactionsDB.t_date);
                            if(p == 0)
                            {
                                sysDate=year_date.format(d);
                                if(sysDate.equals(year))
                                {
                                    if(transactionsDB.t_type.equals("Expense"))
                                    {
                                        if(transactionsDB.t_c_id == categoryDB.c_id)
                                        {
                                            mAmount += transactionsDB.t_balance;
                                        }
                                    }
                                }
                            }
                            else if(p == 1)
                            {
                                sysDate=month_date.format(d);
                                if(sysDate.equals(month_name))
                                {
                                    if(transactionsDB.t_type.equals("Expense"))
                                    {
                                        if(transactionsDB.t_c_id == categoryDB.c_id)
                                        {
                                            mAmount += transactionsDB.t_balance;
                                        }
                                    }
                                }
                            }
                            else if(p == 3)
                            {
                                sysDate=sdf.format(d);
                                if(sysDate.equals(day))
                                {
                                    if(transactionsDB.t_type.equals("Expense"))
                                    {
                                        if(transactionsDB.t_c_id == categoryDB.c_id)
                                        {
                                            mAmount += transactionsDB.t_balance;
                                        }
                                    }
                                }
                            }
                        }
                        catch(ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    mCat.add(categoryDB.c_name);
                    mAmt_Category.add(mAmount);
                }
                mPie.setCenterText("Categories");
                setData(mAmt_Category.size() - 1, actionId);
                reportsCategoriesAdapter =new ReportsCategoriesAdapter(getActivity(),R.layout.list_report,allCategories,mPeriod_adapter);
                mLv_Pie.setAdapter(reportsCategoriesAdapter);
            }
            else if(actionId == 2)
            {
                accountsDBIterator =allAccounts.iterator();
                while (accountsDBIterator.hasNext())
                {
                    accountsDB=accountsDBIterator.next();
                    transactionsDBIterator =allTransactions.iterator();
                    mAmount=0;
                    while (transactionsDBIterator.hasNext())
                    {
                        try
                        {
                            transactionsDB=transactionsDBIterator.next();
                            Date d=sdf.parse(transactionsDB.t_date);
                            if(p == 0)
                            {
                                sysDate=year_date.format(d);
                                if(sysDate.equals(year))
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
                            else if(p == 1)
                            {
                                sysDate=month_date.format(d);
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
                            else if(p == 3)
                            {
                                sysDate=sdf.format(d);
                                if(sysDate.equals(day))
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
                reportsAccountsAdapter =new ReportsAccountsAdapter(getActivity(),R.layout.list_report,allAccounts,mPeriod_adapter);
                mLv_Pie.setAdapter(reportsAccountsAdapter);
            }
            else if(actionId == 3)
            {
                personDBIterator=allPersons.iterator();
                while (personDBIterator.hasNext())
                {
                    personDB=personDBIterator.next();
                    transactionsDBIterator =allTransactions.iterator();
                    mAmount=0;
                    while (transactionsDBIterator.hasNext())
                    {
                        try
                        {
                            transactionsDB=transactionsDBIterator.next();
                            Date d=sdf.parse(transactionsDB.t_date);
                            if(p == 0)
                            {
                                sysDate=year_date.format(d);
                                if(sysDate.equals(year))
                                {
                                    if(transactionsDB.t_type.equals("Expense"))
                                    {
                                        if(transactionsDB.t_p_id == personDB.p_id)
                                        {
                                            mAmount += transactionsDB.t_balance;
                                        }
                                    }
                                }
                            }
                            else if(p == 1)
                            {
                                sysDate=month_date.format(d);
                                if(sysDate.equals(month_name))
                                {
                                    if(transactionsDB.t_type.equals("Expense"))
                                    {
                                        if(transactionsDB.t_p_id == personDB.p_id)
                                        {
                                            mAmount += transactionsDB.t_balance;
                                        }
                                    }
                                }
                            }
                            else if(p == 3)
                            {
                                sysDate=sdf.format(d);
                                if(sysDate.equals(day))
                                {
                                    if(transactionsDB.t_type.equals("Expense"))
                                    {
                                        if(transactionsDB.t_p_id == personDB.p_id)
                                        {
                                            mAmount += transactionsDB.t_balance;
                                        }
                                    }
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
                reportsPersonsAdapter =new ReportsPersonsAdapter(getActivity(),R.layout.list_report,allPersons,mPeriod_adapter);
                mLv_Pie.setAdapter(reportsPersonsAdapter);
            }
            else if(actionId == 4)
            {
                subCategoryDBIterator =allSubCategories.iterator();
                while (subCategoryDBIterator.hasNext())
                {
                    subCategoryDB=subCategoryDBIterator.next();
                    transactionsDBIterator =allTransactions.iterator();
                    mAmount=0;
                    while (transactionsDBIterator.hasNext())
                    {
                        try
                        {
                            transactionsDB=transactionsDBIterator.next();
                            Date d=sdf.parse(transactionsDB.t_date);
                            if(p == 0)
                            {
                                sysDate=year_date.format(d);
                                if(sysDate.equals(year))
                                {
                                    if(transactionsDB.t_type.equals("Expense"))
                                    {
                                        if(transactionsDB.t_sub_id == subCategoryDB.sub_id)
                                        {
                                            mAmount += transactionsDB.t_balance;
                                        }
                                    }
                                }
                            }
                            else if(p == 1)
                            {
                                sysDate=month_date.format(d);
                                if(sysDate.equals(month_name))
                                {
                                    if(transactionsDB.t_type.equals("Expense"))
                                    {
                                        if(transactionsDB.t_sub_id == subCategoryDB.sub_id)
                                        {
                                            mAmount += transactionsDB.t_balance;
                                        }
                                    }
                                }
                            }
                            else if(p == 3)
                            {
                                sysDate=sdf.format(d);
                                if(sysDate.equals(day))
                                {
                                    if(transactionsDB.t_type.equals("Expense"))
                                    {
                                        if(transactionsDB.t_sub_id == subCategoryDB.sub_id)
                                        {
                                            mAmount += transactionsDB.t_balance;
                                        }
                                    }
                                }
                            }
                        }
                        catch(ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    mSub.add(subCategoryDB.sub_name);
                    mAmt_Sub.add(mAmount);
                }
                mPie.setCenterText("Sub Categories");
                setData(mAmt_Sub.size() - 1, actionId);
                reportsSubCategoriesAdapter =new ReportsSubCategoriesAdapter(getActivity(),R.layout.list_report,allSubCategories,mPeriod_adapter);
                mLv_Pie.setAdapter(reportsSubCategoriesAdapter);
            }

            mPie.setDrawHoleEnabled(true);
            mPie.setRotationAngle(0);
            mPie.setOnChartValueSelectedListener(this);
            legend.setXEntrySpace(7f);
            legend.setYEntrySpace(5f);

            mPrev.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(p == 0)
                    {
                        mCal.add(Calendar.YEAR,-1);
                        year = year_date.format(mCal.getTime());
                        mPeriod_show.setText(year);
                    }
                    else if(p == 1)
                    {
                        mCal.add(Calendar.MONTH,-1);
                        month_name = month_date.format(mCal.getTime());
                        mPeriod_show.setText(month_name);
                    }
                    else if(p == 3)
                    {
                        mCal.add(Calendar.DAY_OF_MONTH,-1);
                        day = sdf.format(mCal.getTime());
                        mPeriod_show.setText(day);
                    }

                    if(actionId == 1)
                    {
                        mAmt_Category.clear();
                        mCat.clear();
                        categoryDBIterator =allCategories.iterator();
                        while (categoryDBIterator.hasNext())
                        {
                            categoryDB=categoryDBIterator.next();
                            transactionsDBIterator =allTransactions.iterator();
                            mAmount=0;
                            while (transactionsDBIterator.hasNext())
                            {
                                try
                                {
                                    transactionsDB=transactionsDBIterator.next();
                                    Date d=sdf.parse(transactionsDB.t_date);
                                    if(p == 0)
                                    {
                                        sysDate=year_date.format(d);
                                        if(sysDate.equals(year))
                                        {
                                            if(transactionsDB.t_type.equals("Expense"))
                                            {
                                                if(transactionsDB.t_c_id == categoryDB.c_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if(p == 1)
                                    {
                                        sysDate=month_date.format(d);
                                        if(sysDate.equals(month_name))
                                        {
                                            if(transactionsDB.t_type.equals("Expense"))
                                            {
                                                if(transactionsDB.t_c_id == categoryDB.c_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if(p == 3)
                                    {
                                        sysDate=sdf.format(d);
                                        if(sysDate.equals(day))
                                        {
                                            Log.i("Sysdate",sysDate+"  "+day);
                                            if(transactionsDB.t_type.equals("Expense"))
                                            {
                                                if(transactionsDB.t_c_id == categoryDB.c_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                }
                                catch(ParseException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            mCat.add(categoryDB.c_name);
                            mAmt_Category.add(mAmount);
                        }
                        mPie.setCenterText("Categories");
                        setData(mAmt_Category.size() - 1, actionId);
                        reportsCategoriesAdapter =new ReportsCategoriesAdapter(getActivity(),R.layout.list_report,allCategories,mPeriod_adapter);
                        mLv_Pie.setAdapter(reportsCategoriesAdapter);
                    }
                    else if(actionId == 2)
                    {
                        mAmt.clear();
                        mAcc.clear();
                        accountsDBIterator=allAccounts.iterator();
                        while (accountsDBIterator.hasNext())
                        {
                            accountsDB=accountsDBIterator.next();
                            transactionsDBIterator =allTransactions.iterator();
                            mAmount=0;
                            while (transactionsDBIterator.hasNext())
                            {
                                try
                                {
                                    transactionsDB=transactionsDBIterator.next();
                                    Date d=sdf.parse(transactionsDB.t_date);
                                    if(p == 0)
                                    {
                                        sysDate=year_date.format(d);
                                        if(sysDate.equals(year))
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
                                    else if(p == 1)
                                    {
                                        sysDate=month_date.format(d);
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
                                    else if(p == 3)
                                    {
                                        sysDate=sdf.format(d);
                                        if(sysDate.equals(day))
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
                        reportsAccountsAdapter =new ReportsAccountsAdapter(getActivity(),R.layout.list_report,allAccounts,mPeriod_adapter);
                        mLv_Pie.setAdapter(reportsAccountsAdapter);
                    }
                    else if(actionId == 3)
                    {
                        mAmt_Person.clear();
                        mPer.clear();
                        personDBIterator=allPersons.iterator();
                        while (personDBIterator.hasNext())
                        {
                            personDB=personDBIterator.next();
                            transactionsDBIterator =allTransactions.iterator();
                            mAmount=0;
                            while (transactionsDBIterator.hasNext())
                            {
                                try
                                {
                                    transactionsDB=transactionsDBIterator.next();
                                    Date d=sdf.parse(transactionsDB.t_date);
                                    if(p == 0)
                                    {
                                        sysDate=year_date.format(d);
                                        if(sysDate.equals(year))
                                        {
                                            if(transactionsDB.t_type.equals("Expense"))
                                            {
                                                if(transactionsDB.t_p_id == personDB.p_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if(p == 1)
                                    {
                                        sysDate=month_date.format(d);
                                        if(sysDate.equals(month_name))
                                        {
                                            if(transactionsDB.t_type.equals("Expense"))
                                            {
                                                if(transactionsDB.t_p_id == personDB.p_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if(p == 3)
                                    {
                                        sysDate=sdf.format(d);
                                        if(sysDate.equals(day))
                                        {
                                            if(transactionsDB.t_type.equals("Expense"))
                                            {
                                                if(transactionsDB.t_p_id == personDB.p_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
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
                        reportsPersonsAdapter =new ReportsPersonsAdapter(getActivity(),R.layout.list_report,allPersons,mPeriod_adapter);
                        mLv_Pie.setAdapter(reportsPersonsAdapter);
                    }
                    else if(actionId == 4)
                    {
                        mAmt_Sub.clear();
                        mSub.clear();
                        subCategoryDBIterator =allSubCategories.iterator();
                        while (subCategoryDBIterator.hasNext())
                        {
                            subCategoryDB=subCategoryDBIterator.next();
                            transactionsDBIterator =allTransactions.iterator();
                            mAmount=0;
                            while (transactionsDBIterator.hasNext())
                            {
                                try
                                {
                                    transactionsDB=transactionsDBIterator.next();
                                    Date d=sdf.parse(transactionsDB.t_date);
                                    if(p == 0)
                                    {
                                        sysDate=year_date.format(d);
                                        if(sysDate.equals(year))
                                        {
                                            if(transactionsDB.t_type.equals("Expense"))
                                            {
                                                if(transactionsDB.t_sub_id == subCategoryDB.sub_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if(p == 1)
                                    {
                                        sysDate=month_date.format(d);
                                        if(sysDate.equals(month_name))
                                        {
                                            if(transactionsDB.t_type.equals("Expense"))
                                            {
                                                if(transactionsDB.t_sub_id == subCategoryDB.sub_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if(p == 3)
                                    {
                                        sysDate=sdf.format(d);
                                        if(sysDate.equals(day))
                                        {
                                            if(transactionsDB.t_type.equals("Expense"))
                                            {
                                                if(transactionsDB.t_sub_id == subCategoryDB.sub_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                }
                                catch(ParseException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            mSub.add(subCategoryDB.sub_name);
                            mAmt_Sub.add(mAmount);
                        }
                        mPie.setCenterText("Sub Categories");
                        setData(mAmt_Sub.size() - 1, actionId);
                        reportsSubCategoriesAdapter =new ReportsSubCategoriesAdapter(getActivity(),R.layout.list_report,allSubCategories,mPeriod_adapter);
                        mLv_Pie.setAdapter(reportsSubCategoriesAdapter);
                    }
                }
            });

            mNext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (p == 0)
                    {
                        mCal.add(Calendar.YEAR, +1);
                        year = year_date.format(mCal.getTime());
                        mPeriod_show.setText(year);
                    }
                    else if (p == 1)
                    {
                        mCal.add(Calendar.MONTH, +1);
                        month_name = month_date.format(mCal.getTime());
                        mPeriod_show.setText(month_name);
                    }
                    else if (p == 3)
                    {
                        mCal.add(Calendar.DAY_OF_MONTH, +1);
                        day = sdf.format(mCal.getTime());
                        mPeriod_show.setText(day);
                    }

                    if (actionId == 1)
                    {
                        mAmt_Category.clear();
                        mCat.clear();
                        categoryDBIterator = allCategories.iterator();
                        while (categoryDBIterator.hasNext())
                        {
                            categoryDB = categoryDBIterator.next();
                            transactionsDBIterator = allTransactions.iterator();
                            mAmount = 0;
                            while (transactionsDBIterator.hasNext())
                            {
                                try
                                {
                                    transactionsDB = transactionsDBIterator.next();
                                    Date d = sdf.parse(transactionsDB.t_date);
                                    if (p == 0)
                                    {
                                        sysDate = year_date.format(d);
                                        if (sysDate.equals(year))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_c_id == categoryDB.c_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if (p == 1)
                                    {
                                        sysDate = month_date.format(d);
                                        if (sysDate.equals(month_name))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_c_id == categoryDB.c_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if (p == 3)
                                    {
                                        sysDate = sdf.format(d);
                                        if (sysDate.equals(day))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_c_id == categoryDB.c_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            mCat.add(categoryDB.c_name);
                            mAmt_Category.add(mAmount);
                        }
                        mPie.setCenterText("Categories");
                        setData(mAmt_Category.size() - 1, actionId);
                        reportsCategoriesAdapter = new ReportsCategoriesAdapter(getActivity(), R.layout.list_report, allCategories, mPeriod_adapter);
                        mLv_Pie.setAdapter(reportsCategoriesAdapter);
                    }
                    else if (actionId == 2)
                    {
                        mAmt.clear();
                        mAcc.clear();
                        accountsDBIterator = allAccounts.iterator();
                        while (accountsDBIterator.hasNext())
                        {
                            accountsDB = accountsDBIterator.next();
                            transactionsDBIterator = allTransactions.iterator();
                            mAmount = 0;
                            while (transactionsDBIterator.hasNext())
                            {
                                try
                                {
                                    transactionsDB = transactionsDBIterator.next();
                                    Date d = sdf.parse(transactionsDB.t_date);
                                    if (p == 0)
                                    {
                                        sysDate = year_date.format(d);
                                        if (sysDate.equals(year))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_from_acc == accountsDB.acc_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if (p == 1)
                                    {
                                        sysDate = month_date.format(d);
                                        if (sysDate.equals(month_name))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_from_acc == accountsDB.acc_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if (p == 3)
                                    {
                                        sysDate = sdf.format(d);
                                        if (sysDate.equals(day))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_from_acc == accountsDB.acc_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            mAcc.add(accountsDB.acc_name);
                            mAmt.add(mAmount);
                        }
                        mPie.setCenterText("Accounts");
                        setData(mAmt.size() - 1, actionId);
                        reportsAccountsAdapter = new ReportsAccountsAdapter(getActivity(), R.layout.list_report, allAccounts, mPeriod_adapter);
                        mLv_Pie.setAdapter(reportsAccountsAdapter);
                    }
                    else if (actionId == 3)
                    {
                        mAmt_Person.clear();
                        mPer.clear();
                        personDBIterator = allPersons.iterator();
                        while (personDBIterator.hasNext())
                        {
                            personDB = personDBIterator.next();
                            transactionsDBIterator = allTransactions.iterator();
                            mAmount = 0;
                            while (transactionsDBIterator.hasNext())
                            {
                                try
                                {
                                    transactionsDB = transactionsDBIterator.next();
                                    Date d = sdf.parse(transactionsDB.t_date);
                                    if (p == 0)
                                    {
                                        sysDate = year_date.format(d);
                                        if (sysDate.equals(year))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_p_id == personDB.p_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if (p == 1)
                                    {
                                        sysDate = month_date.format(d);
                                        if (sysDate.equals(month_name))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_p_id == personDB.p_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if (p == 3)
                                    {
                                        sysDate = sdf.format(d);
                                        if (sysDate.equals(day))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_p_id == personDB.p_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
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
                        reportsPersonsAdapter = new ReportsPersonsAdapter(getActivity(), R.layout.list_report, allPersons, mPeriod_adapter);
                        mLv_Pie.setAdapter(reportsPersonsAdapter);
                    }
                    else if (actionId == 4)
                    {
                        mAmt_Sub.clear();
                        mSub.clear();
                        subCategoryDBIterator = allSubCategories.iterator();
                        while (subCategoryDBIterator.hasNext())
                        {
                            subCategoryDB = subCategoryDBIterator.next();
                            transactionsDBIterator = allTransactions.iterator();
                            mAmount = 0;
                            while (transactionsDBIterator.hasNext())
                            {
                                try
                                {
                                    transactionsDB = transactionsDBIterator.next();
                                    Date d = sdf.parse(transactionsDB.t_date);
                                    if (p == 0)
                                    {
                                        sysDate = year_date.format(d);
                                        if (sysDate.equals(year))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_sub_id == subCategoryDB.sub_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if (p == 1)
                                    {
                                        sysDate = month_date.format(d);
                                        if (sysDate.equals(month_name))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_sub_id == subCategoryDB.sub_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                    else if (p == 3)
                                    {
                                        sysDate = sdf.format(d);
                                        if (sysDate.equals(day))
                                        {
                                            if (transactionsDB.t_type.equals("Expense"))
                                            {
                                                if (transactionsDB.t_sub_id == subCategoryDB.sub_id)
                                                {
                                                    mAmount += transactionsDB.t_balance;
                                                }
                                            }
                                        }
                                    }
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            mSub.add(subCategoryDB.sub_name);
                            mAmt_Sub.add(mAmount);
                        }
                        mPie.setCenterText("Sub Categories");
                        setData(mAmt_Sub.size() - 1, actionId);
                        reportsSubCategoriesAdapter = new ReportsSubCategoriesAdapter(getActivity(), R.layout.list_report, allSubCategories, mPeriod_adapter);
                        mLv_Pie.setAdapter(reportsSubCategoriesAdapter);
                    }
                }
            });
        }

        private void setData(int count,int id)
        {
            Log.i("Set data:","setData entered "+id);
            ArrayList<Entry> yValues = new ArrayList<>();
            ArrayList<String> xValues = new ArrayList<>();
            PieDataSet dataSet = new PieDataSet(yValues,"Values");
            if(actionId == 1)
            {
                for (int i = 0; i < count + 1; i++)
                {
                    yValues.add(new Entry(mAmt_Category.get(i), i));
                    Log.i("Category SetData",yValues+"");
                }

                for (int i = 0; i < count + 1; i++)
                {
                    xValues.add(mCat.get(i));
                }
                dataSet = new PieDataSet(yValues, "Categories");
            }
            else if(actionId == 2)
            {
                for (int i = 0; i < count + 1; i++)
                {
                    yValues.add(new Entry(mAmt.get(i), i));
                    Log.i("Account SetData",yValues+"");
                }

                for (int i = 0; i < count + 1; i++)
                {
                    xValues.add(mAcc.get(i));
                }
                dataSet = new PieDataSet(yValues, "Accounts");
            }
            else if (actionId == 3)
            {
                for (int i = 0; i < count + 1; i++)
                {
                    yValues.add(new Entry(mAmt_Person.get(i), i));
                    Log.i("Person SetData",yValues+"");
                }

                for (int i = 0; i < count + 1; i++)
                {
                    xValues.add(mPer.get(i));
                }
                dataSet = new PieDataSet(yValues, "Persons");
            }
            else if(actionId == 4)
            {
                for (int i = 0; i < count + 1; i++)
                {
                    yValues.add(new Entry(mAmt_Sub.get(i), i));
                    Log.i("SubCategory SetData",yValues+"");
                }

                for (int i = 0; i < count + 1; i++)
                {
                    xValues.add(mSub.get(i));
                }
                dataSet = new PieDataSet(yValues, "Sub Categories");
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
        else if(id == R.id.action_cat_pie)
        {
            actionId=1;
            Intent i=new Intent(PieChartActivity.this,PieChartActivity.class);
            i.putExtra("actionId",actionId);
            startActivity(i);
            return true;
        }
        else if(id == R.id.action_acc_pie)
        {
            actionId=2;
            Intent i=new Intent(PieChartActivity.this,PieChartActivity.class);
            i.putExtra("actionId",actionId);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_person_pie)
        {
            actionId=3;
            Intent i=new Intent(PieChartActivity.this,PieChartActivity.class);
            i.putExtra("actionId",actionId);
            startActivity(i);
            return true;
        }
        else if(id == R.id.action_sub_cat_pie)
        {
            actionId=4;
            Intent i=new Intent(PieChartActivity.this,PieChartActivity.class);
            i.putExtra("actionId",actionId);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
