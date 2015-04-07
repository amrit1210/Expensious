package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Gaurav on 04-Apr-15.
 */
public class BudgetsActivity extends ActionBarActivity
{
    TextView mB_Amt,mB_Cur,mB_Spent,mB_Spent_cur,mB_Rem,mB_Rem_Cur;
    ListView mBudget_list;

    SharedPreferences sp;
    DBHelper dbHelper;
    ArrayList<BudgetDB> al;
    BudgetsAdapter budgetsAdapter;

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

        al=dbHelper.getAllBudgets(sp.getInt("UID",0));
        budgetsAdapter =new BudgetsAdapter(BudgetsActivity.this,R.layout.list_budget,al);
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
}
