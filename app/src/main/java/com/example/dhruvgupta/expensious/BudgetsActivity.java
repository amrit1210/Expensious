package com.example.dhruvgupta.expensious;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Gaurav on 04-Apr-15.
 */
public class BudgetsActivity extends AbstractNavigationDrawerActivity
{


//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
////        FragmentManager fragmentManager=getFragmentManager();
////        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
////        BudgetFragment budgetFragment=new BudgetFragment();
////        fragmentTransaction.replace(R.id.container,budgetFragment);
////        fragmentTransaction.commit();
//
//    }
public void onInt(Bundle bundle) {
    super.onInt(bundle);

    this.setDefaultStartPositionNavigation(3);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        AccountsFragment fragment = new AccountsFragment();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
    this.getSupportActionBar().setTitle("Budgets");
}

    public static class BudgetFragment extends Fragment
    {
        TextView mB_Amt,mB_Cur,mB_Spent,mB_Spent_cur,mB_Rem,mB_Rem_Cur;
        ListView mBudget_list;

        float amt=0,spent=0,rem=0;
        String curCode;
        SharedPreferences sp;
        DBHelper dbHelper;
        ArrayList<BudgetDB> allBudgets;
        BudgetsAdapter budgetsAdapter;
        ArrayList<CurrencyDB> al1;
        ArrayList<TransactionsDB> allTransactions;
        TransactionsDB dbTrans;
        BudgetDB dbBudget;
        Iterator<TransactionsDB> transactionsDBIterator;
        Iterator<BudgetDB> budgetDBIterator;
        Date startDb, endDb, transDate, sysDate;
        SimpleDateFormat sdf;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater,container,savedInstanceState);
            return inflater.inflate(R.layout.activity_budgets,container,false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View rootView=getView();
            mB_Amt=(TextView)rootView.findViewById(R.id.budgets_total_amt);
            mB_Cur=(TextView)rootView.findViewById(R.id.budgets_total_cur);
            mB_Spent=(TextView)rootView.findViewById(R.id.budgets_spent_amt);
            mB_Spent_cur=(TextView)rootView.findViewById(R.id.budgets_spent_cur);
            mB_Rem=(TextView)rootView.findViewById(R.id.budgets_remain_amt);
            mB_Rem_Cur=(TextView)rootView.findViewById(R.id.budgets_remain_cur);
            mBudget_list =(ListView)rootView.findViewById(R.id.budgets_list);

            sp = getActivity().getSharedPreferences("USER_PREFS", MODE_PRIVATE);
            dbHelper = new DBHelper(getActivity());

            sdf= new SimpleDateFormat("dd-MM-yyyy");

            al1 = new ListOfCurrencies().getAllCurrencies();
            FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
//            fab.attachToListView(listView);
            Cursor c1 = dbHelper.getSettingsData(sp.getInt("UID", 0));
            c1.moveToFirst();
            curCode = c1.getString(c1.getColumnIndex(DBHelper.SETTINGS_COL_CUR_CODE));
            c1.close();

            Iterator<CurrencyDB> iterator = al1.iterator();
            while (iterator.hasNext())
            {
                CurrencyDB curDB = iterator.next();
                if (curDB.c_code.equals(curCode))
                {
                    mB_Cur.setText(curDB.c_symbol);
                    mB_Rem_Cur.setText(curDB.c_symbol);
                    mB_Spent_cur.setText(curDB.c_symbol);
                }
            }

            allTransactions=dbHelper.getAllTransactions(sp.getInt("UID",0));
            transactionsDBIterator =allTransactions.iterator();

            allBudgets=dbHelper.getAllBudgets(sp.getInt("UID",0));
            budgetDBIterator =allBudgets.iterator();

            sysDate=new Date();

            while (budgetDBIterator.hasNext())
            {
                dbBudget = budgetDBIterator.next();
                try
                {
                    startDb = sdf.parse(dbBudget.b_startDate);
                    endDb = sdf.parse(dbBudget.b_endDate);
                    if (!sysDate.before(startDb) && !sysDate.after(endDb))
                    {
                        amt = dbBudget.b_amount;
                        while (transactionsDBIterator.hasNext())
                        {
                            dbTrans = transactionsDBIterator.next();
                            try
                            {
                                transDate =sdf.parse(dbTrans.t_date);
                                if(!((transDate.before(startDb)) && !(transDate.after(endDb))))
                                {
                                    spent += dbTrans.t_balance;
                                }
                            }
                            catch (ParseException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }

            rem += amt-spent;
            mB_Amt.setText(amt+"");
            mB_Spent.setText(spent+"");
            mB_Rem.setText(rem+"");
            allBudgets=dbHelper.getAllBudgets(sp.getInt("UID",0));
            budgetsAdapter =new BudgetsAdapter(getActivity(),R.layout.list_budget,allBudgets);
            mBudget_list.setAdapter(budgetsAdapter);
            registerForContextMenu(mBudget_list);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(getActivity(),AddBudgetActivity.class);
                    startActivity(i);

                }
            });
        }
        @Override
        public boolean onContextItemSelected(MenuItem item)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int listPosition = info.position;
            BudgetDB bud_db = (BudgetDB) mBudget_list.getAdapter().getItem(listPosition);
            int id=item.getItemId();

            if(id==R.id.Edit)
            {
                Cursor c= dbHelper.getBudgetData(bud_db.b_id, sp.getInt("UID", 0));
                c.moveToFirst();
                int b_id=c.getInt(c.getColumnIndex(DBHelper.BUDGETS_COL_B_ID));
                int b_uid=c.getInt(c.getColumnIndex(DBHelper.BUDGETS_COL_B_UID));
                float b_amt=c.getFloat(c.getColumnIndex(DBHelper.BUDGETS_COL_B_AMOUNT));
                String b_sDate=c.getString(c.getColumnIndex(DBHelper.BUDGETS_COL_START_DATE));
                String b_eDate=c.getString(c.getColumnIndex(DBHelper.BUDGETS_COL_END_DATE));
                c.close();
                Intent i=new Intent(getActivity(),AddBudgetActivity.class);
                i.putExtra("b_id",b_id);
                i.putExtra("b_uid",b_uid);
                i.putExtra("b_amt",b_amt);
                i.putExtra("b_sDate",b_sDate);
                i.putExtra("b_eDate",b_eDate);
                startActivity(i);
            }

            if(id==R.id.Delete)
            {
                dbHelper.deleteBudget(bud_db.b_id, sp.getInt("UID", 0));

                ParseObject object = new ParseObject("Budget");
                object.put("b_id", bud_db.b_id);
                object.put("b_uid", bud_db.b_u_id);
                object.put("b_amount", bud_db.b_amount);
                object.put("b_startDate", bud_db.b_startDate);
                object.put("b_endDate", bud_db.b_endDate);
                object.pinInBackground("pinBudgetDelete");

                Intent i=new Intent(getActivity(),BudgetsActivity.class);
                startActivity(i);
                Toast.makeText(getActivity(), "Budget Deleted", Toast.LENGTH_LONG).show();
            }

            return super.onContextItemSelected(item);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
        {
            super.onCreateContextMenu(menu, v, menuInfo);

            MenuInflater inflater= getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu,menu);
        }

    }

}
