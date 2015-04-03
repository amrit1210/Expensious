package com.example.dhruvgupta.expensious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
* Created by Gaurav on 19-Mar-15.
*/
public class CategoriesActivity extends ActionBarActivity
{

    Button mCat_Income,mCat_Expense;
    int c_type;
    CategoriesAdapter ad;
    ArrayList<String> al;
    ArrayList<SubCategoryDB>sub_cat_al;
    ArrayList<CategoryDB_Specific>cat_al;
    DBHelper dbHelper;
    SharedPreferences sp;
    ListView mList_cat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        al=new ArrayList();
        mList_cat = (ListView) findViewById(R.id.category_list);
        mCat_Income = (Button) findViewById(R.id.category_btn_income);
        mCat_Expense = (Button) findViewById(R.id.category_btn_expense);
        sp = getSharedPreferences("USER_PREFS",MODE_PRIVATE);
        dbHelper =new DBHelper(CategoriesActivity.this);
        c_type=0;
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),0);
        for(int i=0;i<cat_al.size();i++)
    {
        CategoryDB_Specific categoryDB_specific=cat_al.get(i);
        sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
        al.add(categoryDB_specific.c_name);
        for(int j=0;j<sub_cat_al.size();j++)
        {
            SubCategoryDB subCategoryDB=sub_cat_al.get(j);
            al.add(subCategoryDB.sub_name);
        }

    }
        Log.i("ArrayList:",al+"");
        ad=new CategoriesAdapter(CategoriesActivity.this,R.layout.list_category,al);
        mList_cat.setAdapter(ad);
        registerForContextMenu(mList_cat);
    }

    public void onIncomeBtnClick(View v)
    {
        c_type=1;
      // al1= setCategoryGroups();
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),1);
        for(int i=0;i<cat_al.size();i++)
        {
            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
            al.add(categoryDB_specific.c_name);
            for(int j=0;j<sub_cat_al.size();j++)
            {
                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
                al.add(subCategoryDB.sub_name);
            }

        }
        ad=new CategoriesAdapter(CategoriesActivity.this,R.layout.list_category,al);
        mList_cat.setAdapter(ad);
        registerForContextMenu(mList_cat);
    }

    public void onExpenseBtnClick(View v)
    {
        c_type=0;
       //al1= setCategoryGroups();
        cat_al= dbHelper.getAllCategories(sp.getInt("UID",0),0);
        for(int i=0;i<cat_al.size();i++)
        {
            CategoryDB_Specific categoryDB_specific=cat_al.get(i);
            sub_cat_al=dbHelper.getAllSubCategories(sp.getInt("UID",0),categoryDB_specific.c_id);
            al.add(categoryDB_specific.c_name);
            for(int j=0;j<sub_cat_al.size();j++)
            {
                SubCategoryDB subCategoryDB=sub_cat_al.get(j);
                al.add(subCategoryDB.sub_name);
            }

        }
        ad=new CategoriesAdapter(CategoriesActivity.this,R.layout.list_category,al);
        mList_cat.setAdapter(ad);
        registerForContextMenu(mList_cat);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int listPosition = info.position;
//        Object str1 =  mList_cat.getAdapter().getItem(listPosition);
//        int id=item.getItemId();
//
//        if(id==R.id.Edit)
//        {
//            Cursor c= dbHelper.getCategoryData(category_DB.c_id);
//            c.moveToFirst();
//            int c_id=c.getInt(c.getColumnIndex(DBHelper.CATEGORY_COL_C_ID));
//            int c_uid=c.getInt(c.getColumnIndex(DBHelper.CATEGORY_COL_C_UID));
//            String c_name=c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_NAME));
//            String c_type=c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_TYPE));
//            String c_icon=c.getString(c.getColumnIndex(DBHelper.CATEGORY_COL_C_ICON));
//            Intent i=new Intent(CategoriesActivity.this,AddCategoryActivity.class);
//            i.putExtra("c_id",c_id);
//            i.putExtra("c_u_id",c_uid);
//            i.putExtra("c_name",c_name);
//            i.putExtra("c_type",c_type);
//            i.putExtra("c_icon",c_icon);
//            startActivity(i);
//        }
//
//        if(id==R.id.Delete)
//        {
//            dbHelper.deleteCategory(category_DB.c_id,sp.getInt("UID",0));
//            Intent i=new Intent(CategoriesActivity.this,CategoriesActivity.class);
//            startActivity(i);
//            Toast.makeText(CategoriesActivity.this, "Category Deleted", Toast.LENGTH_LONG).show();
//        }
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
