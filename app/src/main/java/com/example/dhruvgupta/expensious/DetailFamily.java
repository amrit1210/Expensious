package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amrit on 5/11/2015.
 */
public class DetailFamily extends ActionBarActivity
{
    TextView bud_name,bud_cur;
    EditText bud_amount;
    int u_id,u_fid,flag=0;
    float u_budget;
    ListView mem_list;
    ArrayList<TransactionsDB>al;
    TransactionAdapter adapter;
    String u_name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_bud_trans);
        bud_name=(TextView)findViewById(R.id.budget_name);
        bud_amount=(EditText)findViewById(R.id.budget_bal);
        bud_cur=(TextView)findViewById(R.id.budget_cur);
        mem_list=(ListView)findViewById(R.id.mem_trans_list);
        al=new ArrayList<>();
        adapter=new TransactionAdapter(DetailFamily.this,R.layout.list_transaction,al);
        mem_list.setAdapter(adapter);
        if(getIntent().getIntExtra("u_id", 0)!=0)
        {
            flag=1;
            bud_name.setText(getIntent().getStringExtra("User_Name"));
            u_id=getIntent().getIntExtra("u_id",0);
            u_fid=getIntent().getIntExtra("u_fid",0);
        }

        if(u_id!=0 && u_fid!=0)
        {
//            ParseObject transactions=new ParseObject("Transactions");
            ParseQuery<ParseObject>trans=ParseQuery.getQuery("Transactions");
            trans.whereEqualTo("trans_uid",u_id);
            trans.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    for(ParseObject todo:parseObjects)
                    {
                    todo.fetchInBackground( new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            TransactionsDB transactionsDB=new TransactionsDB();
                            transactionsDB.t_id=parseObject.getInt("trans_id");
                            transactionsDB.t_balance=parseObject.getDouble("trans_balance");
                            transactionsDB.t_from_acc=parseObject.getInt("trans_from_acc");
                            transactionsDB.t_to_acc=parseObject.getInt("trans_to_acc");
                            transactionsDB.t_c_id=parseObject.getInt("trans_category");
                            transactionsDB.t_sub_id=parseObject.getInt("trans_subcategory");
                            transactionsDB.t_date=parseObject.getString("trans_date");
                            transactionsDB.t_time=parseObject.getString("trans_time");
                            transactionsDB.t_note=parseObject.getString("trans_note");
                            transactionsDB.t_type=parseObject.getString("trans_type");
                            transactionsDB.t_p_id=parseObject.getInt("trans_person");
                            transactionsDB.t_show=parseObject.getInt("trans_show");
                            transactionsDB.t_rec_id=parseObject.getInt("trans_rec_id");
                            transactionsDB.t_u_id=parseObject.getInt("trans_uid");
                            al.add(transactionsDB);
                        }
                    });
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void onEditBudget()
    {
        bud_amount.setEnabled(true);
    }
    public void onSaveBudget()
    {
        u_budget=Float.parseFloat(bud_amount.getText().toString());
        ParseObject saveBudget = new ParseObject("Member_Budget");
        ParseACL addAcl = new ParseACL();
        addAcl.setPublicReadAccess(true);
        addAcl.setPublicWriteAccess(true);
        saveBudget.setACL(addAcl);
        saveBudget.put("u_id", u_id);
        saveBudget.put("u_fid", u_fid);
        saveBudget.put("u_budget", u_budget);

        saveBudget.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("Member Budget saved", "YES! YES! YES!");
            }
        });
       bud_amount.setEnabled(false);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        Drawable saveBtn = new IconDrawable(this, Iconify.IconValue.fa_check_circle_o)
                .colorRes(R.color.accent_color_200)
                .actionBarSize();
        menu.findItem(R.id.action_done).setIcon(saveBtn);
        Drawable editBtn = new IconDrawable(this, Iconify.IconValue.fa_pencil)
                .colorRes(R.color.accent_color_200)
                .actionBarSize();
        menu.findItem(R.id.action_edit).setIcon(editBtn);

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
        if (id == R.id.action_done)
        {
           onSaveBudget();
            return true;
        }
        if (id == R.id.action_edit)
        {
           onEditBudget();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

