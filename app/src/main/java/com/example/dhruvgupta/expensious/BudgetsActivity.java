package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Gaurav on 04-Apr-15.
 */
public class BudgetsActivity extends ActionBarActivity
{
    TextView mB_Amt,mB_Cur,mB_Spent,mB_Spent_cur,mB_Rem,mB_Rem_Cur;
    ListView mBudget_list;

    float amt=0,spent=0,rem=0;
    SharedPreferences sp;
    DBHelper dbHelper;
    ArrayList<BudgetDB> allBudgets;
    BudgetsAdapter budgetsAdapter;
    ArrayList<TransactionsDB> allTransactions;
    TransactionsDB dbTrans;
    BudgetDB dbBudget;
    Iterator<TransactionsDB> transactionsDBIterator;
    Iterator<BudgetDB> budgetDBIterator;
    Date startDb, endDb, transDate, sysDate;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets);

        mB_Amt=(TextView)findViewById(R.id.budgets_total_amt);
        mB_Cur=(TextView)findViewById(R.id.budgets_total_cur);
        mB_Spent=(TextView)findViewById(R.id.budgets_spent_amt);
        mB_Spent_cur=(TextView)findViewById(R.id.budgets_spent_cur);
        mB_Rem=(TextView)findViewById(R.id.budgets_remain_amt);
        mB_Rem_Cur=(TextView)findViewById(R.id.budgets_remain_cur);
        mBudget_list =(ListView)findViewById(R.id.budgets_list);

        sp = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        dbHelper = new DBHelper(BudgetsActivity.this);

        sdf= new SimpleDateFormat("dd-MM-yyyy");

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
                if (!(sysDate.before(startDb) && sysDate.after(endDb)))
                {
                    amt = dbBudget.b_amount;

                    while (transactionsDBIterator.hasNext())
                    {
                        dbTrans = transactionsDBIterator.next();
                        try
                        {
                            transDate =sdf.parse(dbTrans.t_date);
                            if(!(transDate.before(startDb) && transDate.after(endDb)))
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
        budgetsAdapter =new BudgetsAdapter(BudgetsActivity.this,R.layout.list_budget,allBudgets);
        mBudget_list.setAdapter(budgetsAdapter);
        registerForContextMenu(mBudget_list);
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
            Cursor c= dbHelper.getBudgetData(bud_db.b_id);
            c.moveToFirst();
            int b_id=c.getInt(c.getColumnIndex(DBHelper.BUDGETS_COL_B_ID));
            int b_uid=c.getInt(c.getColumnIndex(DBHelper.BUDGETS_COL_B_UID));
            float b_amt=c.getFloat(c.getColumnIndex(DBHelper.BUDGETS_COL_B_AMOUNT));
            String b_cur=c.getString(c.getColumnIndex(DBHelper.BUDGETS_COL_B_CURRENCY));
            String b_sDate=c.getString(c.getColumnIndex(DBHelper.BUDGETS_COL_START_DATE));
            String b_eDate=c.getString(c.getColumnIndex(DBHelper.BUDGETS_COL_END_DATE));
            c.close();
            Intent i=new Intent(BudgetsActivity.this,AddBudgetActivity.class);
            i.putExtra("b_id",b_id);
            i.putExtra("b_uid",b_uid);
            i.putExtra("b_amt",b_amt);
            i.putExtra("b_cur",b_cur);
            i.putExtra("b_sDate",b_sDate);
            i.putExtra("b_eDate",b_eDate);
            startActivity(i);
        }

        if(id==R.id.Delete)
        {
            dbHelper.deleteBudget(bud_db.b_id, sp.getInt("UID", 0));
            Intent i=new Intent(BudgetsActivity.this,BudgetsActivity.class);
            startActivity(i);
            Toast.makeText(BudgetsActivity.this, "Budget Deleted", Toast.LENGTH_LONG).show();
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
    }

    @Override
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
        else if(id == R.id.action_acc)
        {
            Intent i =new Intent(this, AddAccountActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_person)
        {
            Intent i =new Intent(this, AddPersonActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_trans)
        {
            Intent i =new Intent(this, AddTransactionsActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_budget)
        {
            Intent i =new Intent(this, AddBudgetActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
