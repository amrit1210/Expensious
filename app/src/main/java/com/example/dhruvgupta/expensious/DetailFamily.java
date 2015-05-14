package com.example.dhruvgupta.expensious;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Amrit on 5/11/2015.
 */
public class DetailFamily extends ActionBarActivity
{
    TextView bud_name,bud_cur;
    EditText bud_amount;
    int u_id,u_fid,flag=0, currentYear, currentMonth, transYear, transMonth;
    float u_budget;
    Date dd;
    SimpleDateFormat sdf;
    ListView mem_list;
    ArrayList<TransactionsDB>al;
    ArrayList<CurrencyDB> al1;
    TransactionFamilyAdapter adapter;
//    TransactionAdapter adapter;
    String u_name;
    boolean isAmtEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_bud_trans);
        bud_name=(TextView)findViewById(R.id.budget_name);
        bud_amount=(EditText)findViewById(R.id.budget_bal);
        bud_amount.setEnabled(false);
        bud_cur=(TextView)findViewById(R.id.budget_cur);
        mem_list=(ListView)findViewById(R.id.mem_trans_list);
        al=new ArrayList<>();
        sdf = new SimpleDateFormat("dd-MM-yyyy");

        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        Log.i("Current date", cal + " : " + currentMonth + " : " + currentYear);

        if(getIntent().getIntExtra("u_id", 0)!=0)
        {
            flag=1;
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Member_Budget");
            query.whereEqualTo("u_id", getIntent().getIntExtra("u_id", 0));

            try {
                ParseObject mBud = query.getFirst();
                bud_amount.setText(mBud.getDouble("u_budget")+"");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bud_name.setText(getIntent().getStringExtra("User_Name"));
            u_id=getIntent().getIntExtra("u_id",0);
            u_fid=getIntent().getIntExtra("u_fid",0);

            ParseQuery<ParseObject> curQuery = ParseQuery.getQuery("Settings");
            curQuery.whereEqualTo("settings_uid", u_id);
            curQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    al1 = new ListOfCurrencies().getAllCurrencies();

                    Iterator<CurrencyDB> iterator = al1.iterator();
                    while (iterator.hasNext()) {
                        CurrencyDB curDB = iterator.next();
                        if (curDB.c_code.equals(parseObject.getString("settings_cur_code")))
                            bud_cur.setText(curDB.c_symbol);
                    }
                }
        });
        }

        if(u_id!=0 && u_fid!=0)
        {
//            adapter=new TransactionAdapter(DetailFamily.this,R.layout.list_transaction,al);
            adapter=new TransactionFamilyAdapter(DetailFamily.this,R.layout.list_transaction,al,u_id);
            mem_list.setAdapter(adapter);
//            ParseObject transactions=new ParseObject("Transactions");
            ParseQuery<ParseObject>trans=ParseQuery.getQuery("Transactions");
            trans.whereEqualTo("trans_uid",u_id);
            trans.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    for(ParseObject parseObject:parseObjects)
                    {
                        String date = parseObject.getString("trans_date");
                        try {
                            dd = sdf.parse(date);
                        } catch (java.text.ParseException e1) {
                            e1.printStackTrace();
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dd);
                        transYear = calendar.get(Calendar.YEAR);
                        transMonth = calendar.get(Calendar.MONTH);
                        Log.i("Trans date", calendar + " : " + transMonth + " : " + transYear);
                        if (transYear == currentYear) {
                            if (transMonth == currentMonth) {
                                TransactionsDB transactionsDB=new TransactionsDB();
                                transactionsDB.t_id=parseObject.getInt("trans_id");
                                transactionsDB.t_balance=Float.parseFloat(parseObject.getDouble("trans_balance")+"");
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
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void onEditBudget()
    {
        bud_amount.setEnabled(true);
        isAmtEditable = true;
        invalidateOptionsMenu();
    }

    public void onSaveBudget()
    {
        int saveFlag = 0;
        String objId = null;
        bud_amount.setEnabled(false);
        isAmtEditable = false;
        invalidateOptionsMenu();

        u_budget=Float.parseFloat(bud_amount.getText().toString());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Member_Budget");
        query.whereEqualTo("u_id", u_id);
        try {
            ParseObject memBud = query.getFirst();
            saveFlag = 1;
            objId = memBud.getObjectId();
            Log.i("memBud", "Entered");

        } catch (ParseException e) {
            e.printStackTrace();
        }

       bud_amount.setEnabled(false);

        if (saveFlag != 0)
        {
            Log.i("memBud if", "Entered if");

            ParseQuery<ParseObject> memQuery = ParseQuery.getQuery("Member_Budget");
            memQuery.getInBackground(objId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    parseObject.put("u_budget", u_budget);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                                Log.i("Member Budget saved", "YES! YES! YES!");
                            else
                                Log.i("Member Budget failed", e.getMessage());
                        }
                    });
                }
            });
        }
        else
        {
            Log.i("memBud else", "Entered else");
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
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (isAmtEditable) {
            getMenuInflater().inflate(R.menu.menu_login, menu);

            Drawable saveBtn = new IconDrawable(this, Iconify.IconValue.fa_check_circle_o)
                    .colorRes(R.color.accent_color_200)
                    .actionBarSize();
            menu.findItem(R.id.action_done).setIcon(saveBtn);
        } else {
            getMenuInflater().inflate(R.menu.menu_login2, menu);

            Drawable editBtn = new IconDrawable(this, Iconify.IconValue.fa_pencil)
                    .colorRes(R.color.accent_color_200)
                    .actionBarSize();
            menu.findItem(R.id.action_edit).setIcon(editBtn);
        }

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

